package edu.fich.pfcii.snoredetection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnAddRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Comenzar con la activity Onboarding si el usuario nunca finalizo la guia
        // o bien nunca la vio (primera vez que entra a la App)

        // Obtener las Shared Preferences
        SharedPreferences preferences = getSharedPreferences("mis_preferencias", MODE_PRIVATE);

        // Chequear si la variable onboarding_complete esta en FALSE
        if (!preferences.getBoolean("onboarding_complete", false)) {
            // Iniciar la actividad Onboarding
            Intent onboarding = new Intent(this, OnBoardingActivity.class);
            startActivity(onboarding);

            // Cerrar la Actividad Principal
            finish();
            return;
        }

        //Comenzar una nueva grabación si es que no elegimos alguna del listado.

        //Referencio al boton para agregar una grabacion nueva
        btnAddRecord = (Button)findViewById(R.id.btnAddRecord);

        //Iniciar la actividad AddRecord
        btnAddRecord.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0)
            {
                Intent intentAddRecord = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(intentAddRecord);
            }
        });

    }
}
