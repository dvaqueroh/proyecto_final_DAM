package com.example.pruebalista.ui.clasesResarvas;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.example.pruebalista.MenuNavegacionActivity;
import com.example.pruebalista.R;
import com.example.pruebalista.clases.Asignatura;
import com.example.pruebalista.clases.Clase;
import com.example.pruebalista.ui.asignaturas.AsignaturaDetalleFragment;

import java.util.ArrayList;

public class adaptadorHoras extends RecyclerView.Adapter<adaptadorHoras.ViewHolderAsignaturas> {
    private final Context Context;
    ArrayList<Asignatura> listaAsignaturas;
    AsignaturaDetalleFragment asigDetFrag;
    Clase clase;
    SharedPreferences datosSP;
    public adaptadorHoras(Context context, ArrayList<Asignatura> listaAsignaturas) {
        this.Context = context;
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
                MenuNavegacionActivity menNavAct = new MenuNavegacionActivity();
                Asignatura asignatura = listaAsignaturas.get(position);
                System.out.println("Click en item "+asignatura.getIdAsignatura()+" - "+asignatura.getNombre());
                //
                //Crear bundle, que son los datos que pasaremos
                Bundle datosAEnviar = new Bundle();
                // pasamos objeto clase con los datos de la asignatura
                /* FALTA EL ID DEL ALUMNO cuando inice sesion*/
                clase = new Clase();

                //datosSP = menNavAct.getSharedPreferences("datosAlumno",Context.MODE_PRIVATE);
                datosSP = Context.getSharedPreferences("datosAlumno",Context.MODE_PRIVATE);
                int idAlum = datosSP.getInt("idAlum",-1);
                clase.setIdAlumno(idAlum);
                clase.setIdAsignatura(asignatura.getIdAsignatura());
                clase.setAsignatura(asignatura.getNombre());
                datosAEnviar.putSerializable("clase",clase);
                System.out.println("*******************************");
                System.out.println("* objeto clase datos a enviar *");
                System.out.println("* id Alumno : "+clase.getIdAlumno()+" *");
                System.out.println("* id Asignatura - "+clase.getIdAsignatura()+" *");
                System.out.println("* Asignatura - "+clase.getAsignatura()+" *");
                System.out.println("* id Hora - "+" *");
                System.out.println("* Hora - "+" *");
                System.out.println("* fecha - "+" *");
                System.out.println("*******************************");
                navController.navigate(R.id.action_nav_clases_volleyFragment_to_calendarioFragment,datosAEnviar);
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

