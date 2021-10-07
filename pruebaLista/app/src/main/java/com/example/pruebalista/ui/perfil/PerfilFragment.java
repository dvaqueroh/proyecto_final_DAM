package com.example.pruebalista.ui.perfil;

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
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btDatos,btPagos,btPass;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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
        View vista = inflater.inflate(R.layout.fragment_perfil, container, false);
        btDatos = vista.findViewById(R.id.btPerDatos);
        btPagos = vista.findViewById(R.id.btPerPagos);
        btPass= vista.findViewById(R.id.btPerPass);

        btDatos.setOnClickListener(this);
        btPagos.setOnClickListener(this);
        btPass.setOnClickListener(this);
        return vista;
    }

    @Override
    public void onClick(View v) {
        NavController navController;
        //System.out.println("BOTON PULSADO ");

        switch (v.getId()){
            case R.id.btPerDatos:
                //System.out.println("BOTON DATOS ");
                navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_nav_perfilFragment_to_datosAlumFragment);
                break;
            case R.id.btPerPagos:
                System.out.println("BOTON PAGOS ");
                navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_nav_perfilFragment_to_pagosFragment);
                break;
            case R.id.btPerPass:
                //System.out.println("BOTON PASS ");
                navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_nav_perfilFragment_to_passAlumFragment);
                break;
        }
    }//
} // fin perfil Fragment