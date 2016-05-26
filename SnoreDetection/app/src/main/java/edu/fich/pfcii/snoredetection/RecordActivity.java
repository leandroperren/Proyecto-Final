package edu.fich.pfcii.snoredetection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Vector;

public class RecordActivity extends AppCompatActivity {

    //Parametros para la clase AudioRecord
    private static final int RECORDER_SAMPLERATE = 8000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord recorder = null;
    private int bufferSize = 0;
    private Thread recordingThread = null;
    private boolean isRecording = false;

    //Defino los vectores donde almaceno el fragmento y su energía
    Vector<Double> fragmento;
    Vector<Double> energia;

    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        //seteo el estado (activado o desavtivado) de los botones iniciar y detener
        setButtonHandlers();
        enableButtons(false);

        //calculo el tamaño del buffer por medio de la clase AudioRecord
        bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

        //Registro el IntentService
        IntentFilter filter = new IntentFilter();
        filter.addAction(T0IntentService.ACTION_PROGRESO);
        receiver = new BroadcastReceiver() {
            //Analizo los datos recibidos y determino si es ronquido o no
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(T0IntentService.ACTION_PROGRESO)) {

                    double t0 = intent.getDoubleExtra("tcero", 0);
                    double max = intent.getDoubleExtra("maximo", 0);
                    energia.add(intent.getDoubleExtra("energia", 0));

                    String resultado=((TextView)findViewById(R.id.TxtResultado)).getText().toString();

                    //Etiqueto el fragmento como ronquido o no ronquido
                    String ronquido;
                    if ((t0 > 3.05) && (t0 < 5.9) && (max > 0.25)) {
                        ronquido = "Ronquidos";
                    } else {
                        ronquido = "No ronquido";
                    }

                    //Imprimo provisoriamente los resultados en el TextView, porteriormente se mostraran los resultados de mejor manera
                    ((TextView)findViewById(R.id.TxtResultado)).setText(resultado + "\n" + t0 +"--"+ max +"--"+ ronquido);
                }
            }
        };
        registerReceiver(receiver, filter);
    }

    //seteo los métodos onclick a los botones iniciar y detener
    private void setButtonHandlers() {
        ((Button)findViewById(R.id.btnStart)).setOnClickListener(btnClick);
        ((Button)findViewById(R.id.btnStop)).setOnClickListener(btnClick);
    }

    //Activo o desactivo el boton pasado como parámetro
    private void enableButton(int id,boolean isEnable){
        ((Button)findViewById(id)).setEnabled(isEnable);
    }

    //Activo y desactivo los botones iniciar y detener de acuerdo a si esta grabando o no
    private void enableButtons(boolean isRecording) {
        enableButton(R.id.btnStart, !isRecording);
        enableButton(R.id.btnStop, isRecording);
    }

    //comienza la grabacion creando el hilo para la captura via micrófono
    private void startRecording(){

        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING, bufferSize);

        int i = recorder.getState();
        if(i==1)
            recorder.startRecording();

        isRecording = true;

        recordingThread = new Thread(new Runnable() {

            @Override
            public void run() {
                writeAudioDataToFile();
            }
        },"AudioRecorder Thread");

        recordingThread.start();

        ((TextView)findViewById(R.id.TxtResultado)).setText("T0(seg)                 Amplitud (Normalizada)               Etiqueta");
    }

    //Esta funcion es lo que hace el hilo de la captura
    //Arma los fragmentos del tamaño necesario y luego se crean los IntentServices para calcular la autocorrelacion
    private void writeAudioDataToFile(){

        short data[] = new short[bufferSize];

        int read = 0;

        fragmento = new Vector<Double>();
        energia = new Vector<Double>();

        int j=0;

        //Umbral funcion de recorte
        double CL = 2.3;

        //coeficientes del filtro FIR pasa-bajos de orden 6 con frecuencia de corte en pi/80
        double B[] =  {0.0237,    0.0928,    0.2323,    0.3024,    0.2323,    0.0928,    0.0237};
        //orden del filtro FIR
        int orden = 6;

        while(isRecording){
            read = recorder.read(data, 0, bufferSize);

            for (int i = 0; i < read; i=i+1) {

                double value = data[i];

                //Aplico la función de recorte numero 3 con umbral CL
                if (value< CL) {
                    value=0;
                }

                //Obtengo el fragmento correspondiente a la ventana de 1 minuto de duración
                if (j < (1*480000)) {
                    fragmento.add(value);
                    j++;
                } else {
                    fragmento.add(value);

                    //Una vez que tengo el fragmento procedo a filtrarlo y submuestrearlo (Decimation).

                    //filtro los datos del fragmento eliminando las altas frecuencias
                    Vector<Double> filtrada = new Vector<Double>();
                    int size = fragmento.size();

                    for (int n = 0; n < size; n++) {
                        double muestra = 0;
                        double anterior;
                        for (int k = 0; k <= orden; k++) {
                            if (n > k) {
                                anterior = fragmento.elementAt(n - k);
                                muestra = muestra + B[k] * anterior;
                            }
                        }
                        filtrada.add(muestra);
                    }

                    fragmento.clear();

                    //Submuestrea a 100Hz la señal recientemente filtrada
                    for (int n = 0; n < filtrada.size(); n = n + 80) {
                        fragmento.add(filtrada.elementAt(n));
                    }
                    //Guardo el fragmento en nuevo array para poder pasarselo al IntentService
                    double muestras[]= new double[fragmento.size()];
                    for (int n = 0; n < fragmento.size(); n++) {
                        muestras[n]=fragmento.elementAt(n);
                    }

                    //Inicio el IntentService que calcula el t0
                    Intent T0Intent = new Intent(RecordActivity.this, T0IntentService.class);
                    T0Intent.putExtra("fragmento", muestras);
                    startService(T0Intent);

                    fragmento.clear();
                    j = 0;
                }
            }
        }

    }

    //Funcion para detener el hilo de grabación
    private void stopRecording() {
        if (null != recorder){
            isRecording = false;

            int i = recorder.getState();
            if(i==1)
                recorder.stop();
            recorder.release();

            recorder = null;
            recordingThread = null;
        }
    }

    //Seteo los eventos click de los botones iniciar y detener
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
}
