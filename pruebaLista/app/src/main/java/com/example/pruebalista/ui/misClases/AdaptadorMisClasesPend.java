package com.example.pruebalista.ui.misClases;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pruebalista.R;
import com.example.pruebalista.clases.Asignatura;
import com.example.pruebalista.clases.Clase;
import com.example.pruebalista.dummy.DummyContent.DummyItem;
import com.example.pruebalista.ui.asignaturas.AsignaturaDetalleFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AdaptadorMisClasesPend extends RecyclerView.Adapter<AdaptadorMisClasesPend.ViewHolder> {
    ArrayList<Clase> listaClases;
    AsignaturaDetalleFragment asigDetFrag;
    Clase clase;
    ImageView iconoAsignatura;
    //private final List<DummyItem> mValues;

    public AdaptadorMisClasesPend(ArrayList<Clase> listaClases) {

        this.listaClases = listaClases;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mis_clases_pendientes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvNombreAsig.setText(listaClases.get(position).getAsignatura());
        holder.tvFechaClase.setText(listaClases.get(position).getFecha());
        holder.tvHoraClase.setText(listaClases.get(position).getHora());
        // evento click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clase clase = listaClases.get(position);
                System.out.println("abrir la clase seleccionada: "+clase.getAsignatura());
                System.out.println("ID clase seleccionada"+clase.getIdClase());

                //Crear bundle, que son los datos que pasaremos
                Bundle datosAEnviar = new Bundle();
                // pasamos la clase seleccionada
                datosAEnviar.putSerializable("clase",clase);
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_misClasesPendientesFragment_to_misClaseDetalleFragment,datosAEnviar);
            }
        });
    }

    @Override
    public int getItemCount() {
        System.out.println("ADAPTADOR tamaño lista MIS CLASES PENDIENTES "+listaClases.size());
        return listaClases.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreAsig, tvHoraClase, tvFechaClase;
        ImageView iconoAsig;

        public ViewHolder(View view) {
            super(view);
            tvNombreAsig = (TextView) view.findViewById(R.id.misClasesAsig);
            tvFechaClase = (TextView) view.findViewById(R.id.misClasesFecha);
            tvHoraClase = (TextView) view.findViewById(R.id.misClasesHora);
        }
    }
}