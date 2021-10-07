package com.example.pruebalista.ui.perfil;

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
import android.widget.EditText;
import android.widget.Spinner;
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
import com.example.pruebalista.clases.Alumno;
import com.example.pruebalista.clases.Clase;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DatosModFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatosModFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText etUsuario,etNombre,etDni,etNacimiento,etTlf,etEmail;
    Button btMod,btVolver;
    int idAlum,telefono;
    Alumno alumnoSesion;
    String usuario,nombre,dni,nacimiento,email;
    SharedPreferences datosSP;
    SharedPreferences.Editor editorSP;
    // consultas Volley
    ProgressDialog progressDialog;
    RequestQueue request;
    StringRequest stringRequest;
    VolleySingleton volleySingleton;

    public DatosModFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DatosModFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DatosModFragment newInstance(String param1, String param2) {
        DatosModFragment fragment = new DatosModFragment();
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
            alumnoSesion = (Alumno) getArguments().getSerializable("alumno");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_datos_mod, container, false);
        //
        etUsuario = vista.findViewById(R.id.etModDatosUsuario);
        etNombre = vista.findViewById(R.id.etModDatosNombre);
        etDni = vista.findViewById(R.id.etModDatosDni);
        etNacimiento= vista.findViewById(R.id.etModDatosNacimiento);
        etTlf = vista.findViewById(R.id.etModDatosTelefono);
        etEmail = vista.findViewById(R.id.etModDatosEmail);


        btMod = vista.findViewById(R.id.btModDatosModificar);
        btVolver = vista.findViewById(R.id.btModDatosVolver);
        btMod.setOnClickListener(this);
        btVolver.setOnClickListener(this);
        //
        datosSP = getContext().getSharedPreferences("datosAlumno", Context.MODE_PRIVATE);
        /*
        System.out.println("usuario "+usuario);
        usuario = datosSP.getString("usuario","");
        nombre = datosSP.getString("nombreAlum","");
        dni = datosSP.getString("dniAlum","");
        nacimiento = datosSP.getString("nacimientoAlum","");
        telefono = datosSP.getInt("tlfAlum",-1);
        email = datosSP.getString("emailAlum","");
        */
        //
        etUsuario.setText(alumnoSesion.getUsuarioAlumno());
        etNombre.setText(alumnoSesion.getNombreAlumno());
        etDni.setText(alumnoSesion.getDniAlumno());
        etNacimiento.setText(alumnoSesion.getFechaNacAlumno());
        etTlf.setText(String.valueOf(alumnoSesion.getTlfAlumno()));
        etEmail.setText(alumnoSesion.getEmailAlumno());

        return vista;
    }

    @Override
    public void onClick(View v) {
        NavController navController;
        switch (v.getId()){
            case R.id.btModDatosModificar:
                System.out.println("BOTON MODIFICAR DATOS ALUMNO ");
                modificarDatosAlumno();

                break;
            case R.id.btModDatosVolver:
                // retorna al PERFIL
                navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_datosModFragment_to_datosAlumFragment);
                break;
        }
    }// fin on click

    private void modificarDatosAlumno() {

        MenuNavegacionActivity menuNav = new MenuNavegacionActivity();

        datosSP = getContext().getSharedPreferences("datosAlumno", Context.MODE_PRIVATE);

        int idAlum = datosSP.getInt("idAlum",-1);
        int tlf = Integer.valueOf(etTlf.getText().toString());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Procesando Modificacion...");
        progressDialog.show();
        //  request POST para comprar clases
        String url = "http://10.0.2.2/WEBS/app_escuela/alumnoModDatos.php";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.trim().equalsIgnoreCase("modificado")){
                    Toast.makeText(getContext(), "Datos modificados:", Toast.LENGTH_SHORT).show();
                    System.out.println(" MODIFICADO ");
                    //actualiza los datos del shared
                    editorSP = datosSP.edit();
                    editorSP.putString("usuario",etUsuario.getText().toString());
                    editorSP.putString("nombreAlum",etNombre.getText().toString());
                    editorSP.putString("dniAlum",etDni.getText().toString());
                    editorSP.putString("nacimientoAlum",etNacimiento.getText().toString());
                    editorSP.putInt("tlfAlum",tlf);
                    editorSP.putString("emailAlum",etEmail.getText().toString());
                    editorSP.apply();
                    //
                    mostrarAlertDatosMod();
                    // retorna datos pago
                    NavController navController = Navigation.findNavController(getView());
                    navController.navigate(R.id.action_datosModFragment_to_datosAlumFragment);
                }
                else{
                    Toast.makeText(getContext(), "No se han modificado los datos", Toast.LENGTH_SHORT).show();
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
                parametros.put("usuarioAlum",etUsuario.getText().toString());
                parametros.put("nombreAlum",etNombre.getText().toString());
                parametros.put("dniAlum",etDni.getText().toString());
                parametros.put("nacimientoAlum",etNacimiento.getText().toString());
                parametros.put("tlfAlum",etTlf.getText().toString());
                parametros.put("emailAlum",etEmail.getText().toString());
                //
                System.out.println(" Alum - "+idAlum);
                System.out.println(" Usuario - "+etUsuario);
                System.out.println(" nombre - "+etNombre);
                System.out.println(" dni - "+etDni);
                System.out.println(" nacimiento - "+etNacimiento);
                System.out.println(" tlf - "+etTlf);
                System.out.println(" email - "+etEmail);

                return parametros;
            }
        };

        //request.add(stringRequest);
        volleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);


    }// fin modificar datos alumno

    private void mostrarAlertDatosMod() {
        AlertDialog.Builder alertaCompraClase = new AlertDialog.Builder(getContext());
        alertaCompraClase.setTitle("Datos Modificados");
        alertaCompraClase.setMessage("Se han modificado los datos correctamente");
        alertaCompraClase.setPositiveButton("Aceptar", null);
        AlertDialog dialog = alertaCompraClase.create();
        dialog.show();
    }
}