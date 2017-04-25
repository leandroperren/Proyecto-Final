package edu.fich.pfcii.snoredetection;

import android.app.IntentService;
import android.content.Intent;


public class T0IntentService extends IntentService {

    public static final String ACTION_PROGRESO = "edu.fich.pfcii.snoredetection.PROGRESO";

    public T0IntentService() {
        super("T0IntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Recuperar el segmento/fragmento
        float[] fragmento = intent.getFloatArrayExtra("fragmento");

        // Calcular la autocorrelación
        double aux;
        float[] xcorr = new float[401];
        int size = fragmento.length;

        // Calcular la autocorrelacion para 0=energia, y entre 200 y 600 (2seg y 6 seg).
        for (int s = 0; s < size; s++) {

            aux = 0.0;

            if ((s >= 200 && s < 600) || (s == 0)) {
                int limit = size - s;
                for (int k = 0; k < limit; k++) {
                    aux += fragmento[k] * fragmento[k + s];
                }

            }

            aux /= size;
//            autocorrelation.add((float)aux);

            // NEW
            if (s == 0)
                xcorr[0] = (float)aux;
            else if (s >= 200 && s < 600)
                xcorr[s-199] = (float)aux;
        }

        // Obtener el T0 (pitch) para la señal del vector de autocorrelacion
        float max = 0;
        float t0 = 0;
        for (int s = 200; s < 600; s++) {

//            if (autocorrelation.get(s) > max) {
//                max = autocorrelation.get(s);
//                t0 = (float)s / 100.0f;
//            }

            // NEW
            if (xcorr[s-199] > max) {
                max = xcorr[s-199];
                t0 = (float)s/100f;
            }
        }

        // Elemento en '0' de la autocorrelacion es la energia
//        float energia = autocorrelation.get(0);

        // calcular la energia de todo el segmento
        // Es equivalente a la autocorrelacion en '0'
        // NEW
        float energia = xcorr[0];

        // Normalizar el maximo del T0 (a fines de saber que % de energia posee respecto a la energia maxima)
        max /= energia;

        // Devuelver los valores al hilo de grabacion
        Intent bcIntent = new Intent();
        bcIntent.setAction(ACTION_PROGRESO);
        // Guardar el valor de período
        bcIntent.putExtra("periodo", t0);
        // Guardar el valor de amplitud de ese período detectado
        bcIntent.putExtra("amplitud", max);
        // Guardar el valor de la energia del segmento
        bcIntent.putExtra("energia", energia);
        sendBroadcast(bcIntent);
    }
}
