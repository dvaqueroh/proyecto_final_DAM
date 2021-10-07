package com.example.pruebalista.ui.clasesResarvas;

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
import com.example.pruebalista.clases.Clase;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservaClaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class
ReservaClaseFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int claPen,bolCla;
    Clase claseRecibida;
    TextView tvAsig;
    TextView tvHora;
    TextView tvFecha;
    TextView tvUsuario;
    Button btReservar,btVolver;
    // consultas Volley
    ProgressDialog progressDialog;
    RequestQueue request;
    StringRequest stringRequest;
    VolleySingleton volleySingleton;
    //
    SharedPreferences datosSP;
    SharedPreferences.Editor editorSP;

    public ReservaClaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReservaClaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReservaClaseFragment newInstance(String param1, String param2) {
        ReservaClaseFragment fragment = new ReservaClaseFragment();
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
        View vista = inflater.inflate(R.layout.fragment_reserva_clase, container, false);
        tvAsig = vista.findViewById(R.id.tvResAsignatura);
        tvHora = vista.findViewById(R.id.tvResHora);
        tvFecha = vista.findViewById(R.id.tvResFecha);
        tvUsuario = vista.findViewById(R.id.tvResUsuario);
        btReservar = vista.findViewById(R.id.btReservar);
        btVolver = vista.findViewById(R.id.btResrvVolver);
        btReservar.setOnClickListener(this);
        btVolver.setOnClickListener(this);
        //
        tvAsig.setText(claseRecibida.getAsignatura());
        tvHora.setText(claseRecibida.getHora());
        tvFecha.setText(claseRecibida.getFecha());
        tvUsuario.setText(claseRecibida.getAlumno());
        return vista;
    }

    @Override
    public void onClick(View v) {
        NavController navController = Navigation.findNavController(v);
        if(v.getId() == R.id.btResrvVolver){
            System.out.println(" Volver atras");
            navController.navigate(R.id.action_reservaClaseFragment_to_nav_clases_volleyFragment);
        }

        if(v.getId() == R.id.btReservar){
            System.out.println(" boton reservar");
            cargarWebServiceReservaClases();

        }
    }// fin onclick

    private void cargarWebServiceReservaClases() {
        MenuNavegacionActivity menuNav = new MenuNavegacionActivity();
        datosSP = getContext().getSharedPreferences("datosAlumno", Context.MODE_PRIVATE);
        bolCla = datosSP.getInt("bolsaClases",-1);
        claPen = datosSP.getInt("clasesPendientes",-1);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Procesando Compra...");
        progressDialog.show();
        //  request POST para comprar clases
        String url = "http://10.0.2.2/WEBS/app_escuela/reservaClase.php";
        System.out.println("crea string Request");
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.trim().equalsIgnoreCase("reservada")){
                    System.out.println("Clase reservada");
                    Toast.makeText(getContext(), "Clase reservada", Toast.LENGTH_SHORT).show();
                    // actualiza bolsa clases y clases pendientes en
                    System.out.println("Actualiza SharedPreferences bolsa clases y clases pendientes");
                    editorSP = datosSP.edit();
                    editorSP.putInt("bolsaClases",bolCla-1);
                    editorSP.putInt("clasesPendientes",claPen+1);
                    editorSP.apply();
                    //alert
                    mostrarAlerteReserva();
                    // retorna al HOME
                    NavController navController = Navigation.findNavController(getView());
                    navController.navigate(R.id.action_reservaClaseFragment_to_nav_clases_volleyFragment);
                }
                else{
                    Toast.makeText(getContext(), "No se han reservado la clase", Toast.LENGTH_SHORT).show();
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
                //idClase, estadoClase, fechaClase, idAsignaturaAux, idHoraAux, idAlumAux
                parametros.put("estadoClase",Integer.toString(1));
                parametros.put("fechaClase",claseRecibida.getFecha());
                parametros.put("idAsignaturaAux",Integer.toString(claseRecibida.getIdAsignatura()));
                parametros.put("idHoraAux",Integer.toString(claseRecibida.getIdHora()));
                parametros.put("idAlumAux",Integer.toString(claseRecibida.getIdAlumno()));

                return parametros;
            }
        };

        //request.add(stringRequest);
        System.out.println("VolleySingleton string Request");
        volleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }// fin carga webService

    private void mostrarAlerteReserva() {
        AlertDialog.Builder alertaCompraClase = new AlertDialog.Builder(getContext());
        alertaCompraClase.setTitle("Clases Reservada");
        alertaCompraClase.setMessage("Se ha reservado la clase correctamente");
        alertaCompraClase.setPositiveButton("Aceptar", null);
        AlertDialog dialog = alertaCompraClase.create();
        dialog.show();
    }
}// fin reservaclasefragment