package com.example.chuti.test;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {
    private TextView textView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.TextView01);

        //run every seconds
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override public void run() {
                readWebpage();
            }
        }, 0, 1000);
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                httpGet.setHeader("Cookie", "AIROS_SESSIONID=52fb8930fe14a09dc50e651f0fd87971; path=/; domain=192.168.1.20; Expires=Tue Jan 19 2038 03:14:07 GMT+0700 (SE Asia Standard Time);");
                try {

                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(
                            new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            textView.setText(Html.fromHtml(result));
            Log.v("html", Html.fromHtml(result).toString());
        }
    }

    public void readWebpage () {
        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute("http://192.168.1.20/status.cgi");

    }


}
