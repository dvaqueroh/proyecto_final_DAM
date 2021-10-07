package com.example.pruebalista.ui.asignaturas;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.pruebalista.MenuNavegacionActivity;
import com.example.pruebalista.R;
import com.example.pruebalista.baseDatosVolley.VolleySingleton;
import com.example.pruebalista.clases.Asignatura;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompaClasesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompaClasesFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Button btComprar,btVolver;
    SharedPreferences datosSP;
    SharedPreferences.Editor editorSP;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Asignatura asignatura;
    int numClases;
    int idAlum;
    int bolCla;
    int idAsig;
    Spinner spinner;
    // consultas Volley
    ProgressDialog progressDialog;
    RequestQueue request;
    StringRequest stringRequest;
    VolleySingleton volleySingleton;

    public CompaClasesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompaClasesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompaClasesFragment newInstance(String param1, String param2) {
        CompaClasesFragment fragment = new CompaClasesFragment();
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
            asignatura = (Asignatura) getArguments().getSerializable("asignatura");
            System.out.println("comprar clases de: " + asignatura.getNombre());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_compa_clase, container, false);
        btComprar = vista.findViewById(R.id.btComprar);
        btVolver = vista.findViewById(R.id.btVolver);

        btComprar.setOnClickListener(this);
        btVolver.setOnClickListener(this);
        //
        spinner = vista.findViewById(R.id.spinnerClases);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.paquetesClases, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        super.onViewCreated(vista, savedInstanceState);
        return vista;
    }

    @Override
    public void onClick(View v) {
        //System.out.println("Boton pulsado " + v.getId());
        NavController navController = Navigation.findNavController(v);
        switch (v.getId()){
            case R.id.btComprar:
                System.out.println(" btn COMPRAR CLASES");
                comprarClases();
                break;
            case R.id.btVolver:
                System.out.println("btn VOLVER ATRAS");
                navController.navigate(R.id.action_compaAsigFragment_to_nav_asigFragmentVolley);
                break;
        }
    }

    private void comprarClases() {
        MenuNavegacionActivity menuNav = new MenuNavegacionActivity();

        datosSP = getContext().getSharedPreferences("datosAlumno",Context.MODE_PRIVATE);

        idAlum = datosSP.getInt("idAlum",-1);
        bolCla = datosSP.getInt("bolsaClases",-1);
        idAsig = asignatura.getIdAsignatura();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Procesando Compra...");
        progressDialog.show();
        //  request POST para comprar clases
        String url = "http://10.0.2.2/WEBS/app_escuela/comprarClases.php";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.trim().equalsIgnoreCase("comprada")){
                    System.out.println("IdAlumno: "+idAlum+" - "+numClases+" clases compradas");
                    Toast.makeText(getContext(), "Clases compradas:", Toast.LENGTH_SHORT).show();

                    bolCla = bolCla + numClases;
                    System.out.println("bolsa de clases actualizada"+bolCla);
                    //
                    editorSP = datosSP.edit();
                    editorSP.putInt("bolsaClases",bolCla);
                    editorSP.apply();
                    //alert
                    mostrarAlertClaseComprada();
                    // retorna al HOME
                    NavController navController = Navigation.findNavController(getView());
                    navController.navigate(R.id.action_compaAsigFragment_to_nav_home);
                }
                else{
                    Toast.makeText(getContext(), "No se han comprado las clases", Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams() throws AuthFailureError{

                Map<String, String> parametros=new HashMap<>();
                parametros.put("idAlum",Integer.toString(idAlum));
                parametros.put("nClases",Integer.toString(numClases));
                System.out.println(" Alumno - "+idAlum);
                System.out.println(" nClases - "+numClases);

                return parametros;
            }
        };

        //request.add(stringRequest);
        volleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }// fin Comprar clases

    private void mostrarAlertClaseComprada() {
        AlertDialog.Builder alertaCompraClase = new AlertDialog.Builder(getContext());
        alertaCompraClase.setTitle("Clases Compradas");
        alertaCompraClase.setMessage("Se han comprado las clases correctamente");
        alertaCompraClase.setPositiveButton("Aceptar", null);
        AlertDialog dialog = alertaCompraClase.create();
        dialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String seleccion =spinner.getSelectedItem().toString();
        numClases = Integer.parseInt(seleccion);
        System.out.println("clases seleccionadas "+numClases);


        datosSP = getContext().getSharedPreferences("datosAlumno",Context.MODE_PRIVATE);
        int idAlum = datosSP.getInt("idAlum",-1);
        System.out.println("IdAlumno "+idAlum);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}// fin Fragment COMPRA CLASES