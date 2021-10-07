package com.example.pruebalista.ui.misClases;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.pruebalista.R;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link misClasesCalendarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class misClasesCalendarioFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //
    CalendarView calendarioMisClases;
    public misClasesCalendarioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment misClasesCalendarioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static misClasesCalendarioFragment newInstance(String param1, String param2) {
        misClasesCalendarioFragment fragment = new misClasesCalendarioFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_mis_clases_calendario, container, false);

        calendarioMisClases = vista.findViewById(R.id.calendarMisClases);

        calendarioMisClases.setFirstDayOfWeek(2); // la semana empieza el lunes;
        calendarioMisClases.setShowWeekNumber(true); // muestra numero de semanas
        long fechaHoy = calendarioMisClases.getDate();
        calendarioMisClases.setMinDate(fechaHoy); // el calendario solo marca a partir de hoy
        System.out.println(" Fecha HOY - " + fechaHoy);

        //




        try {
            marcarFechas();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*
        calendarioMisClases.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                System.out.println(" Fecha pinchada ");
            }
        });
        */
        return vista;
    }

    private void marcarFechas() throws ParseException {
        /*
        DateFormat df = new SimpleDateFormat("dd/MM/yyy");
        Long dateL = df.parse("28/05/2021").getTime();
        calendarioMisClases.setDate(dateL);
        System.out.println(dateL);
        */
    }// fin marcar fechas

}// fin fragment mis clases CALENDARIO