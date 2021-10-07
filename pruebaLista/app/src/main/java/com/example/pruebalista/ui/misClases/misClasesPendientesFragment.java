package com.example.pruebalista.ui.misClases;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pruebalista.R;
import com.example.pruebalista.clases.Clase;
import com.example.pruebalista.dummy.DummyContent;
import com.example.pruebalista.ui.horasClases.adaptadorClasesAsig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class misClasesPendientesFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    private ArrayList<Clase> listaClases;
    RecyclerView recyclerClases;
    ProgressDialog progressDialog;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    private JSONArray jsonArray;
    Clase clase;
    SharedPreferences datosSP;

    public misClasesPendientesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static misClasesPendientesFragment newInstance(int columnCount) {
        misClasesPendientesFragment fragment = new misClasesPendientesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_mis_clases_pendientes_list, container, false);

        /*
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new AdaptadorMisClasesPend(DummyContent.ITEMS));
        }
        */
        listaClases = new ArrayList<Clase>();
        recyclerClases = vista.findViewById(R.id.recyclerMisClases);
        recyclerClases.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerClases.setHasFixedSize(true);

        request = Volley.newRequestQueue(getContext());

        cargarWebServiceMisClasesPendientes();
        return vista;
    }

    private void cargarWebServiceMisClasesPendientes() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Procesando busqueda...");
        progressDialog.show();
        datosSP = getContext().getSharedPreferences("datosAlumno", Context.MODE_PRIVATE);

        //
        int idAlum ;
        idAlum = datosSP.getInt("idAlum",-1);
        System.out.println(" ** idAlum: " + idAlum+" **");
        //
        String url = "http://10.0.2.2/WEBS/app_escuela/muestraMisClasesPendientes.php?idAlum="+idAlum;
        System.out.println(" ** URL: " + url);
        // parametros para pasar por el metodo GET
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null, (Response.Listener<JSONObject>) this,this);
        System.out.println(" crea el Json Object Request ");
        request.add(jsonObjectRequest);
    }// fin web service mis clases

    @Override
    public void onResponse(JSONObject response) {
        System.out.println(" ON RESPONSE ");
        Clase clase = null;
        Clase claseLis = null;

        JSONArray jsonArray = response.optJSONArray("clase");
        JSONObject jsobjeto = null;

        try {

            // comprueba si el primer objeto del array recibido , si el campo idClase es -1 es que el array esta vacio
            jsobjeto = jsonArray.getJSONObject(0);
            int idAs = jsobjeto.getInt("idClase");
            //System.out.println(" OBJETO recibido "+idAs);
            //
            if(idAs == -1){
                System.out.println(" NO HAY CLASES PENDIENTES ");
                mostrarAlertClases();
            }
            else {
                // for recorre el array de los resultados de la consulta
                for (int i = 0; i < jsonArray.length(); i++) {
                    clase = new Clase();
                    jsobjeto = null;
                    System.out.println(" dentro del FOR resultados - I=" + i);
                    // guarda en un OBJETO una asignatura del array con las asignaturas recibidas
                    jsobjeto = jsonArray.getJSONObject(i);
                    //
                    //Agrega datos al objeto CLASE
                    // "idClase"/"estadoClase"/ fechaClase"/"idHora"/"hora"/"idAsignatura"/"nombreAsignatura"/"idAlum"/"nombreAlum"
                    clase.setIdClase(jsobjeto.getInt("idClase"));
                    clase.setIdAsignatura(jsobjeto.getInt("idAsignatura"));
                    clase.setAsignatura(jsobjeto.getString("nombreAsignatura"));
                    clase.setIdAlumno(jsobjeto.getInt("idAlum"));
                    clase.setAlumno(jsobjeto.getString("nombreAlum"));
                    clase.setFecha(jsobjeto.getString("fechaClase"));
                    clase.setIdHora(jsobjeto.getInt("idHora"));
                    clase.setHora(jsobjeto.getString("Hora"));
                    clase.setEstado(jsobjeto.getInt("estadoClase"));
                    //
                    // for recorre la lista de horas y sustituye las que estan reservadas
                    if(clase.getEstado()==1) {
                        listaClases.add(clase);
                    }

                } // for resultados
            }
            progressDialog.hide();
            Toast.makeText(getContext(), "Operacion exitosa:", Toast.LENGTH_SHORT).show();
            //adaptadorAsignaturas adaptador = new adaptadorAsignaturas(listaClase);

            System.out.println(" crea adaptador");
            AdaptadorMisClasesPend adaptador = new AdaptadorMisClasesPend(listaClases);
            recyclerClases.setAdapter(adaptador);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Error.Response", e.toString());
            Toast.makeText(getContext(), "no se puede establecer conexion", Toast.LENGTH_SHORT).show();
            progressDialog.hide();
        }

    }// fin on response



    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("Error.Response", error.toString());
        System.out.println(" Error en respuesta - "+error.toString());
        Toast.makeText(getContext(), "Error en respuesta", Toast.LENGTH_SHORT).show();
        progressDialog.hide();
    }

    private void mostrarAlertClases() {
        AlertDialog.Builder alertaCompraClase = new AlertDialog.Builder(getContext());
        alertaCompraClase.setTitle("Clases Pendientes");
        alertaCompraClase.setMessage("No tienes clases reservadas");
        alertaCompraClase.setPositiveButton("Aceptar", null);
        AlertDialog dialog = alertaCompraClase.create();
        dialog.show();
    }

}// fin Fragment MIS CLASES PENDIENTES