package com.example.pruebalista.ui.asignaturas;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pruebalista.R;
import com.example.pruebalista.clases.Asignatura;

import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;

public class adaptadorAsignaturas extends RecyclerView.Adapter<adaptadorAsignaturas.ViewHolderAsignaturas> {
    ArrayList<Asignatura> listaAsignaturas;
    AsignaturaDetalleFragment asigDetFrag;
    public adaptadorAsignaturas(ArrayList<Asignatura> listaAsignaturas) {

        this.listaAsignaturas = listaAsignaturas;
    }

    @NonNull
    @Override
    public ViewHolderAsignaturas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_asignaturas,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new ViewHolderAsignaturas(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAsignaturas holder, int position) {
        System.out.println("carga los items");
        holder.idAsig.setText(String.valueOf(listaAsignaturas.get(position).getIdAsignatura()));
        holder.nombreAsig.setText(listaAsignaturas.get(position).getNombre());
        holder.infoAsig.setText(listaAsignaturas.get(position).getInfo());

        // si la asignatura tiene foto la carga, si no, carga una imagen por defecto
        if(listaAsignaturas.get(position).getImagen() != null) {
            holder.idFotoAsig.setImageBitmap(listaAsignaturas.get(position).getImagen());
        }else{
            holder.idFotoAsig.setImageResource(R.drawable.ic_menu_camera);
        }

        // evento click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);

                Asignatura asignatura = listaAsignaturas.get(position);
                System.out.println("Click en item "+asignatura.getIdAsignatura()+" - "+asignatura.getNombre());
                //
                //Crear bundle, que son los datos que pasaremos
                Bundle datosAEnviar = new Bundle();
                // pasamos la asignatura seleccionada
                datosAEnviar.putSerializable("asignatura",asignatura);
                System.out.println("ID a pasar "+asignatura.getIdAsignatura());
                System.out.println("NOMBRE a pasar "+asignatura.getNombre());
                // Preparar el fragmento
                asigDetFrag = new AsignaturaDetalleFragment();
                // ¡Importante! darle argumentos
                asigDetFrag.setArguments(datosAEnviar);
                //
                // cargamos el fragment nuevo y los datos que pasamos
                //navController.navigate(R.id.asignaturaDetalleFragment,datosAEnviar);
                navController.navigate(R.id.action_asigFragmentVolley_to_asignaturaDetalleFragment,datosAEnviar);
                //

            }
        });
    }

    @Override
    public int getItemCount() {
        //System.out.println("ADAPTADOR tamaño lista "+listaAsignaturas.size());
        return listaAsignaturas.size();
    }

    public class ViewHolderAsignaturas extends RecyclerView.ViewHolder {
        TextView idAsig,nombreAsig,infoAsig;
        ImageView idFotoAsig;
        public ViewHolderAsignaturas(@NonNull View itemView) {
            super(itemView);
            idAsig=itemView.findViewById(R.id.idAsig);
            nombreAsig=itemView.findViewById(R.id.nombreAsig);
            infoAsig=itemView.findViewById(R.id.infoAsig);
            idFotoAsig=itemView.findViewById(R.id.IdImgAsig);
        }
    }
}// fin clase adaptadorAsignaturas

