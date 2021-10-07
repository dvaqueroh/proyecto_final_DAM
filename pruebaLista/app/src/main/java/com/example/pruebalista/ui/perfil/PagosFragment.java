package com.example.pruebalista.ui.perfil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pruebalista.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PagosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PagosFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView tvDatosPago,tvPagosConfirm;
    Button btMod,btVolver;
    SharedPreferences datosSP;
    Spinner spinnerPagos;

    public PagosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PagosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PagosFragment newInstance(String param1, String param2) {
        PagosFragment fragment = new PagosFragment();
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
        View vista = inflater.inflate(R.layout.fragment_pagos, container, false);

        tvDatosPago = vista.findViewById(R.id.tvPagoDatos);
        tvPagosConfirm = vista.findViewById(R.id.tvPagoConf);

        btMod = vista.findViewById(R.id.btPagoMod);
        btVolver = vista.findViewById(R.id.btPagoVolver);
        btMod.setOnClickListener(this);
        btVolver.setOnClickListener(this);

        //
        spinnerPagos = vista.findViewById(R.id.spinnerPagos);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.formasPago, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerPagos.setAdapter(adapter);
        spinnerPagos.setEnabled(false);
        spinnerPagos.setClickable(false);


        // carga los datos del alumno desde el share preferences
        datosSP = getContext().getSharedPreferences("datosAlumno", Context.MODE_PRIVATE);

        int formaPag = datosSP.getInt("idFormaPagoAux",0);
        String datosPag = datosSP.getString("datosFormaPago","");
        int pagoConf = datosSP.getInt("formaPagoConfirm",-1);

        //
        tvDatosPago.setText(datosPag);
        tvPagosConfirm.setText(Integer.toString(pagoConf));
        System.out.println("id forma pago "+formaPag);
        System.out.println("carga posicion del spinner");

        spinnerPagos.setSelection(formaPag);


        return vista;
    }


    @Override
    public void onClick(View v) {
        NavController navController;
        switch (v.getId()){
            case R.id.btPagoMod:
                System.out.println("BOTON MODIFICAR PAGOS ");
                // va a Modificar datos de pagos
                navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_pagosFragment_to_pagosModFragment);

            break;
            case R.id.btPagoVolver:
                // retorna al PERFIL
                navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_pagosFragment_to_nav_perfilFragment);
            break;
        }
    }

}// fin PAGOS FRAGMENT