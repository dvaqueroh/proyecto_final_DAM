package com.example.pruebalista;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pruebalista.clases.Alumno;
import com.example.pruebalista.ui.home.HomeFragment;
import com.example.pruebalista.ui.perfil.CerrarSesionFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MenuNavegacionActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private boolean sesionIniciada;
    NavigationView navigationView;
    SharedPreferences datosSP;
    SharedPreferences.Editor editorSP;
    String llave = "datosAlumno";
    NavController navController;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppEscuela_NoActionBar);
        setContentView(R.layout.activity_menu_navegacion);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //FloatingActionButton fab = findViewById(R.id.fab);
        System.out.println(" **** CARGA MENU NAVEGATION ACTIVITY **** ");
        /*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
         */
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view); //instancia el Navigation Drawer
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_perfilFragment, R.id.nav_usuarioClasesFragment,R.id.misClasesPendientesFragment,
                R.id.asignaturaDetalleFragment,R.id.nav_asigFragmentVolley,R.id.compraAsigFragment,R.id.nav_clases_volleyFragment,
                R.id.acercaFragment)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //
        System.out.println(" **** COMPRUEBA SI HAY SESION ABIERTA **** ");
        datosSP = getSharedPreferences("datosAlumno", Context.MODE_PRIVATE);
        sesionIniciada = datosSP.getBoolean("sesion",false);
        System.out.println(" **** SESION INICIADA : "+sesionIniciada);
        if( sesionIniciada==true){
            // CARGAR LA FOTO DEL USUARIO
            //cargaDatosNavDraw();
            System.out.println(" **** SESION ABIERTA, VA DIRECTAMENTE A HOME**** ");
            navController.navigate(R.id.action_loginFragment_to_nav_home);
        }
        else{
            System.out.println(" **** SESION NO INICIADA, ABRE LOGIN **** ");
        }

        //
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        // CARGAR LA FOTO DEL USUARIO
        //cargaDatosNavDraw();

    }// fin ON CREATE
/*
    public void cargaDatosNavDraw() {

        TextView tvUsuario = navigationView.findViewById(R.id.nav_header_userName);
        //ImageView idFotoAlum = navigationView.findViewById(R.id.fotoPerfilNavDraw);
        System.out.println(" **** CARGA USUARIO Y FOTO EN NAV DRAWER **** ");
        String usuario = datosSP.getString("usuarioAlum","");
        System.out.println(" usuario drawer"+usuario);
        tvUsuario.setText(usuario);
        // si la asignatura tiene foto la carga, si no, carga una imagen por defecto
        /*
        if(datosSP.getString("fotoAlum","") != null) {
            byte[] byteDato = Base64.decode(datosSP.getString("fotoAlum",""),Base64.DEFAULT);
            //crea la imagen decodificando los datos base 64
            Bitmap imagenAlum = BitmapFactory.decodeByteArray(byteDato,0,byteDato.length);
            idFotoAlum.setImageBitmap(imagenAlum);
        }else{
            idFotoAlum.setImageResource(R.drawable.ic_menu_camera);
        }


    }// cargar datos nav drawer

    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_navegacion, menu);
        return true;

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_acercaDe:
                System.out.println(" **** ACTION ACERCA DE **** ");
                //NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.acercaFragment);
                return true;
            case R.id.action_settings:
                System.out.println(" **** ACTION SETTINGS **** ");
                return true;
            case R.id.action_salir:
                System.out.println(" **** ACTION SALIR**** ");
                confirmarSalir();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void confirmarSalir() {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿ Desea cerrar la aplicacion ? (no cierra sesión)");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //cerrarAplicacion(); // vacia los datos de sesion de shared preferences y cierra sesion
                System.out.println(" cierra la app - FINISH ");
                //finish();
                System.exit(0);
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.cancel();
            }
        });
        dialogo1.show();
    }// confirma salir

    private void cerrarAplicacion() {
        datosSP = getSharedPreferences("datosAlumno", Context.MODE_PRIVATE);
        editorSP = datosSP.edit();
        // metemos los datos CLAVE VALOR al editor del shared preferences
        editorSP.putBoolean("sesion",false);// se pasa a true en HOME
        editorSP.putInt("idAlum",-1);
        editorSP.putString("usuario","");
        editorSP.putString("pass","");
        editorSP.putString("nombreAlum","");
        editorSP.putString("dniAlum","");
        editorSP.putString("nacimientoAlum","");
        editorSP.putString("tlfAlum","");
        editorSP.putString("emailAlum","");
        editorSP.putInt("idFormaPagoAux",-1);
        editorSP.putString("datosFormaPago","");
        editorSP.putInt("formaPagoConfirm",0);
        editorSP.putInt("bolsaClases",-1);
        editorSP.putInt("clasesPendientes",-1);
        editorSP.putString("fotoAlum","");
        System.out.println(" RESETEA los datos del LOGIN en en el SHAREDPREFERENCES ");
        // guardamos los datos en el editor
        editorSP.apply();
        System.out.println(" cierra la app - FINISH ");
        finish();
        
    }// fin CIERRA APLICACION


    //GET SET

    public SharedPreferences getDatosSP() {
        return datosSP;
    }

    public void setDatosSP(SharedPreferences datosSP) {
        this.datosSP = datosSP;
    }


}