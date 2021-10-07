package com.example.pruebalista.baseDatosVolley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton mVolleySingleton = null;
    private static Context ctx;
    //Este objeto es la cola que usará la aplicación
    private RequestQueue mRequestQueue;

    private VolleySingleton(Context context) {

        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static VolleySingleton getInstance(Context context) {
        if (mVolleySingleton == null) {
            mVolleySingleton = new VolleySingleton(context);
        }
        return mVolleySingleton;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            System.out.println("M REQUEST QUEUE VACIO");
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }

        System.out.println("GET REQUEST QUEUE");
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }
}// fin clase Volleys
