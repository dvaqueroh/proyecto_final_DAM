package com.example.pruebalista.ui.perfil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pruebalista.MenuNavegacionActivity;
import com.example.pruebalista.R;
import com.example.pruebalista.baseDatosVolley.VolleySingleton;
import com.example.pruebalista.clases.Alumno;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link datosAlumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class datosAlumFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView tvUsuario,tvNombre,tvDni,tvNacimiento,tvTelefono,tvEmail;
    int idAlum,telefono;
    String usuario,nombre,dni,nacimiento,email;
    Button btMod,btVolver;
    SharedPreferences datosSP;

    Alumno alumnoSesion;
    // consultas Volley
    ProgressDialog progressDialog;
    RequestQueue request;
    StringRequest stringRequest;
    JsonObjectRequest jsonObjectRequest;
    VolleySingleton volleySingleton;

    public datosAlumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment datosAlumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static datosAlumFragment newInstance(String param1, String param2) {
        datosAlumFragment fragment = new datosAlumFragment();
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
        View vista = inflater.inflate(R.layout.fragment_datos_alum, container, false);

        tvUsuario = vista.findViewById(R.id.tvDatosUsuario);
        tvNombre = vista.findViewById(R.id.tvDatosNombre);
        tvDni = vista.findViewById(R.id.tvDatosDni);
        tvNacimiento = vista.findViewById(R.id.tvDatosNacimiento);
        tvTelefono = vista.findViewById(R.id.tvDatosTelefono);
        tvEmail = vista.findViewById(R.id.tvDatosEmail);


        btMod = vista.findViewById(R.id.btDatosMod);
        btVolver = vista.findViewById(R.id.btDatosVolver);

        btMod.setOnClickListener(this);
        btVolver.setOnClickListener(this);
        alumnoSesion = new Alumno();
        cargardatosAlumno();


        return vista;
    }

    private void cargardatosAlumno() {

        datosSP = getContext().getSharedPreferences("datosAlumno", Context.MODE_PRIVATE);
        MenuNavegacionActivity menuNav = new MenuNavegacionActivity();
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Iniciando Sesion...");
        progressDialog.show();
        int idAlum = datosSP.getInt("idAlum",-1);
        System.out.println("idUSUARIO  : "+idAlum);
        //  request POST para traer los datos del alumno
        //String url = " http://10.0.2.2/WEBS/app_escuela/login.php?usuarioAlum=PRUEBA&passAlum=123456 ";
        String url = "http://10.0.2.2/WEBS/app_escuela/alumnoMuestraDatos.php?idAlum="+idAlum;
        System.out.println("crea string Request GET");
        request = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(" ON RESPONSE GUARDA DATOS ALUMNO");
                        JSONArray jsonArray = null;
                        try {
                            System.out.println(" DENTRO DEL TRY");
                            jsonArray = response.getJSONArray("alumno");
                            JSONObject jsonObject = jsonArray.getJSONObject(0); //0 indica el primer objeto dentro del array.

                            alumnoSesion.setIdAlumno(jsonObject.getInt("idAlum"));
                            alumnoSesion.setUsuarioAlumno(jsonObject.getString("usuarioAlum"));
                            //alumnoSesion.setPassAlumno(jsonObject.getString("passAlum"));
                            alumnoSesion.setNombreAlumno(jsonObject.getString("nombreAlum"));
                            alumnoSesion.setDniAlumno(jsonObject.getString("dniAlum"));
                            alumnoSesion.setFechaNacAlumno(jsonObject.getString("nacimientoAlum"));
                            alumnoSesion.setTlfAlumno(jsonObject.getInt("tlfAlum"));
                            alumnoSesion.setEmailAlumno(jsonObject.getString("emailAlum"));
                            alumnoSesion.setIdFormaPago(jsonObject.getInt("idFormaPagoAux"));
                            alumnoSesion.setFormaPago(jsonObject.getString("datosFormaPago"));
                            alumnoSesion.setFormaPagoConfirmada(jsonObject.getInt("formaPagoConfirm"));
                            alumnoSesion.setBolsaClases(jsonObject.getInt("bolsaClases"));
                            alumnoSesion.setClasesPendientes(jsonObject.getInt("clasesPendientes"));
                            System.out.println(" carga datosusuario "+alumnoSesion.getUsuarioAlumno());
                            System.out.println("usuario "+alumnoSesion.getUsuarioAlumno());
                            tvUsuario.setText(alumnoSesion.getUsuarioAlumno());
                            System.out.println(" nombre "+alumnoSesion.getNombreAlumno());
                            tvNombre.setText(alumnoSesion.getNombreAlumno());
                            tvDni.setText(alumnoSesion.getDniAlumno());
                            tvNacimiento.setText(alumnoSesion.getFechaNacAlumno());
                            tvTelefono.setText(Integer.toString(alumnoSesion.getTlfAlumno()));
                            tvEmail.setText(alumnoSesion.getEmailAlumno());
                            progressDialog.hide();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.hide();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(" ERROR  RESPONSE");
                        // TODO: Handle error

                    }
                });
        // Access the RequestQueue through your singleton class.
        volleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
        ///


    }

    @Override
    public void onClick(View v) {
        NavController navController;
        switch (v.getId()){
            case R.id.btDatosMod:
                System.out.println("BOTON MODIFICAR DATOS ");
                // va a Modificar datos de pagos
                //Crear bundle, que son los datos que pasaremos
                Bundle datosAEnviar = new Bundle();
                datosAEnviar.putSerializable("alumno",alumnoSesion);
                navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_datosAlumFragment_to_datosModFragment,datosAEnviar);

                break;
            case R.id.btDatosVolver:
                // retorna al PERFIL
                navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_datosAlumFragment_to_nav_perfilFragment);
                break;
        }
    }
}