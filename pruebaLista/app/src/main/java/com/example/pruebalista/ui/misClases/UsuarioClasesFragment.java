package com.example.pruebalista.ui.misClases;

import android.app.AlertDialog;
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
 * Use the {@link UsuarioClasesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsuarioClasesFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button btBolsaClases,btClasesPen;
    TextView tvBolscacl,tvClasesPen;
    int bolCla,claPen;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SharedPreferences datosSP;


    public UsuarioClasesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsuarioClasesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UsuarioClasesFragment newInstance(String param1, String param2) {
        UsuarioClasesFragment fragment = new UsuarioClasesFragment();
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
        View vista = inflater.inflate(R.layout.fragment_usuario_clases, container, false);
        tvBolscacl = vista.findViewById(R.id.tvUsBolCla);
        tvClasesPen = vista.findViewById(R.id.tvUsClaPen);
        btBolsaClases = vista.findViewById(R.id.btUsBolClas);
        btClasesPen = vista.findViewById(R.id.btUsClPen);

        btClasesPen.setOnClickListener(this);
        btBolsaClases.setOnClickListener(this);

        //

        datosSP = getContext().getSharedPreferences("datosAlumno", Context.MODE_PRIVATE);
        bolCla = datosSP.getInt("bolsaClases",-1);
        claPen = datosSP.getInt("clasesPendientes",-1);
        //
        System.out.println(" bolsa clases: "+bolCla+" clases pendientes: "+claPen);
        //tvBolscacl.setText(bolCla);
        tvBolscacl.setText( String.valueOf(bolCla));
        //tvClasesPen.setText(claPen);
        tvClasesPen.setText( String.valueOf(claPen));

        return vista;
    }

    @Override
    public void onClick(View v) {
        NavController navController = Navigation.findNavController(v);
        if(v.getId() == R.id.btUsBolClas){
            System.out.println(" boton BOLSA DE CLASES");
            mostrarBolsaClases();

        }

        if(v.getId() == R.id.btUsClPen){
            System.out.println(" boton CLASES PENDIENTES");
            //navController.navigate(R.id.action_nav_usuarioClasesFragment_to_misClasesCalendarioFragment);
            navController.navigate(R.id.action_nav_usuarioClasesFragment_to_misClasesPendientesFragment);
        }

    }// fin metodo onclick

    private void mostrarBolsaClases() {
        AlertDialog.Builder alertaCompraClase = new AlertDialog.Builder(getContext());
        alertaCompraClase.setTitle("Bolsa de Clases");
        alertaCompraClase.setMessage("Tiene un total de "+bolCla + " clases compradas.");
        alertaCompraClase.setPositiveButton("Aceptar", null);
        AlertDialog dialog = alertaCompraClase.create();
        dialog.show();
    }//
}// fin fragmento