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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassModFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassModFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int idTipoFormaPago;
    private String passAct,passN1,passN2;

    EditText etPass,etPassN1,etPassN2;
    Button btMod,btVolver;
    SharedPreferences datosSP;
    SharedPreferences.Editor editorSP;
    // consultas Volley
    ProgressDialog progressDialog;
    RequestQueue request;
    StringRequest stringRequest;
    VolleySingleton volleySingleton;

    public PassModFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PassModFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PassModFragment newInstance(String param1, String param2) {
        PassModFragment fragment = new PassModFragment();
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
        View vista = inflater.inflate(R.layout.fragment_pass_mod, container, false);

        etPass = (EditText)vista.findViewById(R.id.etModPassAct);
        etPassN1 = (EditText)vista.findViewById(R.id.etModPassNuevo1);
        etPassN2 = (EditText)vista.findViewById(R.id.etModPassNuevo2);

        btMod = vista.findViewById(R.id.btModPassModificar);
        btVolver = vista.findViewById(R.id.btModPassVolver);

        btMod.setOnClickListener(this);
        btVolver.setOnClickListener(this);
        /*
        datosSP = getContext().getSharedPreferences("datosAlumno", Context.MODE_PRIVATE);
        passAct = datosSP.getString("pass","");
        etPass.setText(passAct);
         */
        return vista;
    }

    @Override
    public void onClick(View v) {
        NavController navController;
        switch (v.getId()){
            case R.id.btModPassModificar:
                System.out.println("BOTON MODIFICAR PASS ");
                modificarPass();

                break;
            case R.id.btModPassVolver:
                // retorna al PERFIL
                navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_passModFragment_to_passAlumFragment);
                break;
        }
    }

    private void modificarPass() {
        MenuNavegacionActivity menuNav = new MenuNavegacionActivity();

        datosSP = getContext().getSharedPreferences("datosAlumno", Context.MODE_PRIVATE);

        int idAlum = datosSP.getInt("idAlum",-1);
        passAct = etPass.getText().toString();
        passN1 = etPassN1.getText().toString();
        passN2 = etPassN2.getText().toString();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Procesando Modificacion...");
        progressDialog.show();
        //  request POST para comprar clases
        String url = "http://10.0.2.2/WEBS/app_escuela/alumnoModPass.php";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.trim().equalsIgnoreCase("vacio")){
                    Toast.makeText(getContext(), "CAMPOS VACIOS :", Toast.LENGTH_SHORT).show();
                    System.out.println(" CAMPOS VACIOS ");
                }

                if (response.trim().equalsIgnoreCase("noLogin")){
                    Toast.makeText(getContext(), "CONTRASEÑA ERRONEA :", Toast.LENGTH_SHORT).show();
                    System.out.println(" LOGIN ERRONEO ");
                }

                if (response.trim().equalsIgnoreCase("diferentePass")){
                    Toast.makeText(getContext(), "NUEVO PASS INCORRECTO :", Toast.LENGTH_SHORT).show();
                    System.out.println(" LOS NUEVOS PASS DEBEN COINCIDIR ");
                }

                if(response.trim().equalsIgnoreCase("LoginOkpassModificado")){
                    Toast.makeText(getContext(), "Contraseña modificada:", Toast.LENGTH_SHORT).show();
                    System.out.println(" PASS MODIFICADO ");
                    //
                    editorSP = datosSP.edit();
                    editorSP.putString("pass",passN1);
                    editorSP.apply();
                    mostrarAlertPassMod();
                    // retorna datos pass
                    NavController navController = Navigation.findNavController(getView());
                    navController.navigate(R.id.action_passModFragment_to_passAlumFragment);
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
                parametros.put("passAlum", passAct);
                parametros.put("passNuevo",passN1);
                parametros.put("passNuevo2",passN2);

                return parametros;
            }
        };

        //request.add(stringRequest);
        volleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }// fin modificara PASS

    private void mostrarAlertPassMod() {
        AlertDialog.Builder alertaCompraClase = new AlertDialog.Builder(getContext());
        alertaCompraClase.setTitle("Contraseña Modificada");
        alertaCompraClase.setMessage("Contraseña modificada correctamente");
        alertaCompraClase.setPositiveButton("Aceptar", null);
        AlertDialog dialog = alertaCompraClase.create();
        dialog.show();
    }
}