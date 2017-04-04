package edu.fich.pfcii.snoredetection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private EditText name, lastName, age, doctorEmail;
    private RadioGroup radioSex;
    private RadioButton radio;

    public static final String PREF_CONFIG_NAME = "MisConfiguraciones";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Información");

        // Set EditTexts and RadioGroup
        radioSex    = (RadioGroup) findViewById(R.id.radioSex);
        name        = (EditText) findViewById(R.id.name);
        lastName    = (EditText) findViewById(R.id.lastName);
        age         = (EditText) findViewById(R.id.age);
        doctorEmail = (EditText) findViewById(R.id.doctorEmail);

        // Get all values saved on shared preferences
        SharedPreferences settings = getSharedPreferences(PREF_CONFIG_NAME, MODE_PRIVATE);
        String name_val     = settings.getString("nombre", "");
        String lastName_val = settings.getString("apellido", "");
        int age_val         = settings.getInt("edad", 18);
        String genre_val    = settings.getString("sexo", "F");
        String emailMed_val = settings.getString("email_medico", "");

        // assing values to the edit texts and radio button
        name.setText(name_val);
        lastName.setText(lastName_val);
        age.setText(Integer.toString(age_val));
        doctorEmail.setText(emailMed_val);
        // set radio button
        if (((RadioButton)findViewById(R.id.female)).getText().toString().equals(genre_val.toString())) {
            ((RadioButton)findViewById(R.id.female)).setChecked(true);
        } else {
            ((RadioButton)findViewById(R.id.male)).setChecked(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnSaveInformation);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get radio button ID selected
                int selectedId = radioSex.getCheckedRadioButtonId();

                // get radio button from its ID
                radio = (RadioButton) findViewById(selectedId);

                // Set Shared Preferences
                SharedPreferences settings = getSharedPreferences(PREF_CONFIG_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("nombre", name.getText().toString());
                editor.putString("apellido", lastName.getText().toString());
                editor.putInt("edad", Integer.parseInt(age.getText().toString()));
                editor.putString("sexo", radio.getText().toString());
                editor.putString("email_medico", doctorEmail.getText().toString());
                editor.commit();

                //Snackbar.make(view, "Datos guardados correctamente.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                // Show a message to the user
                Toast.makeText(getApplicationContext(), "Datos almacenados correctamente.", Toast.LENGTH_LONG).show();

                // Redirect user to main activity
                Intent main = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(main);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
