package edu.fich.pfcii.snoredetection;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RecordActivity extends AppCompatActivity {

    Button btnIniciar;
    Button btnDetener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        //Referencio a los botones iniciar y detener grabacion
        btnIniciar = (Button)findViewById(R.id.btnIniciar);
        btnDetener = (Button)findViewById(R.id.btnDetener);

        //Lanzo el proceso para comenzar grabación
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0)
            {
                Context context = getApplicationContext();
                CharSequence text = "Grabación iniciada";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        //Lanzo el proceso de detener grabación
        btnDetener.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0)
            {
                Context context = getApplicationContext();
                CharSequence text = "Grabación detenida";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

    }
}
