package com.example.pruebalista.ui.login;

import android.app.ProgressDialog;

import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.pruebalista.baseDatosVolley.VolleySingleton;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private Integer error;

    LoginResult(@Nullable Integer error) {
        System.out.println("LOGIN RESULT ERROR");
        this.error = error;
    }

    LoginResult(@Nullable LoggedInUserView success) {
        System.out.println("LOGIN RESULT SUCCESS");
        cargarWebServiceLogin();
        this.success = success;
    }

    private void cargarWebServiceLogin() {
    }// fin metodo carga web service login

    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}