package edu.fich.pfcii.snoredetection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.Toast;

import java.util.ArrayList;

import edu.fich.pfcii.snoredetection.db.DatabaseManagerSnore;
import edu.fich.pfcii.snoredetection.helper.Helper;
import edu.fich.pfcii.snoredetection.model.Snore;

public class MainActivity extends AppCompatActivity implements SnoreAdapter.ItemClickCallback {

    private final static String ITEM_PERIODOS = "PERIODOS";
    private final static String ITEM_AMPLITUDES = "AMPLITUDES";

    private RecyclerView lista;
    private FloatingActionButton fabAdd;

    private DatabaseManagerSnore managerSnore;
    private ArrayList<Snore> datos;
    private Helper helper = new Helper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Grabaciones");
        getSupportActionBar().setSubtitle("Lista de grabaciones");
        getSupportActionBar().setIcon(R.drawable.ic_launcher_demo);

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

        managerSnore = new DatabaseManagerSnore(this);
        // Cargar la lista de datos desde la DB (ahora esta de prueba)
        //generarValoresParaDB();
        datos = managerSnore.getSnoreList();

        // Agregar la lista de resultados con RecyclerView y Adapter
        lista = (RecyclerView) findViewById(R.id.lista);
        SnoreAdapter adaptador = new SnoreAdapter(datos, this);
        lista.setAdapter(adaptador);

        adaptador.setItemClickCallback(this);


        // Set layout
        lista.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Set divider
        lista.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        lista.setItemAnimator(new DefaultItemAnimator());

        // Enlazar el Floating Action Button (FAB)
        fabAdd = (FloatingActionButton)findViewById(R.id.fab);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar activity para Nueva grabación
                Intent intentAddRecord = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(intentAddRecord);
            }
        });

    }

    private void generarValoresParaDB() {
        managerSnore.eliminarTodo();
        for (int i=0; i<10; i++) {

            if (i == 0 || i == 3 || i == 5 || i == 9) {
                ArrayList<Double> t0 = new ArrayList<Double>() {{
                    add(1.5); add(2.6); add(3.8); add(4.1);
                }};
                ArrayList<Double> amplitud = new ArrayList<Double>() {{
                    add(0.35); add(0.44); add(0.32); add(0.51);
                }};
                ArrayList<Integer> tiempo = new ArrayList<Integer>() {{
                    add(3); add(5); add(11); add(13);
                }};

                // insertar en DB
                //insertar(String hora_inicio, String hora_fin, String t0, String amplitud, String tiempo)
                managerSnore.insertar(Long.toString(helper.getTimestamp()), Long.toString(helper.getTimestamp() + 1457), helper.getStringFromDouble(t0), helper.getStringFromDouble(amplitud), helper.getStringFromInteger(tiempo));

            } else if (i == 1 || i == 2 || i == 8) {
                ArrayList<Double> t0 = new ArrayList<Double>() {{
                    add(3.2); add(1.8); add(3.1); add(4.7);
                    add(1.5); add(2.6); add(3.8); add(2.8);
                }};
                ArrayList<Double> amplitud = new ArrayList<Double>() {{
                    add(0.35); add(0.44); add(0.32); add(0.51);
                    add(0.55); add(0.48); add(0.47); add(0.84);
                }};
                ArrayList<Integer> tiempo = new ArrayList<Integer>() {{
                    add(3);
                    add(5);
                    add(11);
                    add(13);
                    add(3);
                    add(5);
                    add(11);
                    add(13);
                }};

                // insertar en DB
                //insertar(String hora_inicio, String hora_fin, String t0, String amplitud, String tiempo)
                managerSnore.insertar(
                        Long.toString(helper.getTimestamp()),
                        Long.toString(helper.getTimestamp() + 23324),
                        helper.getStringFromDouble(t0),
                        helper.getStringFromDouble(amplitud),
                        helper.getStringFromInteger(tiempo)
                );

            } else if (i == 4 || i == 6 || i == 7) {
                ArrayList<Double> t0 = new ArrayList<Double>() {{
                    add(1.5);
                    add(2.6);
                    add(3.8);
                    add(4.1);
                    add(2.2);
                    add(2.6);
                    add(3.8);
                    add(4.1);
                    add(2.5);
                    add(2.6);
                    add(4.8);
                    add(4.1);
                    add(3.5);
                    add(2.6);
                    add(2.8);
                    add(4.1);
                    add(1.3);
                    add(2.6);
                    add(0.8);
                    add(4.1);
                    add(1.8);
                    add(2.6);
                    add(8.8);
                    add(4.1);
                    add(1.4);
                    add(2.6);
                    add(1.8);
                    add(4.1);
                    add(4.5);
                    add(2.6);
                    add(3.8);
                    add(4.1);
                }};
                ArrayList<Double> amplitud = new ArrayList<Double>() {{
                    add(0.35);
                    add(0.44);
                    add(0.32);
                    add(0.51);
                    add(0.22);
                    add(0.44);
                    add(0.32);
                    add(0.51);
                    add(0.25);
                    add(0.44);
                    add(0.32);
                    add(0.51);
                    add(0.55);
                    add(0.44);
                    add(0.29);
                    add(0.51);
                    add(0.78);
                    add(0.44);
                    add(0.27);
                    add(0.51);
                    add(0.95);
                    add(0.44);
                    add(0.22);
                    add(0.51);
                    add(0.35);
                    add(0.44);
                    add(0.33);
                    add(0.51);
                    add(0.65);
                    add(0.44);
                    add(0.78);
                    add(0.51);
                }};
                ArrayList<Integer> tiempo = new ArrayList<Integer>() {{
                    add(3);
                    add(5);
                    add(11);
                    add(13);
                    add(3);
                    add(5);
                    add(11);
                    add(13);
                    add(3);
                    add(5);
                    add(11);
                    add(13);
                    add(3);
                    add(5);
                    add(11);
                    add(13);
                    add(3);
                    add(5);
                    add(11);
                    add(13);
                    add(3);
                    add(5);
                    add(11);
                    add(13);
                    add(3);
                    add(5);
                    add(11);
                    add(13);
                    add(3);
                    add(5);
                    add(11);
                    add(13);
                }};

                // insertar en DB
                //insertar(String hora_inicio, String hora_fin, String t0, String amplitud, String tiempo)
                managerSnore.insertar(
                        Long.toString(helper.getTimestamp()),
                        Long.toString(helper.getTimestamp() + 18572),
                        helper.getStringFromDouble(t0),
                        helper.getStringFromDouble(amplitud),
                        helper.getStringFromInteger(tiempo)
                );
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //Toast.makeText(this, "Opcion de menu seleccionada: SETTINGS", Toast.LENGTH_SHORT).show();
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }

        if (id == R.id.help_screen) {
            Intent help = new Intent(this, OnBoardingActivity.class);
            startActivity(help);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {
        //Toast.makeText(this, "Click en el item " + position, Toast.LENGTH_SHORT).show();
        Snore item = datos.get(position);

        Intent intent = new Intent(this, DetalleCaptura.class);
        intent.putExtra(ITEM_AMPLITUDES, item.getAmplitud());
        intent.putExtra(ITEM_PERIODOS, item.getT0());

        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        managerSnore.cerrar();
    }
}
