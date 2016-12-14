package edu.fich.pfcii.snoredetection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import edu.fich.pfcii.snoredetection.helper.Helper;
import edu.fich.pfcii.snoredetection.model.Snore;

public class DetalleCaptura extends AppCompatActivity {

    private final static String ITEM_PERIODOS = "PERIODOS";
    private final static String ITEM_AMPLITUDES = "AMPLITUDES";

    private TextView amplitud;
    private TextView periodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_captura);

        amplitud = (TextView) findViewById(R.id.detalle_amplitud_value);
        periodo = (TextView) findViewById(R.id.detalle_periodo_value);

        ArrayList<Double> amplitudes = (ArrayList<Double>) getIntent().getSerializableExtra(ITEM_AMPLITUDES);
        ArrayList<Double> periodos   = (ArrayList<Double>) getIntent().getSerializableExtra(ITEM_PERIODOS);

        String value_amplitud, value_periodo;
        value_amplitud = getAverageOfDouble(amplitudes);
        value_periodo  = getAverageOfDouble(periodos);

        amplitud.setText(value_amplitud);
        periodo.setText(value_periodo + " s");

    }

    private String getAverageOfDouble(ArrayList<Double> values) {
        Double sum = 0.0;

        if (values != null && !values.isEmpty()) {
            int amount = values.size();
            for (Double val : values) {
                sum += val;
            }
            return Double.toString(Helper.round((sum / amount), 4));
        }
        return Double.toString(Helper.round(sum, 2));
    }
}
