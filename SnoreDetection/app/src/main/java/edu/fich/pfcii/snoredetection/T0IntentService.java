package edu.fich.pfcii.snoredetection;

import android.app.IntentService;
import android.content.Intent;

import java.util.Vector;

/**
 * Created by Leandro on 04/03/2016.
 */
public class T0IntentService extends IntentService{

    public static final String ACTION_PROGRESO =
            "edu.fich.pfcii.snoredetection.PROGRESO";

    Vector<Double> autocorrelation;

    public T0IntentService() {
        super("T0IntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        //Recupero el fragmento
        double fragmento[] = intent.getDoubleArrayExtra("fragmento");

        //calculo la autocorrelación
        autocorrelation = new Vector<Double>();
        double aux = 0;
        int size = fragmento.length;

        //Calculo la autocorrelacion para 0=energia, y entre 200 y 600 (2seg y 6 seg).
        for (int s = 0; s < size; s++) {
            aux = 0;
            if (((s>=200) && (s<600)) || (s==0)) {
                for (int k = 0; k < size - s; k++) {
                    aux += fragmento[k] * fragmento[k + s];
                }
            }
            aux /= size;
            autocorrelation.add(aux);
        }

        //Obtengo el T0 para la señal del vector de autocorrelacion
        double max = 0;
        double t0 = 0;
        for (int s = 200; s < 600; s++) {
            if (autocorrelation.elementAt(s) > max) {
                max = autocorrelation.elementAt(s);
                t0 = s / 100f;
            }
        }

        //el elemento 0 de la autocorrelacion es la energia
        double energia = autocorrelation.elementAt(0);

        //normalizo el maximo del T0
        max = max/autocorrelation.elementAt(0);

        //Devuelvo los valores al hilo principal
        Intent bcIntent = new Intent();
        bcIntent.setAction(ACTION_PROGRESO);
        bcIntent.putExtra("tcero", t0);
        bcIntent.putExtra("maximo", max);
        bcIntent.putExtra("energia", energia);
        sendBroadcast(bcIntent);

    }
}
