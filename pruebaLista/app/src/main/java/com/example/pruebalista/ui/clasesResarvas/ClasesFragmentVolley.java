package com.example.pruebalista.ui.clasesResarvas;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
import com.example.pruebalista.MenuNavegacionActivity;
import com.example.pruebalista.R;
import com.example.pruebalista.clases.Asignatura;
import com.example.pruebalista.clases.Clase;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClasesFragmentVolley#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClasesFragmentVolley extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //
    private int bolsaClases;

    private MenuNavegacionActivity meNavAct;
    private ArrayList<Asignatura> listaAsignaturas;
    RecyclerView recyclerAsignaturas;
    ProgressDialog progressDialog;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    private JSONArray jsonArray;
    Clase clase;
    //
    SharedPreferences datosSP;

    public ClasesFragmentVolley() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClasesFragmentVolley.
     */
    // TODO: Rename and change types and number of parameters

    public static ClasesFragmentVolley newInstance(String param1, String param2) {
        ClasesFragmentVolley fragment = new ClasesFragmentVolley();
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
            //clase = (Clase) getArguments().getSerializable("clase");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_clases_volley, container, false);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        meNavAct = new MenuNavegacionActivity();


        listaAsignaturas = new ArrayList<Asignatura>();
        recyclerAsignaturas = vista.findViewById(R.id.recyclerClases);
        recyclerAsignaturas.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAsignaturas.setHasFixedSize(true);

        //comprueba si el alumno tiene clases compradas, 1 o mas
        datosSP = getContext().getSharedPreferences("datosAlumno",Context.MODE_PRIVATE);
        bolsaClases = datosSP.getInt("bolsaClases",-1);

        if(bolsaClases > 0) {
            request = Volley.newRequestQueue(getContext());
            System.out.println(" tiene clases compradas, carga web service");
            cargarWebServiceClases();


        }
        else{
            // si no tiene calses compradas carga el fragment para comprar clases
            System.out.println(" ** NO TIENE CLASES COMPRADAS - carga fragment comprar clases");
            Toast.makeText(getContext(), "No tiene clases compradas:", Toast.LENGTH_SHORT).show();
            mostrarAlertaComprarClases(vista);
        }

        return vista;
    }

    private void mostrarAlertaComprarClases(View vista) {
        AlertDialog.Builder alertaCompraClase = new AlertDialog.Builder(getContext());
        alertaCompraClase.setTitle("No tiene clases compradas");
        alertaCompraClase.setMessage("No puede reservar clases, debe tener clases compradas.");
        alertaCompraClase.setPositiveButton("Aceptar", null);
        AlertDialog dialog = alertaCompraClase.create();
        dialog.show();

    }// fin mostrar alerta clases


    private void cargarWebServiceClases() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Procesando busqueda...");
        progressDialog.show();
        //
        String url = "http://10.0.2.2/WEBS/app_escuela/asignaturasReservaFoto.php";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);

    }

    @Override
    public void onResponse(JSONObject response) {
        Asignatura asignatura = null;
        JSONArray jsonArray = response.optJSONArray("asignatura");


        try {
            for (int i = 0; i <jsonArray.length(); i++) {
                asignatura = new Asignatura();
                JSONObject jsobjeto = null;

                // guarda en un OBJETO una asignatura del array con las asignaturas recibidas
                jsobjeto = jsonArray.getJSONObject(i);
                //
                //Agrega datos al objeto asignatura
                asignatura.setIdAsignatura(jsobjeto.getInt("idAsignatura"));
                asignatura.setNombre(jsobjeto.getString("nombreAsignatura"));
                asignatura.setDato(jsobjeto.getString("fotoAsig"));
                asignatura.setInfo("Clases individuales de "+jsobjeto.getString("nombreAsignatura"));

                listaAsignaturas.add(asignatura);

            }
            progressDialog.hide();
            Toast.makeText(getContext(), "Operacion exitosa:", Toast.LENGTH_SHORT).show();
            adaptadorHoras adaptador = new adaptadorHoras(getContext(),listaAsignaturas);
            recyclerAsignaturas.setAdapter(adaptador);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Error.Response", e.toString());
            Toast.makeText(getContext(), "no se puede establecer conexion", Toast.LENGTH_SHORT).show();
            progressDialog.hide();
        }
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("Error.Response", error.toString());
        System.out.println(" Error en respuesta - "+error.toString());
        Toast.makeText(getContext(), "Error en respuesta", Toast.LENGTH_SHORT).show();
        progressDialog.hide();

    }


}// fin CLASES VOLLEY