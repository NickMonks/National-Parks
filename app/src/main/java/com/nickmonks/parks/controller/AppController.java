package com.nickmonks.parks.controller;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

// Remember: App controller needs to extend application, which needs to be defined in the manifest.xml
// A subclass of Application will be instantiated at the beginning of the application, and will hold
// the state of the entire application. This is useful for singleton application such as APp controllers
public class AppController extends Application {
    private static AppController instance;
    private RequestQueue requestQueue;

    public static synchronized AppController getInstance(){
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) { getRequestQueue().add(req);}

    // IMPORTANT: WE NEED TO OVERRIDE THE METHOD AND ASIGN THE INSTANCE TO THIS
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
