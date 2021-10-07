package com.example.pruebalista.ui.horasClases;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pruebalista.R;
import com.example.pruebalista.clases.Clase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HorarioDiaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HorarioDiaFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ////////  //////////
    //private static final android.R.attr R = ;

    private ArrayList<Clase> listaHoras;
    RecyclerView recyclerHoras;
    ProgressDialog progressDialog;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    private JSONArray jsonArray;
    Clase clase;
    Clase claseRecibida;
    public HorarioDiaFragment() {
        // Required empty public constructor

    }

    /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AsigFragmentVolley.
     */
    // TODO: Rename and change types and number of parameters

    public static HorarioDiaFragment newInstance(Clase claseEnviada) {
        HorarioDiaFragment fragment = new HorarioDiaFragment();
        Bundle args = new Bundle();
        args.putSerializable("clase", claseEnviada);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(" ** HORARIOS DIA FRAGMENT **");
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
        //super.onCreateView(inflater, container, savedInstanceState);
        View vista = inflater.inflate(R.layout.fragment_horario_dia_list, container, false);
        listaHoras = new ArrayList<Clase>();
        iniciaListaHoras();
        recyclerHoras = vista.findViewById(R.id.recyclerHoras);
        recyclerHoras.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerHoras.setHasFixedSize(true);

        request = Volley.newRequestQueue(getContext());
        cargarWebServiceClasesHoras();

        return vista;
    }

    private void iniciaListaHoras() {
        Clase clase = null;
        String[] ArrHoras = {"12:00-13:00","13:00-14:00","15:00-16:00","16:00-17:00","17:00-18:00","18:00-19:00","19:00-20:00","20:00-21:00"};
        System.out.println(" llena la lista de horas con 8 ");
        for (int i = 0; i < 8 ; i++) {
            clase = new Clase();
            clase.setIdHora(i+1);
            clase.setHora(ArrHoras[i]);
            clase.setEstado(0);
            listaHoras.add(clase);
        }
    }

    private void cargarWebServiceClasesHoras() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Procesando busqueda...");
        progressDialog.show();
        //
        int idAsig = claseRecibida.getIdAsignatura();
        String fecha = claseRecibida.getFecha();
        System.out.println(" ** idAsig: " + idAsig +" fecha: "+fecha);
        //
        String url = "http://10.0.2.2/WEBS/app_escuela/muestraClasesDia.php?asignatura="+idAsig+"&fecha="+fecha;
        //String url = "http://10.0.2.2/WEBS/app_escuela/muestraClasesDia.php?asignatura=2&fecha=2021-05-21";
        System.out.println(" ** URL: " + url);
        // parametros para pasar por el metodo GET
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        System.out.println(" crea el Json Object Request ");
        request.add(jsonObjectRequest);

    }

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
                System.out.println(" NO HAY CLASES RESERVADAS ESE DIA ");
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
                    for (int j = 0; j < listaHoras.size(); j++) {
                        //System.out.println(" dentro del FOR resultados - J="+j);
                        Clase claseL = new Clase();
                        claseL = listaHoras.get(j);
                        //System.out.println(" clase L - idHora="+claseL.getIdHora());
                        if (clase.getIdHora() == claseL.getIdHora()) {
                            //System.out.println(" ** COINCIDE, SE SUSTITUYE LA CLASE**");
                            listaHoras.set(j, clase);
                            j = listaHoras.size();
                        }

                    }

                } // for resultados
            }
            progressDialog.hide();
            Toast.makeText(getContext(), "Operacion exitosa:", Toast.LENGTH_SHORT).show();
            //adaptadorAsignaturas adaptador = new adaptadorAsignaturas(listaClase);

            System.out.println(" crea adaptador");
            adaptadorClasesAsig adaptador = new adaptadorClasesAsig(listaHoras);
            adaptador.enviarDatos(claseRecibida);
            recyclerHoras.setAdapter(adaptador);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Error.Response", e.toString());
            Toast.makeText(getContext(), "no se puede establecer conexion", Toast.LENGTH_SHORT).show();
            progressDialog.hide();
        }
        /*
        System.out.println(" crea adaptador");
        adaptadorClasesAsig adaptador = new adaptadorClasesAsig(listaHoras);
        recyclerHoras.setAdapter(adaptador);
        */


    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("Error.Response", error.toString());
        System.out.println(" Error en respuesta - "+error.toString());
        Toast.makeText(getContext(), "Error en respuesta", Toast.LENGTH_SHORT).show();
        progressDialog.hide();
    }

// GET SET

    public Clase getClaseRecibida() {
        System.out.println("envia objero ClaseRecibida");
        System.out.println("*******************************");
        System.out.println("* objeto clase datos a enviar *");
        System.out.println("* id Alumno - "+claseRecibida.getIdAlumno()+" *");
        System.out.println("* id Asignatura - "+claseRecibida.getIdAsignatura()+" *");
        System.out.println("* Asignatura - "+claseRecibida.getAsignatura()+" *");
        System.out.println("* id Hora - "+" *");
        System.out.println("* Hora - "+" *");
        System.out.println("* fecha - "+claseRecibida.getFecha()+" *");
        System.out.println("*******************************");
        return claseRecibida;
    }

}// fin clase ASIG FRAGMENT VOLLEY