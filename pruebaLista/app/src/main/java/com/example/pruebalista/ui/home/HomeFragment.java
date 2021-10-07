package com.example.pruebalista.ui.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pruebalista.MainActivity;
import com.example.pruebalista.MenuNavegacionActivity;
import com.example.pruebalista.R;
import com.example.pruebalista.baseDatosVolley.VolleySingleton;
import com.example.pruebalista.clases.Alumno;
import com.example.pruebalista.clases.Asignatura;
import com.example.pruebalista.clases.Clase;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    //private Alumno alumnoSesion;
    boolean sesionIniciada;
    MenuNavegacionActivity meNavAct;
    MainActivity mainAc;
    TextView etUsuario;
    // consultas Volley
    ProgressDialog progressDialog;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    //
    SharedPreferences datosSP;
    SharedPreferences.Editor editorSP;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //

        //final TextView textView = root.findViewById(R.id.text_home);
        etUsuario = root.findViewById(R.id.tv_Home_Usuario);
        meNavAct = new MenuNavegacionActivity();
        System.out.println(" ** CARGA HOME **");
        datosSP = getContext().getSharedPreferences("datosAlumno",Context.MODE_PRIVATE);
        int idAl = datosSP.getInt("idAlum",-1);
        sesionIniciada = datosSP.getBoolean("sesion",false);
        String pass = datosSP.getString("pass","");
        // si ya ha iniciado sesion, no vuelve a cargar los datos

        System.out.println(" HOME - ALUMNO: " + idAl);
        //comprueba si el pass es el provisional al crear el alumno, si es asi, lanza fragment para cambiarlo
        if(pass.equalsIgnoreCase("Prov11223344")){
            System.out.println(" pass Provisional, debe cambiarlo, salta Fragment cambio contraseña ");


            // borra el pass del sharedPreference
            editorSP = datosSP.edit();
            editorSP.putString("pass","");
            editorSP.apply();
            // lanza el fragment CAMBIAR CONTRASEÑA
            AlertDialog.Builder alertaCompraClase = new AlertDialog.Builder(getContext());
            alertaCompraClase.setTitle("Cambio de Contraseña");
            alertaCompraClase.setMessage("Debe cambiar su contraseña provisional");
            alertaCompraClase.setPositiveButton("Cambiar", null);
            AlertDialog dialog = alertaCompraClase.create();
            dialog.show();
            //View vista = inflater.inflate(R.layout.fragment_home, container, false);
            NavController navController;
            navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
            navController.navigate(R.id.action_nav_home_to_passAlumFragment);

             
        }
        else {
            System.out.println("SesionIniciada = " + sesionIniciada);
            if (sesionIniciada == false) {

                //alumnoSesion = (Alumno) getArguments().getSerializable("alumnoSesion");

                System.out.println(" HOME - ALUMNO SESION RECIBIDO: " + idAl);
                editorSP = datosSP.edit();
                editorSP.putBoolean("sesion", true);
                editorSP.apply();
                sesionIniciada = datosSP.getBoolean("sesion", false);
                System.out.println("SesionIniciada = " + sesionIniciada);
            } else {
                System.out.println(" HOME - SESION YA INICIADA: " + idAl);
            }
            System.out.println("SesionIniciada = " + sesionIniciada);
        }
        //
        // CARGAR LA FOTO DEL USUARIO


        //
        etUsuario.setText(datosSP.getString("nombreAlum",""));

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                //textView.setText(s);
            }
        });
        return root;
    }

    /* GET SET*/

}// fin HOME Fragment