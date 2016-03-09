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
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity {

    // Parametros de la clase AudioRecord
    private static final int RECORDER_SAMPLERATE = 8000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord recorder;
    private int bufferSize = 0;
    private Thread recordingThread;
    private boolean isRecording = false;

    // Definir los vectores donde almacenar el fragmento y su energía
//    ArrayList<Float> fragmento;
    ArrayList<Float> energia = new ArrayList<>();


    /// PARTE NUEVA
    static final int SAMPLES_PER_SECOND_AT_8K = 480000;
    static final int SIZE_SEGMENTO = 5*SAMPLES_PER_SECOND_AT_8K;
    float[] fragmento2 = new float[SIZE_SEGMENTO];
    float[] filtrada2  = new float[SIZE_SEGMENTO];
    float[] muestras2  = new float[30000];


    // Vector para señal filtrada
//    ArrayList<Float> filtrada;

    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        // Setear el estado (activado o desavtivado) de los botones iniciar y detener
        setButtonHandlers();
        enableButtons(false);

        // Calcular el tamaño del buffer por medio de la clase AudioRecord
        bufferSize = AudioRecord.getMinBufferSize(
                RECORDER_SAMPLERATE,
                RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING);

        // Registrar el IntentService
        IntentFilter filter = new IntentFilter();
        filter.addAction(T0IntentService.ACTION_PROGRESO);
        receiver = new BroadcastReceiver() {
            // Analizar los datos recibidos y determino si es ronquido o no
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(T0IntentService.ACTION_PROGRESO)) {

                    float t0 = intent.getFloatExtra("tcero", 0);
                    float max = intent.getFloatExtra("maximo", 0);
                    energia.add(intent.getFloatExtra("energia", 0));

                    String resultado = ((TextView)findViewById(R.id.datos)).getText().toString();

                    //Etiqueto el fragmento como ronquido o no ronquido
                    String ronquido;
                    if ((t0 > 3.05f) && (t0 < 5.9f) && (max > 0.25f)) {
                        ronquido = "Ronquidos";
                    } else {
                        ronquido = "No ronquido";
                    }

                    //Imprimo provisoriamente los resultados en el TextView, porteriormente se mostraran los resultados de mejor manera
                    ((TextView)findViewById(R.id.datos)).setText(resultado + "\n" + t0 +"--"+ max +"--"+ ronquido);
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

    // Activar o desactivar el boton pasado como parámetro
    private void enableButton(int id, boolean isEnable) {
        findViewById(id).setEnabled(isEnable);
    }

    // Activar y desactivar los botones iniciar y detener en funcion de si esta grabando o no
    private void enableButtons(boolean isRecording) {
        enableButton(R.id.btnStart, !isRecording);
        enableButton(R.id.btnStop, isRecording);
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

        ((TextView)findViewById(R.id.datos)).setText("T0(seg)\tAmplitud (Normalizada)\tEtiqueta");
    }

    // Esta funcion es la ejecutada por el hilo de la captura
    // Arma los fragmentos del tamaño necesario y luego se crean los IntentServices para calcular la autocorrelacion
    private void writeAudioDataToFile() {

        short[] data = new short[bufferSize];

        int read;

//        fragmento = new ArrayList<>();
//        filtrada  = new ArrayList<>();


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

                // Obtener el fragmento correspondiente a la ventana de 1 minuto de duración
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
                            Intent pitch = new Intent(RecordActivity.this, T0IntentService.class);
//                            pitch.putExtra("fragmento", muestras);
                            pitch.putExtra("fragmento", muestras2);
                            startService(pitch);

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
        if (null != recorder){
            isRecording = false;

            int i = recorder.getState();
            if (i == 1)
                recorder.stop();

            recorder.release();

            recorder = null;
            recordingThread = null;
        }
    }

    // Setear los eventos OnClick de los botones iniciar y detener
    private View.OnClickListener btnClick = new View.OnClickListener() {

        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btnStart:{
                    Context context = getApplicationContext();
                    Toast.makeText(context, "Grabación iniciada", Toast.LENGTH_SHORT).show();

                    enableButtons(true);
                    startRecording();

                    break;
                }
                case R.id.btnStop:{
                    Context context = getApplicationContext();
                    Toast.makeText(context, "Grabación detenida", Toast.LENGTH_SHORT).show();

                    enableButtons(false);
                    stopRecording();

                    break;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
