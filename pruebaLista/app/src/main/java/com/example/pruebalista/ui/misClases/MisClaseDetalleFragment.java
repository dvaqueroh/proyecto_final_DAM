package com.example.pruebalista.ui.misClases;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pruebalista.MenuNavegacionActivity;
import com.example.pruebalista.R;
import com.example.pruebalista.baseDatosVolley.VolleySingleton;
import com.example.pruebalista.clases.Asignatura;
import com.example.pruebalista.clases.Clase;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MisClaseDetalleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MisClaseDetalleFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView tvAsig,tvFecha,tvHora;
    Clase claseRecibida;
    Button btAnularClase,btAnularVolver;
    SharedPreferences datosSP;
    SharedPreferences.Editor editorSP;
    // consultas Volley
    ProgressDialog progressDialog;
    RequestQueue request;
    StringRequest stringRequest;
    VolleySingleton volleySingleton;

    public MisClaseDetalleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MisClaseDetalleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MisClaseDetalleFragment newInstance(String param1, String param2) {
        MisClaseDetalleFragment fragment = new MisClaseDetalleFragment();
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
            claseRecibida = (Clase) getArguments().getSerializable("clase");
            System.out.println("anular clases de: " + claseRecibida.getAsignatura());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_mis_clase_detalle, container, false);
        tvAsig = vista.findViewById(R.id.tvMisClaseDetAsig);
        tvFecha = vista.findViewById(R.id.tvMisClaseDetFecha);
        tvHora = vista.findViewById(R.id.tvMisClaseDetHora);
        btAnularClase = vista.findViewById(R.id.misClasBtCancelar);
        btAnularVolver = vista.findViewById(R.id.misClasbtVolver);
        btAnularClase.setOnClickListener(this);
        btAnularVolver.setOnClickListener(this);
        //
        tvAsig.setText(claseRecibida.getAsignatura());
        tvFecha.setText(claseRecibida.getFecha());
        tvHora.setText(claseRecibida.getHora());

        return vista;
    }

    @Override
    public void onClick(View v) {
        //System.out.println("Boton pulsado " + v.getId());
        NavController navController = Navigation.findNavController(v);
        switch (v.getId()){
            case R.id.misClasBtCancelar:
                System.out.println(" btn CANCELAR CLASES");
                cancelarClases();
                break;
            case R.id.misClasbtVolver:
                System.out.println("btn VOLVER ATRAS");
                navController.navigate(R.id.action_misClaseDetalleFragment_to_misClasesPendientesFragment);
                break;
        }
    }// fin on click

    private void cancelarClases() {
        MenuNavegacionActivity menuNav = new MenuNavegacionActivity();

        datosSP = getContext().getSharedPreferences("datosAlumno", Context.MODE_PRIVATE);

        int idAlum = datosSP.getInt("idAlum",-1);
        int idClase = claseRecibida.getIdClase();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Procesando Cancelacion...");
        progressDialog.show();
        //  request POST para comprar clases
        String url = "http://10.0.2.2/WEBS/app_escuela/misClasesModClase.php";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.trim().equalsIgnoreCase("cancelada")){
                    System.out.println("IdAlumno: "+idAlum+" id clase: "+idClase);
                    Toast.makeText(getContext(), "Clases cancelada:", Toast.LENGTH_SHORT).show();
                    //
                    //alert
                    mostrarAlertClaseCancelada();
                    // actualiza bolsa clases y clases pendientes en
                    System.out.println("Actualiza SharedPreferences bolsa clases y clases pendientes");
                    editorSP = datosSP.edit();
                    int bolCla = datosSP.getInt("bolsaClases",-1);
                    int claPen = datosSP.getInt("clasesPendientes",-1);
                    editorSP.putInt("bolsaClases",bolCla+1);
                    editorSP.putInt("clasesPendientes",claPen-1);
                    editorSP.apply();
                    // retorna al la lista de clases
                    NavController navController = Navigation.findNavController(getView());
                    navController.navigate(R.id.action_misClaseDetalleFragment_to_misClasesPendientesFragment);
                }
                else{
                    Toast.makeText(getContext(), "No se han cancelado las clases", Toast.LENGTH_SHORT).show();
                    Log.i("RESPUESTA: ",""+response);
                }
                progressDialog.hide();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
                System.out.println(" Error en respuesta - "+error.toString());
                Toast.makeText(getContext(), "Error en respuesta", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros=new HashMap<>();
                parametros.put("idAlum",Integer.toString(idAlum));
                parametros.put("idClase",Integer.toString(idClase));
                System.out.println(" Alumno : "+idAlum);
                System.out.println(" Clase : "+idClase);

                return parametros;
            }
        };

        //request.add(stringRequest);
        volleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }// fin cancelar clases

        private void mostrarAlertClaseCancelada() {

            AlertDialog.Builder alertaCcancelaClase = new AlertDialog.Builder(getContext());
            alertaCcancelaClase.setTitle("Clase Cancelada");
            alertaCcancelaClase.setMessage("Se ha cancelado la clase correctamente");
            alertaCcancelaClase.setPositiveButton("Aceptar", null);
            AlertDialog dialog = alertaCcancelaClase.create();
            dialog.show();
        }
}//fin fragment mis clases detalle