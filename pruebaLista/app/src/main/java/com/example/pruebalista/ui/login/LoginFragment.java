package com.example.pruebalista.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private String UsPass;
    MenuNavegacionActivity meNavAct;
    // consultas Volley
    ProgressDialog progressDialog;
    RequestQueue request;
    StringRequest stringRequest;
    JsonObjectRequest jsonObjectRequest;
    VolleySingleton volleySingleton;
    //
    Alumno alumnoSesion;
    SharedPreferences datosSP;
    SharedPreferences.Editor editorSP;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_login, container, false);
        //datosSP = getContext().getSharedPreferences("datosAlumno", Context.MODE_PRIVATE);
        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = view.findViewById(R.id.username);
        final EditText passwordEditText = view.findViewById(R.id.password);
        final Button loginButton = view.findViewById(R.id.login);
        final ProgressBar loadingProgressBar = view.findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                System.out.println(" ** llama al metodo para que compruebe el login ** ");
                // metodo que hace la consulta al servidor
                cargarWebServiceLogin();
            }

            private void cargarWebServiceLogin() {
                MenuNavegacionActivity menuNav = new MenuNavegacionActivity();
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Autenticando...");
                progressDialog.show();
                //  request POST para comprar clases
                String url = "http://10.0.2.2/WEBS/app_escuela/login.php";
                System.out.println("crea string Request POST");
                stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.trim().equalsIgnoreCase("LoginOk")){
                            // login correcto, devuelve objeto Alumno
                            // se guardan datos en
                            System.out.println("Login correcto");
                            Toast.makeText(getContext(), "Login Correcto", Toast.LENGTH_SHORT).show();
                            // guarda datos en el shared preferences

                            // abre al home pasando datos
                            alumnoSesion = new Alumno();
                            alumnoSesion.setUsuarioAlumno(usernameEditText.getText().toString());
                            //alumnoSesion.setPassAlumno(passwordEditText.getText().toString());
                            //
                            UsPass = passwordEditText.getText().toString();
                            cargarWebServiceSesion();
                            //
                            /*
                            Bundle datosAEnviar = new Bundle();
                            datosAEnviar.putSerializable("alumnoSesion",alumnoSesion);
                            NavController navController = Navigation.findNavController(getView());
                            navController.navigate(R.id.action_loginFragment_to_nav_home,datosAEnviar);
                             */
                            loginViewModel.login(alumnoSesion.getUsuarioAlumno(),alumnoSesion.getPassAlumno());
                            progressDialog.hide();
                        }
                        else{
                            Toast.makeText(getContext(), "Login incorrecto", Toast.LENGTH_SHORT).show();
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
                        mostrarAlertFalloConexion();
                        progressDialog.hide();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> parametros=new HashMap<>();
                        System.out.println(" usuario - "+usernameEditText.getText().toString());
                        System.out.println(" pass - "+passwordEditText.getText().toString());
                        parametros.put("usuarioAlum",usernameEditText.getText().toString());
                        parametros.put("passAlum",passwordEditText.getText().toString());


                        return parametros;
                    }
                };

                //request.add(stringRequest);
                System.out.println("VolleySingleton string Request LOGIN");
                volleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
            }// fin cargar web service LOGIN
        });
    }

    private void cargarWebServiceSesion() {
        MenuNavegacionActivity menuNav = new MenuNavegacionActivity();
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Iniciando Sesion...");
        progressDialog.show();
        String usuario = alumnoSesion.getUsuarioAlumno();
        String pass = UsPass;
        System.out.println("usuario y pass : "+usuario+" - "+pass);
        //  request POST para traer los datos del alumno
        //String url = " http://10.0.2.2/WEBS/app_escuela/login.php?usuarioAlum=PRUEBA&passAlum=123456 ";
        String url = "http://10.0.2.2/WEBS/app_escuela/login.php?usuarioAlum="+usuario+"&passAlum="+pass;
        System.out.println("crea string Request GET");
        request = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(" ON RESPONSE");
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("alumno");
                            JSONObject jsonObject = jsonArray.getJSONObject(0); //0 indica el primer objeto dentro del array.
                            System.out.println(" *** Sesion iniciada, - "+jsonObject.getString("usuarioAlum"));
                            alumnoSesion.setIdAlumno(jsonObject.getInt("idAlum"));
                            alumnoSesion.setUsuarioAlumno(jsonObject.getString("usuarioAlum"));
                            alumnoSesion.setPassAlumno(jsonObject.getString("passAlum"));
                            alumnoSesion.setNombreAlumno(jsonObject.getString("nombreAlum"));
                            //alumnoSesion.setDniAlumno(jsonObject.getString("dniAlum"));
                            alumnoSesion.setFechaNacAlumno(jsonObject.getString("nacimientoAlum"));
                            alumnoSesion.setTlfAlumno(jsonObject.getInt("tlfAlum"));
                            alumnoSesion.setEmailAlumno(jsonObject.getString("emailAlum"));
                            alumnoSesion.setIdFormaPago(jsonObject.getInt("idFormaPagoAux"));
                            alumnoSesion.setFormaPago(jsonObject.getString("datosFormaPago"));
                            alumnoSesion.setFormaPagoConfirmada(jsonObject.getInt("formaPagoConfirm"));
                            alumnoSesion.setBolsaClases(jsonObject.getInt("bolsaClases"));
                            alumnoSesion.setClasesPendientes(jsonObject.getInt("clasesPendientes"));
                            //alumnoSesion.setDato(jsonObject.getString("fotoAlum"));
                            // GRBAR datos en SHARED PREFERENCES
                            guardarDatosLogin();
                            //
                            // lanza el fragment HOME
                            System.out.println(" *** Sesion iniciada - "+alumnoSesion.getUsuarioAlumno());
                            Toast.makeText(getContext(), "SESION INICIADA - "+alumnoSesion.getUsuarioAlumno(), Toast.LENGTH_SHORT).show();
                            //Bundle datosAEnviar = new Bundle();
                            //datosAEnviar.putSerializable("alumnoSesion",alumnoSesion);
                            NavController navController = Navigation.findNavController(getView());
                            navController.navigate(R.id.action_loginFragment_to_nav_home);
                            progressDialog.hide();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.hide();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        mostrarAlertFalloConexion();
                    }
                });

        // Access the RequestQueue through your singleton class.
        volleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

    }// fin cargar webService SESION -- datos alumno


    private void guardarDatosLogin() {
        datosSP = getContext().getSharedPreferences("datosAlumno",Context.MODE_PRIVATE);
        editorSP = datosSP.edit();
        // metemos los datos CLAVE VALOR al editor del shared preferences
        editorSP.putBoolean("sesion",false);// se pasa a true en HOME
        editorSP.putInt("idAlum",alumnoSesion.getIdAlumno());
        editorSP.putString("usuario",alumnoSesion.getUsuarioAlumno());
        editorSP.putString("pass",alumnoSesion.getPassAlumno());// se guarda para comprobar si no es el provisional y se borra despues
        editorSP.putString("nombreAlum",alumnoSesion.getNombreAlumno());
        //editorSP.putString("dniAlum",alumnoSesion.getDniAlumno());
        editorSP.putString("nacimientoAlum",alumnoSesion.getFechaNacAlumno());
        editorSP.putInt("tlfAlum",alumnoSesion.getTlfAlumno());
        editorSP.putString("emailAlum",alumnoSesion.getEmailAlumno());
        editorSP.putInt("idFormaPagoAux",alumnoSesion.getIdFormaPago());
        editorSP.putString("datosFormaPago",alumnoSesion.getFormaPago());
        editorSP.putInt("formaPagoConfirm",alumnoSesion.getFormaPagoConfirmada());
        editorSP.putInt("bolsaClases",alumnoSesion.getBolsaClases());
        editorSP.putInt("clasesPendientes",alumnoSesion.getClasesPendientes());
        //editorSP.putString("fotoAlum",alumnoSesion.getDato()); //imagen en base 64
        System.out.println(" guarda los datos del LOGIN en en el SHAREDPREFERENCES ");
        // guardamos los datos en el editor
        editorSP.apply();
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        }
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }
    private void mostrarAlertFalloConexion() {
        AlertDialog.Builder alertaCompraClase = new AlertDialog.Builder(getContext());
        alertaCompraClase.setTitle("Fallo en conexion");
        alertaCompraClase.setMessage("revise su conexion a Internet");
        alertaCompraClase.setPositiveButton("Aceptar", null);
        AlertDialog dialog = alertaCompraClase.create();
        dialog.show();
    }
}// fin Fragment LOGIN