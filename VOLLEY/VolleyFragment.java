package com.example.pruebalista.baseDatosVolley;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.pruebalista.ui.asignaturas.Asignatura;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VolleyFragment extends BaseVolleyFragment {
    private static final android.R.attr R = ;
    private TextView label;
    private Button connector;
    private JSONArray jsonArray;
    private ArrayList<Asignatura> listaAsignaturas = new ArrayList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //View v = inflater.inflate(R.layout.fragment_volley, container, false);
        View v = inflater.inflate(R.layout, container, false);
        label = (TextView) v.findViewById(R.id.label);
        connector = (Button) v.findViewById(R.id.btn_Volley);

        System.out.println("Carga Fragment VOLLEY");
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        connector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Boton CONECTAR");
                creaRequest();
            }
        });
    }// fin on view created

    private void creaRequest(){
        //String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=39.476245,-0.349448&sensor=true";
        //String url = "https://mdn.github.io/learning-area/javascript/oojs/json/superheroes.json";
        // base de datos PHP MYADMIN
        //String url = "http://localhost/WEBS/app_escuela/asignatura.php";
        // localhost para android studio 10.0.2.2
        String url = "http://10.0.2.2/WEBS/app_escuela/asignatura.php";
        //
        RequestQueue queue = Volley.newRequestQueue(getContext());
        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override public void onResponse(JSONArray response) {
                // display response
                jsonArray = response;
                Log.d("Response", jsonArray.toString());
                //
                    cargarDatosRespuesta(jsonArray);
                //
                Toast.makeText(getContext(), "Operacion exitosa:", Toast.LENGTH_SHORT).show();
                onConnectionFinished();
            }


        }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
                label.setText(error.toString());
                Toast.makeText(getContext(), "Error en respuesta", Toast.LENGTH_SHORT).show();
                onConnectionFailed(error.toString());
            }
        } );
        // add it to the RequestQueue
        queue.add(getRequest);
        //addToQueue(request);
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        // Access the RequestQueue through your singleton class.
        //MySingleton.getInstance(getContext()).addToRequestQueue(request);

    } //fin MAKE REQUEST
    private void cargarDatosRespuesta(JSONArray jsonArray) {

        System.out.println("CARGA LOS DATOS DE LA RESPUESTA");
        //label.setText(jsonArray.toString());
        Asignatura asignatura = new Asignatura();
        int idAsig;
        String nombAsignatura;
        JSONObject objeto = null;
        try {
            for(int i = 0; i < jsonArray.length(); i++) {

                //Revisa el objeto dentro del array
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                //Agrega Descripcion y Talla
                idAsig = jsonObject.getInt("idAsignatura");
                nombAsignatura = jsonObject.getString("nombreAsignatura");
                //asignatura.idAsignatura = idAsig;
                //asignatura.nombreAsignatura = nombAsignatura;
                //System.out.println(asignatura.idAsignatura);
                //System.out.println(asignatura.nombreAsignatura);
                listaAsignaturas.add(asignatura);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("termina el for");
        label.setText(listaAsignaturas.toString());
        /*
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objeto = jsonArray.getJSONObject(i);
                idAsig = objeto.getInt("idAsignatura");
                asignatura = objeto.getString("nombreAsignatura");
                listaAsignatuas[i][0].add(idAsig);
                listaAsignatuas[i][1].add(asignatura);
            }//

            label.setText(listaAsignatuas.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */
    }// fin carcar datos de la respuesta

}// fin Main Fragment
