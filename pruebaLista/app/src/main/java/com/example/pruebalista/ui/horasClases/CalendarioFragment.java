package com.example.pruebalista.ui.horasClases;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.pruebalista.R;
import com.example.pruebalista.clases.Clase;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarioFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btVolver;
    CalendarView calendario;
    Clase claseRecibida;

    public CalendarioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarioFragment newInstance(String param1, String param2) {
        CalendarioFragment fragment = new CalendarioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            //recibe como argumentos un objeto CLASE donde se iran rellenando datos
            claseRecibida = (Clase) getArguments().getSerializable("clase");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_calendario, container, false);
        btVolver = vista.findViewById(R.id.btCalVolver);
        btVolver.setOnClickListener(this);
        calendario = vista.findViewById(R.id.calendarView);

        calendario.setFirstDayOfWeek(2); // la semana empieza el lunes;
        calendario.setShowWeekNumber(true); // muestra numero de semanas

        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                NavController navController = Navigation.findNavController(view);
                int mes = 0+month+1;
                Format formatter = new SimpleDateFormat("dd/MM/yyyy");

                String fechaHoy = formatter.format(calendario.getDate());
                String fecha = dayOfMonth+"/"+ mes+"/"+year ;


                System.out.println(" Fecha HOY - " + fechaHoy);
                System.out.println(" Fecha marcada - " + fecha);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                try {
                    Date DateHoy = sdf.parse(fechaHoy);
                    Date DateSelec = sdf.parse(fecha);
                    // comprueba que el dia seleccionado no es anterior a la fecha de HOY
                    if (DateSelec.before(DateHoy)) {
                        System.out.println(" Fecha Anterior a "+fechaHoy);
                        Toast.makeText(getContext(), "Dia Pasado", Toast.LENGTH_LONG).show();

                    }
                    else{
                        System.out.println(" Fecha posterior - Abrir lista de horas");
                        Toast.makeText(getContext(), "Abre lista de horas", Toast.LENGTH_LONG).show();
                        // falta pasar los parametros de la clase
                        String fechaF = year+"/"+mes+"/"+dayOfMonth;
                        claseRecibida.setFecha(fechaF);
                        //claseRecibida.setIdAlumno(sharedPref.getInt("idAlum",-1));
                        Bundle datosAEnviar = new Bundle();
                        // pasamos la asignatura seleccionada
                        datosAEnviar.putSerializable("clase",claseRecibida);
                        // Preparar el fragmento
                        System.out.println("*******************************");
                        System.out.println("* objeto clase datos a enviar *");
                        System.out.println("* id Alumno : "+claseRecibida.getIdAlumno()+" *");
                        System.out.println("* id Asignatura - "+claseRecibida.getIdAsignatura()+" *");
                        System.out.println("* Asignatura - "+claseRecibida.getAsignatura()+" *");
                        System.out.println("* id Hora - "+" *");
                        System.out.println("* Hora - "+" *");
                        System.out.println("* fecha - "+claseRecibida.getFecha()+" *");
                        System.out.println("*******************************");
                        navController.navigate(R.id.action_calendarioFragment_to_horarioDiaFragment,datosAEnviar);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getContext(), fecha, Toast.LENGTH_LONG).show();
            }

        });;
        return vista;
    }

    @Override
    public void onClick(View v) {
        NavController navController = Navigation.findNavController(v);
        if(v.getId() == R.id.btCalVolver){
            System.out.println(" Volver atras");
            navController.navigate(R.id.action_calendarioFragment_to_nav_clases_volleyFragment);
        }
    }

    // GET SET

    public Clase getClaseRecibida() {
        return claseRecibida;
    }
} // FIN CALENDARIO