package edu.fich.pfcii.snoredetection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

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
    }
}
