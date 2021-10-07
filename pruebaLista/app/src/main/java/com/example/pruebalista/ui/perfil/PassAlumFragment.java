package com.example.pruebalista.ui.perfil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pruebalista.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassAlumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassAlumFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView tvPassActual;
    Button btMod,btVolver;
    SharedPreferences datosSP;

    public PassAlumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PassAlumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PassAlumFragment newInstance(String param1, String param2) {
        PassAlumFragment fragment = new PassAlumFragment();
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
        View vista = inflater.inflate(R.layout.fragment_pass_alum, container, false);
        tvPassActual = vista.findViewById(R.id.tvPassActual);

        btMod = vista.findViewById(R.id.btPassMod);
        btVolver = vista.findViewById(R.id.btPassVolver);
        btMod.setOnClickListener(this);
        btVolver.setOnClickListener(this);

        // carga los datos del alumno desde el share preferences
        datosSP = getContext().getSharedPreferences("datosAlumno", Context.MODE_PRIVATE);

        String passAct = datosSP.getString("pass","");

        tvPassActual.setText(passAct);

        return vista;
    }

    @Override
    public void onClick(View v) {
        NavController navController;
        switch (v.getId()){
            case R.id.btPassMod:
                System.out.println("BOTON MODIFICAR PASS ");
                // va a Modificar datos de pagos
                navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_passAlumFragment_to_passModFragment);

                break;
            case R.id.btPassVolver:
                // retorna al PERFIL
                navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_passAlumFragment_to_nav_perfilFragment);
                break;
        }
    }
}