package edu.fich.pfcii.snoredetection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.fich.pfcii.snoredetection.db.DatabaseManagerSnore;
import edu.fich.pfcii.snoredetection.helper.Helper;

public class RecordActivity extends AppCompatActivity {

    private int CONTADOR_TIEMPO = 0;

    // Parametros de la clase AudioRecord
    private static final int RECORDER_SAMPLERATE = 8000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord recorder;
    private int bufferSize = 0;
    private Thread recordingThread;
    private boolean isRecording = false;

    // Definir los vectores donde almacenar período, energía y tiempo
    public ArrayList<Double> periodo  = new ArrayList<>();
    public ArrayList<Double> amplitud = new ArrayList<>();
    public ArrayList<Integer> tiempo  = new ArrayList<>();
    public ArrayList<Double> energia  = new ArrayList<>();


    /// PARTE NUEVA
    static final int SAMPLES_PER_SECOND_AT_8K = 480000;
    static final int SIZE_SEGMENTO            = 5*SAMPLES_PER_SECOND_AT_8K;
    static final int SIZE_AT_100HZ            = SIZE_SEGMENTO/80;

    private TextView resultados_parciales;

    // Vector para señal filtrada
//    ArrayList<Float> filtrada;

    private BroadcastReceiver receiver;

    // Manejador de la BBDD SQLite
    private DatabaseManagerSnore managerSnore;

    // Variables para control de tiempo
    private long horaInicio;
    private long horaFin;

    // Helper para convertir vectores a string y viceversa, ademas de obtener tiempos
    private Helper helper = new Helper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        // Recuperar la variable isRecording si existe
        if (null != savedInstanceState) {
            // Si la variable fue guardada anteriormente se asigna a isRecording
            isRecording = savedInstanceState.getBoolean("IS_RECORDING");
        }

        // Setear el estado (activado o desavtivado) de los botones iniciar y detener
        setButtonHandlers();
        enableButtons(isRecording);

        // Calcular el tamaño del buffer por medio de la clase AudioRecord
        bufferSize = AudioRecord.getMinBufferSize(
                RECORDER_SAMPLERATE,
                RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING);

        // Registrar el IntentService
        IntentFilter filter = new IntentFilter();
        filter.addAction(T0IntentService.ACTION_PROGRESO);

        resultados_parciales = (TextView)findViewById(R.id.datos);

        // Intanciar Objeto para la BBDD
        managerSnore = new DatabaseManagerSnore(this);

