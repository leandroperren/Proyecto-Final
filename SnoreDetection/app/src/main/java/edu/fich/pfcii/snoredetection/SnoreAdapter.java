package edu.fich.pfcii.snoredetection;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import edu.fich.pfcii.snoredetection.helper.Helper;
import edu.fich.pfcii.snoredetection.model.Snore;

public class SnoreAdapter extends RecyclerView.Adapter<SnoreAdapter.SnoreViewHolder> {

    private static Helper helper = new Helper();
    private ArrayList<Snore> datos;

    private LayoutInflater inflater;

    private static ItemClickCallback itemClickCallback;

    public interface ItemClickCallback {
        void onItemClick(int position);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public SnoreAdapter(ArrayList<Snore> datos, Context c) {
        this.datos = datos;
        this.inflater = LayoutInflater.from(c);
    }

    @Override
    public SnoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_fila, parent, false);
        SnoreViewHolder svh = new SnoreViewHolder(v, parent.getContext());
        return svh;
    }

    @Override
    public void onBindViewHolder(SnoreViewHolder holder, int position) {
        Snore snore = datos.get(position);
        holder.bindSnore(snore);
    }

    @Override
    public int getItemCount() {
        return (null != datos ? datos.size() : 0);
    }

    public static class SnoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout contenido_cantidad;
        private TextView cantidad, title_fecha_hora, tiempo_total;

        private final int sdk = Build.VERSION.SDK_INT;
        private Context ctx;
        private View container;

        public SnoreViewHolder(View itemView, Context ctx) {
            super(itemView);

            this.ctx = ctx;

            contenido_cantidad = (LinearLayout) itemView.findViewById(R.id.contenido_cantidad);
            cantidad           = (TextView) itemView.findViewById(R.id.cantidad);
            title_fecha_hora   = (TextView) itemView.findViewById(R.id.title_fecha_hora);
            tiempo_total       = (TextView) itemView.findViewById(R.id.tiempo_total);
            container          = (View) itemView.findViewById(R.id.container);

            container.setOnClickListener(this);
        }

        // Función para bidear todos los datos del objeto con el patrón ViewHolder
        // para la funcion onBindViewHolder
        public void bindSnore(Snore s) {
            int snore_count = s.getSnoreCount();

            // Por defecto se asigna color verde al circulo
            Drawable fondo = ContextCompat.getDrawable(ctx, R.drawable.circle_green);

            // Set background color for status bar
            if (snore_count > 5 && snore_count <= 28) {
                fondo = ContextCompat.getDrawable(ctx, R.drawable.circle_yellow);
            } else if (snore_count > 28) {
                fondo = ContextCompat.getDrawable(ctx, R.drawable.circle_red);
            }

            // Asignar fondo segun version de Android
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                contenido_cantidad.setBackgroundDrawable(fondo);
            } else {
                contenido_cantidad.setBackground(fondo);
            }

            // Setear todos los campos de texto
            cantidad.setText(Integer.toString(snore_count));
            title_fecha_hora.setText(helper.getDateAndTime(s.getHora_inicio()));
            tiempo_total.setText(helper.getTotalTime(s.getHora_inicio(), s.getHora_fin()));
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.container) {
                itemClickCallback.onItemClick(getAdapterPosition());
            }
        }
    }

}
