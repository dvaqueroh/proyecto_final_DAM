package com.example.pruebalista.ui.perfil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PagosModFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PagosModFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int idTipoFormaPago;
    private String datosFormaPago;

    EditText etFormaPago,etDatosPago;
    Button btMod,btVolver;
    Spinner spinnerPagos;
    SharedPreferences datosSP;
    SharedPreferences.Editor editorSP;
    // consultas Volley
    ProgressDialog progressDialog;
    RequestQueue request;
    StringRequest stringRequest;
    VolleySingleton volleySingleton;

    public PagosModFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PagosModFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PagosModFragment newInstance(String param1, String param2) {
        PagosModFragment fragment = new PagosModFragment();
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
        View vista = inflater.inflate(R.layout.fragment_pagos_mod, container, false);
        //etFormaPago = (EditText)vista.findViewById(R.id.etModPagForma);
        etDatosPago = (EditText)vista.findViewById(R.id.etModPagoDatos);



        btMod = vista.findViewById(R.id.btModPagModificar);
        btVolver = vista.findViewById(R.id.btModPagVolver);
        btMod.setOnClickListener(this);
        btVolver.setOnClickListener(this);

        //
        spinnerPagos = vista.findViewById(R.id.spinnerModPagos);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.formasPago, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerPagos.setAdapter(adapter);
        spinnerPagos.setOnItemSelectedListener(this);
        
        return vista;
    }

    @Override
    public void onClick(View v) {
        NavController navController;
        switch (v.getId()){
            case R.id.btModPagModificar:
                System.out.println("BOTON MODIFICAR PAGOS ");
                modificarDatosPago();

                break;
            case R.id.btModPagVolver:
                // retorna al PERFIL
                navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_pagosModFragment_to_pagosFragment);
                break;
        }
    }

    private void modificarDatosPago() {
        MenuNavegacionActivity menuNav = new MenuNavegacionActivity();

        datosSP = getContext().getSharedPreferences("datosAlumno", Context.MODE_PRIVATE);

        int idAlum = datosSP.getInt("idAlum",-1);

        //idTipoFormaPago = etFormaPago.getText().toString();
        datosFormaPago = etDatosPago.getText().toString();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Procesando Modificacion...");
        progressDialog.show();
        //  request POST para comprar clases
        String url = "http://10.0.2.2/WEBS/app_escuela/alumnoModPagos.php";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.trim().equalsIgnoreCase("modificado")){
                    Toast.makeText(getContext(), "Datos modificados:", Toast.LENGTH_SHORT).show();
                    System.out.println(" MODIFICADO ");
                    //
                    editorSP = datosSP.edit();
                    editorSP.putInt("idFormaPagoAux",idTipoFormaPago);
                    editorSP.putString("datosFormaPago",datosFormaPago);
                    editorSP.apply();
                    //
                    mostrarAlertModPagos();
                    // retorna datos pago
                    NavController navController = Navigation.findNavController(getView());
                    navController.navigate(R.id.action_pagosModFragment_to_pagosFragment);
                }
                else{
                    Toast.makeText(getContext(), "No se hanmodificado los datos", Toast.LENGTH_SHORT).show();
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
                parametros.put("idFormaPago", String.valueOf(idTipoFormaPago));
                parametros.put("datosPago",datosFormaPago);
                System.out.println(" Alum - "+idAlum);
                System.out.println(" tipo - "+idTipoFormaPago);
                System.out.println(" datos - "+datosFormaPago);

                return parametros;
            }
        };

        //request.add(stringRequest);
        volleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }// fin modificar datos pago

    private void mostrarAlertModPagos() {
        AlertDialog.Builder alertaCompraClase = new AlertDialog.Builder(getContext());
        alertaCompraClase.setTitle("Pagos Modificados");
        alertaCompraClase.setMessage("Se han modificado los datos correctamente");
        alertaCompraClase.setPositiveButton("Aceptar", null);
        AlertDialog dialog = alertaCompraClase.create();
        dialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String seleccion =spinnerPagos.getSelectedItem().toString();
        int numForm = spinnerPagos.getSelectedItemPosition();
        System.out.println("FORMA  seleccionadas "+seleccion);
        System.out.println("id item "+ numForm);
        idTipoFormaPago = numForm;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}