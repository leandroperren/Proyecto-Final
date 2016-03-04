package edu.fich.pfcii.snoredetection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    Button btnAddRecord;
    FloatingActionButton fabAdd;

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

        // Enlazar el Floating Action Button (FAB)
        fabAdd = (FloatingActionButton)findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 *  @@@@ NOTA: from <matiasm>, to <leandrop>
                 *
                 *  Cambiar lo que esta implementado aca por lo que se quiere hacer:
                 *  como por ejemplo, lanzar otra activity.
                 *
                 *  Aca se hizo un Toast para ver que este bien implementado el FAB
                 */
                Toast.makeText(MainActivity.this, "Ejemplo FAB", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
