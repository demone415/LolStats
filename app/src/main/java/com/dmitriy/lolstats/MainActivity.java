package com.dmitriy.lolstats;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
    boolean DownloadReady = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new DownloadFromUrl().execute(StatusLink);

        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                Status = str;
                jsonGetStatus();
            }
        }, 5500);

 /*       ImageButton reloadStatus = (ImageButton) findViewById(R.id.imageButton);
        reloadStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadStatusfn();
            }
        });*/
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
                ProgressBar pgb = (ProgressBar) findViewById(R.id.progressBar);
                pgb.setIndeterminate(true);
                pgb.setVisibility(View.VISIBLE);
                TextView WaitText = (TextView) findViewById(R.id.WaitText);
                WaitText.setTextColor(getResources().getColor(R.color.gold_text));
                Typeface face = Typeface.createFromAsset(getAssets(),"fonts/BeaufortforLOL-Medium.ttf");
                WaitText.setTypeface(face);
                WaitText.setVisibility(View.VISIBLE);

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

                //DownloadCompletedToast();

            } catch (IOException e) {
                    Log.e("DownloadFromUrl", "Error: " + e);

                ProgressBar pgb = (ProgressBar) findViewById(R.id.progressBar);
                    pgb.setIndeterminate(false);
                    pgb.setVisibility(View.GONE);
                TextView WaitText = (TextView) findViewById(R.id.WaitText);
                    WaitText.setVisibility(View.GONE);

                Typeface face = Typeface.createFromAsset(getAssets(),"fonts/BeaufortforLOL-Medium.ttf");
                TextView text = (TextView) findViewById(R.id.Content1);
                    text.setTextColor(getResources().getColor(R.color.gold_text));
                    text.setVisibility(View.VISIBLE);
                    text.setTypeface(face);
                    text.setText("Download Error");
            }

        }

        @Override
        protected Void doInBackground(String... urls) {
            String url = urls[0];
            try {
                download(url);
                DownloadReady = true;
            } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                e.printStackTrace();

                ProgressBar pgb = (ProgressBar) findViewById(R.id.progressBar);
                    pgb.setIndeterminate(false); ///
                    pgb.setVisibility(View.GONE);  ///
                TextView WaitText = (TextView) findViewById(R.id.WaitText);
                    WaitText.setVisibility(View.GONE); ///

                Typeface face = Typeface.createFromAsset(getAssets(),"fonts/BeaufortforLOL-Medium.ttf");
                TextView text = (TextView) findViewById(R.id.Content1);
                    text.setVisibility(View.VISIBLE);
                    text.setTextColor(getResources().getColor(R.color.gold_text));
                    text.setTypeface(face);
                    text.setText("Download Error");
            }
            return null;
        }
    }



    public void jsonGetStatus() {
        try {
                Log.d("String = ",Status);
            JSONObject Jobj = new JSONObject(Status);

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

            /*getting second news message*/

            JSONObject incedents2 = inc.getJSONObject(1);
            String inc3 = incedents2.toString();

                Log.d("in ","incidents_2");

            JSONObject updObj2 = new JSONObject(inc3);
            JSONArray upd2 = updObj2.getJSONArray("updates");
            JSONObject updates_2 = upd2.getJSONObject(0);
            String updatesReady2 = updates_2.toString();

                Log.d("in ","updates_2" + updatesReady2);

            JSONObject content2Obj = new JSONObject(updatesReady2);

            String content_2 = content2Obj.getString("content");

                Log.d("Content2:  ",content_2);


            /*closing waiting message*/

            ProgressBar pgb = (ProgressBar) findViewById(R.id.progressBar);
                pgb.setIndeterminate(false);
                pgb.setVisibility(View.GONE);
            TextView WaitText = (TextView) findViewById(R.id.WaitText);
                WaitText.setVisibility(View.GONE);

            /*filling Content1 TextView with a first news message*/

            Typeface face = Typeface.createFromAsset(getAssets(),"fonts/BeaufortforLOL-Medium.ttf");
            TextView text = (TextView) findViewById(R.id.Content1);
                text.setVisibility(View.VISIBLE);
                text.setTextColor(getResources().getColor(R.color.gold_text));
                text.setTypeface(face);
                text.setText(content1);

            /* Content1 TextView with a second news message*/

                text.append(" \n \n");
                text.append(content_2);

            //TODO Divide jsonGetStatus into 3 functions. 1: GetFirstMessage; 2: GetSecondMessage; 3: GetOnline (new);

        } catch (JSONException e) {
                Log.e("Array", "Error: " + e);

            ProgressBar pgb = (ProgressBar) findViewById(R.id.progressBar);
                pgb.setIndeterminate(false);
                pgb.setVisibility(View.GONE);
            TextView WaitText = (TextView) findViewById(R.id.WaitText);
                WaitText.setVisibility(View.GONE);

            Typeface face = Typeface.createFromAsset(getAssets(),"fonts/BeaufortforLOL-Medium.ttf");
            TextView text = (TextView) findViewById(R.id.Content1);
                text.setVisibility(View.VISIBLE);
                text.setTextColor(getResources().getColor(R.color.gold_text));
                text.setTypeface(face);
                text.setText("No messages for now :(");
        }
                Log.d("Parsing","Complete");



    }
























}