        receiver = new BroadcastReceiver() {

            // Analizar los datos recibidos y determino si es ronquido o no
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(T0IntentService.ACTION_PROGRESO)) {

                    /*float t0 = intent.getFloatExtra("tcero", 0);
                    float max = intent.getFloatExtra("maximo", 0);
                    double energy = intent.getDoubleExtra("energia", 0.0);
                    energia.add(energy);
                    */

                    /*-----------------------------------------------------
                     *  @@@@ Hay que guardar estos valores:
                     *-----------------------------------------------------
                     *      periodo
                     *      amplitud
                     *      energia
                     */
                    float periodo_value  = intent.getFloatExtra("periodo", .0f);
                    float amplitud_value = intent.getFloatExtra("amplitud", 0.f);
                    //int tiempo_value     = intent.getIntExtra("tiempo", 0);
                    // NOTA: en este caso la energía no tiene sentido, ya que solo se usa
                    // para calcular el umbral que debe pasar la amplitud, del 25%
                    float energia_value  = intent.getFloatExtra("energia", .0f);

                    int tiempo_value = CONTADOR_TIEMPO;
                    CONTADOR_TIEMPO++;

                    //Etiqueto el fragmento como ronquido o no ronquido
                    // Actualizar con datos nuevos
                    /*String ronquido;
                    if ((t0 > 3.05f) && (t0 < 5.9f) && (max > 0.25f)) {
                        ronquido = "Ronquidos";
                    } else {
                        ronquido = "No ronquido";
                    }*/
                    boolean is_snore = false;
                    if ((periodo_value > 3.05f) && (periodo_value < 5.9f) && amplitud_value > 0.25f) {
                        is_snore = true;

                        // Salvar los valores anteriores en sus vectores
                        // que luego seran guardados hacia la BBDD
                        periodo.add((double)periodo_value);
                        amplitud.add((double)amplitud_value);
                        tiempo.add(tiempo_value);
                        energia.add((double)energia_value); // esto no va a la BBDD
                    }


                    // Imprimir en pantalla provisoriamente los resultados en el TextView
                    // parte vieja
                    /*resultados_parciales.setText(
                            resultados_parciales.getText().toString()
                            + "\n"
                            + t0 + "\t" + max + "\t" + ronquido + "\t" + energy);
                    */
                    // Parte con datos nuevos
                    resultados_parciales.setText(
                            resultados_parciales.getText().toString()
                                    + "\n"
                                    + periodo_value + " - "
                                    + String.format("%.4f", amplitud_value) + " - "
                                    + tiempo_value + " - "
                                    + (is_snore ? "SI" : "NO"));
                }
            }
        };
        registerReceiver(receiver, filter);
    }

    // Setear los métodos OnClick a los botones iniciar y detener
    private void setButtonHandlers() {
        findViewById(R.id.btnStart).setOnClickListener(btnClick);
        findViewById(R.id.btnStop).setOnClickListener(btnClick);
    }

    // Activar y desactivar los botones iniciar y detener en funcion de si esta grabando o no
    private void enableButtons(boolean isRecording) {
        enableButton(R.id.btnStart, !isRecording);
        enableButton(R.id.btnStop, isRecording);
    }

    // Activar o desactivar el boton pasado como parámetro
    private void enableButton(int id, boolean isEnable) {
        findViewById(id).setEnabled(isEnable);
    }

    // Comenzar la grabacion creando el hilo para la captura via micrófono
    private void startRecording() {

        recorder = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE,
                RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING,
                bufferSize);

        if (1 == recorder.getState())
            recorder.startRecording();

        isRecording = true;

        recordingThread = new Thread(new Runnable() {

            @Override
            public void run() {
                writeAudioDataToFile();
            }
        },"Hilo para AudioRecorder");

        recordingThread.start();

        // Guardar timestamp de inicio de grabacion
        this.horaInicio = helper.getTimestamp();

        resultados_parciales.setText(
                resultados_parciales.getText().toString()
                + "\nT0 - Amp - Tpo - Status");

        // inicio el contador en 0
        this.CONTADOR_TIEMPO = 0;
    }

    // Esta funcion es la ejecutada por el hilo de la captura
    // Arma los fragmentos del tamaño necesario y luego se crean los IntentServices para calcular la autocorrelacion
    private void writeAudioDataToFile() {

        short[] data = new short[bufferSize];

        int read;

//        fragmento = new ArrayList<>();
//        filtrada  = new ArrayList<>();

        final float[] fragmento2 = new float[SIZE_SEGMENTO];
        final float[] filtrada2  = new float[SIZE_SEGMENTO];
        final float[] muestras2  = new float[SIZE_AT_100HZ];

        int j = 0;

        // Umbral para funcion de recorte
        float CL = 2.3f;

        // Definir tamaño de ventana
        int window_size = SIZE_SEGMENTO; // 5 minutos

        while (isRecording) {

            read = recorder.read(data, 0, bufferSize);

            for (int i=0; i<read; i++) {

                float value = data[i];

                // Aplicar la función de recorte numero 3 con umbral CL
                if (value < CL) {
                    value = 0;
                }

                // Construir el fragmento correspondiente a ventana de 5 minutos
//                if (j < (5*480000)) {
                if (j < window_size) {
//                    fragmento.add(value);

                    /// PARTE NUEVA
                    fragmento2[j] = value;

                    j++;
                } else {
//                    fragmento.add(value);

                    /// PARTE NUEVA
//                    fragmento2[j] = value;

                    Thread filtrado = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            // Con fragmento obtenido se procede a filtrarlo y submuestrearlo (Decimation).

                            // Filtrar los datos del fragmento eliminando las altas frecuencias
//                            int size_fragmento = fragmento.size();

                            // Coeficientes del filtro FIR pasa-bajos de orden 6 con frecuencia de corte en pi/80
                            float[] B = {
                                    0.0237f,
                                    0.0928f,
                                    0.2323f,
                                    0.3024f,
                                    0.2323f,
                                    0.0928f,
                                    0.0237f
                            };

                            // Orden del filtro FIR
                            int orden = 6;

//                            for (int n = 0; n < size_fragmento; n++) {
                            for (int n = 0; n < SIZE_SEGMENTO; n++) {
                                float muestra = 0.0f;
                                float anterior;
                                for (int k = 0; k <= orden; k++) {
                                    if (n > k) {
//                                        anterior = fragmento.get(n - k);
//                                        muestra += B[k] * anterior;


                                        /// PARTE NUEVA
                                        anterior = fragmento2[n-k];
                                        muestra += B[k] * anterior;

                                    }
                                }
//                                filtrada.add(muestra);

                                /// PARTE NUEVA
                                filtrada2[n] = muestra;
                            }

//                            fragmento.clear();

                            // Submuestrear a 100Hz la señal recientemente filtrada
//                            int /*size_filtrada = filtrada.size(),*/
//                                    new_size = 0;

//                            for (int n = 0; n < size_filtrada; n += 80) {
                            for (int n = 0; n < SIZE_SEGMENTO; n += 80) {
                                muestras2[n/80] = filtrada2[n];
//                                fragmento.add(filtrada.get(n));
//                                new_size++;
                            }

                            // Guardar el fragmento en nuevo array para poder pasarselo al IntentService
//                            float[] muestras = new float[new_size];
//                            for (int n = 0; n < new_size; n++) {
//                                muestras[n] = fragmento.get(n);
//                            }

                            // Iniciar el IntentService que calcula el t0
                            Intent periodo = new Intent(RecordActivity.this, T0IntentService.class);
//                            periodo.putExtra("fragmento", muestras);
                            periodo.putExtra("fragmento", muestras2);
                            startService(periodo);

                        }
                    }, "Hilo Filtrado y Decimation");

                    // Iniciar el filtrado en hilo separado
                    filtrado.start();

//                    fragmento.clear();
//                    filtrada.clear();
                    j = 0;

                }
            }
        }

    }

    // Funcion para detener el hilo de grabación
    private void stopRecording() {
        // Si el recorder no esta seteado e inicializado, se pone a FALSE la variable isRecording
        if (null != recorder){
            isRecording = false;

            int i = recorder.getState();
            if (i == 1)
                recorder.stop();

            recorder.release();
            recorder = null;

            // Interrupcion del hilo de grabacion para liberar recursos
            Thread.currentThread().interrupt();
            recordingThread = null;

            // Salvo el timestamp de corte de grabacion
            this.horaFin = helper.getTimestamp();

            // Convertimos los vectores a string y salvamos los datos en la BBDD de SQLite
            int countRegistros = this.tiempo.size();
            if (countRegistros > 0) {
                String periodos = helper.getStringFromDouble(this.periodo);
                String amplitudes = helper.getStringFromDouble(this.amplitud);
                String tiempos = helper.getStringFromInteger(this.tiempo);

                // Insertar valores en la BBDD y obtener el ID del registro insertado
                long id = this.managerSnore.insertar(Long.toString(this.horaInicio), Long.toString(this.horaFin), periodos, amplitudes, tiempos);

                //@@@@ Muestro el ID de la inserción con un toast. BORRAR
                Toast.makeText(RecordActivity.this, "ID de la inserción: " + id, Toast.LENGTH_LONG).show();

                //@@@@  Aqui se deberia llamar al intent con los resultados graficos
                //@@@@  pasandole el ID de la inserción realizada como parametro extra
                //@@@@  y allí levantar el registro desde la BBDD y realizar todos calculos
                Intent tabsresultados = new Intent(RecordActivity.this, tabs.class);
                Integer itemid;
                id = id -1;
                itemid = (int) id;
                tabsresultados.putExtra("ITEM_ID", itemid);
                startActivity(tabsresultados);
            } else {
                // Mostrar toast con mensaje de error (éste sobrevive a cambios de activity)
                Toast.makeText(RecordActivity.this, "No hay datos para guardar y graficar", Toast.LENGTH_LONG).show();

                // Volver a la activity principal
                Intent main = new Intent(RecordActivity.this, MainActivity.class);
                startActivity(main);
            }
        }
    }

    // Setear los eventos OnClick de los botones iniciar y detener
    private View.OnClickListener btnClick = new View.OnClickListener() {

        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btnStart:{
                    Toast.makeText(RecordActivity.this, "Grabando...", Toast.LENGTH_LONG).show();
                    enableButtons(true);
                    startRecording();
                    break;
                }
                case R.id.btnStop:{
                    Context context = getApplicationContext();
                    Toast.makeText(context, "Grabación detenida", Toast.LENGTH_SHORT).show();

                    enableButtons(false);
                    stopRecording();

                    // Creamos en instanciamos el Intent hacia los resultados graficos
                    // ver de hacer esto mismo en 376-378
                    /*Intent intent = new Intent(RecordActivity.this, tabs.class);
                    startActivity(intent);
                    */
                    break;
                }
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("IS_RECORDING", isRecording);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
