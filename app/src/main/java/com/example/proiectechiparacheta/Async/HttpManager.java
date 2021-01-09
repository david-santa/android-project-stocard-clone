package com.example.proiectechiparacheta.Async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

public class HttpManager implements Callable<Bitmap> {
    //clase utilitare pentru conexiunea la retea
    private URL url;
    private HttpURLConnection connection;

    private final String urlAddress;

    public HttpManager(String urlAddress) {
        this.urlAddress = urlAddress;
    }

    @Override
    public Bitmap call() {
        try {
            return getResultFromHttp();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //lucrand cu clase de tip .net ar trebui sa asiguram de fiecare data inchiderea acestora,
            //pentru a evite aparitia de memory leak
            closeConnections();
        }
        return null;
    }

    public Bitmap getResultFromHttp() throws IOException {
        InputStream in = new java.net.URL(urlAddress).openStream();
        Bitmap result = BitmapFactory.decodeStream(in);
        in.close();
        return result;
    }

    private void closeConnections() {
        //connection.disconnect();
    }
}