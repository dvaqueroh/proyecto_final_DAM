package com.example.pruebalista.ui.horasClases;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pruebalista.R;
import com.example.pruebalista.clases.Clase;

import java.util.ArrayList;

public class adaptadorClasesAsig extends RecyclerView.Adapter<adaptadorClasesAsig.ViewHolderClases> {
    HorarioDiaFragment HoDiFrag;
    ArrayList<Clase> listaHoras;
    Clase claseRecibida;
    ImageView iconoEstado;
    public adaptadorClasesAsig(ArrayList<Clase> listaHoras) {

        this.listaHoras = listaHoras;
    }

    @NonNull
    @Override
    public ViewHolderClases onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horario_dia,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        vista.setLayoutParams(layoutParams);
        return new ViewHolderClases(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClases holder, int position) {
        System.out.println("carga los items");
        String estado = "disponible";
        //holder.idAsig.setText(String.valueOf(listaHoras.get(position).getIdAsignatura()));
        holder.horaClase.setText(listaHoras.get(position).getHora());
        // si una hora tiene estado = 1 se marca como reservada
        if(listaHoras.get(position).getEstado()==1){
            estado = "reservada";
            holder.estadoClase.setText(estado);
            holder.estadoClase.setTextColor(Color.RED);
            holder.iconoEstado.setImageResource(R.drawable.ic_close_circle);
        }
        else {
            holder.estadoClase.setText(estado);
            holder.estadoClase.setTextColor(Color.BLUE);
            holder.iconoEstado.setImageResource(R.drawable.check_circle);
        }

        // evento click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HoDiFrag = new HorarioDiaFragment();
                NavController navController = Navigation.findNavController(v);

                Clase clase = listaHoras.get(position);
                System.out.println("Click en item "+clase.getIdHora());
                if(clase.getEstado()==1){
                    System.out.println("clase reservada no se puede entrar");
                }
                else {
                    System.out.println("get CLASE RECIBIDA");
                    //claseRecibida = new Clase();
                    //claseRecibida = HoDiFrag.getClaseRecibida();
                    System.out.println("CLASE RECIBIDA idAlum: - "+claseRecibida.getIdAlumno());
                    System.out.println("graba datos en claseRecibida");

                    claseRecibida.setIdHora(clase.getIdHora());
                    claseRecibida.setHora(clase.getHora());
                    // falta pasar los parametros de la clase
                    Bundle datosAEnviar = new Bundle();
                    // pasamos la asignatura seleccionada
                    datosAEnviar.putSerializable("clase",claseRecibida);
                    //
                    System.out.println("*******************************");
                    System.out.println("* objeto clase datos a enviar *");
                    System.out.println("* id Alumno : "+claseRecibida.getIdAlumno()+" *");
                    System.out.println("* id Asignatura - "+claseRecibida.getIdAsignatura()+" *");
                    System.out.println("* Asignatura - "+claseRecibida.getAsignatura()+" *");
                    System.out.println("* id Hora - "+claseRecibida.getIdHora()+" *");
                    System.out.println("* Hora - "+claseRecibida.getHora()+" *");
                    System.out.println("* fecha - "+claseRecibida.getFecha()+" *");
                    System.out.println("*******************************");
                    // Preparar el fragmento
                    navController.navigate(R.id.action_nav_horarioDiaFragment_to_reservaClaseFragment,datosAEnviar);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        System.out.println("ADAPTADOR tamaño lista "+listaHoras.size());
        return listaHoras.size();
    }

    public class ViewHolderClases extends RecyclerView.ViewHolder {
        TextView idAsig,horaClase,estadoClase;
        ImageView iconoEstado;
        public ViewHolderClases(@NonNull View itemView) {
            super(itemView);
            //idAsig = itemView.findViewById(R.id.idAsig);
            horaClase = itemView.findViewById(R.id.horaClase);
            estadoClase = itemView.findViewById(R.id.estadoClase);
            iconoEstado = itemView.findViewById(R.id.icEstado);
        }
    }
    public void enviarDatos(Clase clase) {

        claseRecibida = clase;
    }
}// fin clase adaptadorAsignaturas

