package edu.fich.pfcii.snoredetection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import com.gc.materialdesign.views.ButtonFlat;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class OnBoardingActivity extends FragmentActivity {

    private ViewPager pager;
    private SmartTabLayout indicator;
    private ButtonFlat skip;
    private ButtonFlat next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding);

        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (SmartTabLayout) findViewById(R.id.indicator);
        skip = (ButtonFlat) findViewById(R.id.skip);
        next = (ButtonFlat) findViewById(R.id.next);

        // Crear un adaptador (Adapter) para poder desplegar las Pantallas creadas
        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0: return new OnBoardingFragment1();
                    case 1: return new OnBoardingFragment2();
                    case 2: return new OnBoardingFragment3();
                    default: return null;
                }
            }

            @Override
            public int getCount() {
                // Aqui se retorna la catidad de pantallas a mostrar. IMPORTANTE!
                return 3;
            }
        };

        // Setear el adaptaor para el ViewPager
        pager.setAdapter(adapter);

        // Apuntar el Smart Tab Layout con el ViewPager
        indicator.setViewPager(pager);

        // Setear el boton SKIP
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishOnboarding();
            }
        });

        // Setear el boton NEXT
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem() == 2) {
                    // Esta es la ultima pantalla
                    finishOnboarding();
                } else {
                    pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                }
            }
        });

        /**
         * Cambios dinamicos al maquetado del Onboarding Screen:
         *      Quitar el boton SKIP
         *      Cambiar etiqueta de NEXT por DONE
         *      Esto sucede al llegar a la ultima pantalla
         */
        indicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    skip.setVisibility(View.GONE);
                    next.setText("Done");
                } else {
                    skip.setVisibility(View.VISIBLE);
                    next.setText("Next");
                }
            }
        });

    }

    /**
     * Metodo finishOnboarding()
     */
    private void finishOnboarding() {
        // Obtener las Shared Preferences
        SharedPreferences preferences = getSharedPreferences("mis_preferencias", MODE_PRIVATE);

        // Setear onboarding_complete a TRUE
        preferences.edit().putBoolean("onboarding_complete", true).apply();

        // Lanzar la actividad Principal (o la pantalla que sigue)
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);

        // Terminar la actividad Onboarding
        finish();
    }
}
