package com.dmitriy.lolstats;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends Activity {


    String StatusLink = "http://status.leagueoflegends.com/shards/ru";

    String str = "";
    String Status = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button getStatusButton = (Button) findViewById(R.id.button);
        getStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadFromUrl().execute(StatusLink);
                Status = str;
            }
        });

        Button parseStatusButton = (Button) findViewById(R.id.button2);
        parseStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
        public  void  onClick(View v) {
                jsonGetStatus();
        }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // "http://status.leagueoflegends.com/shards/ru"


    // Downloader class with AsyncTask. Because honeycomb+ request this shit
    public class DownloadFromUrl extends AsyncTask<String, Void, Void> {

        //new Handler to show Toast within background process
        Handler handler = new Handler();
        public void DownloadCompletedToast() {
            handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(MainActivity.this,
                            "Download Completed", Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void download(String Address) {
            try {
                URL url = new URL(Address);
                URLConnection ucon = url.openConnection();
                InputStream is = ucon.getInputStream();
                Reader reader = null;
                reader = new InputStreamReader(is, "UTF-8");
                char[] buffer = new char[10000];
                reader.read(buffer);
                str = new String(buffer);
                Log.d("DownloadFromUrl","Before writing: " + str);
                FileOutputStream fOut = openFileOutput("temp.json",
                        MODE_WORLD_READABLE);
                OutputStreamWriter osw = new OutputStreamWriter(fOut);
                osw.write(str);
                osw.flush();
                osw.close();
                Log.d("DownloadFromUrl", "Download completed");

                DownloadCompletedToast();
            } catch (IOException e) {
                Log.e("DownloadFromUrl", "Error: " + e);
            }

        }

        @Override
        protected Void doInBackground(String... urls) {
            String url = urls[0];
            try {
                download(url);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
    }



    public void jsonGetStatus() {
        try {
            Log.d("String = ",Status);
            JSONObject Jobj = new JSONObject(str);

            JSONArray services = Jobj.getJSONArray("services");
            JSONObject incidents = services.getJSONObject(1);
            String incident2 = incidents.toString();

            Log.d("in ","services");

            JSONObject IncObj = new JSONObject(incident2);
            JSONArray inc = IncObj.getJSONArray("incidents");
            JSONObject incidents2 = inc.getJSONObject(0);
            String inc2 = incidents2.toString();

            Log.d("in ","incidents");

            JSONObject updObj = new JSONObject(inc2);
            JSONArray upd = updObj.getJSONArray("updates");
            JSONObject updates = upd.getJSONObject(0);
            String updatesReady = updates.toString();

            Log.d("in ","updates" + updatesReady);


            JSONObject content1Obj = new JSONObject(updatesReady);

            String content1 = content1Obj.getString("content");

            Log.d("Content1:  ",content1);


           TextView text = (TextView) findViewById(R.id.textView);
           text.setText(content1);

        } catch (JSONException e) {
            Log.e("Array", "Error: " + e);
            TextView text = (TextView) findViewById(R.id.textView);
            text.setText("No messages for now :(");
        }
        Log.d("Parsing","Complete");



    }
























}


