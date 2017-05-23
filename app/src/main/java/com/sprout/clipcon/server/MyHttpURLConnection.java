package com.sprout.clipcon.server;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by delf on 17-05-18.
 */

public class MyHttpURLConnection extends HttpURLConnection {
    /**
     * Constructor for the HttpURLConnection.
     * @param u the URL
     */
    protected MyHttpURLConnection(URL u) {
        super(u);
    }

    @Override
    public void disconnect() {
        Log.d("delf", "disconnect() in MyHttpURLConnection is called");
    }

    @Override
    public boolean usingProxy() {
        Log.d("delf", "usingProxy() in MyHttpURLConnection is called");
        return false;
    }

    @Override
    public void connect() throws IOException {
    }
}
