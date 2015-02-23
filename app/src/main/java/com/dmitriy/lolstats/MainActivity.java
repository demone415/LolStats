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

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends Activity {

    String str = "";
    //private final String PATH = "/data/data/com.dmitriy.lolstats/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView text = (TextView) findViewById(R.id.String);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadFromUrl().execute("http://status.leagueoflegends.com/shards/ru");
            }
        });

        Button viewJ = (Button) findViewById(R.id.button2);
        viewJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getString();
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

    public void getString() {
        TextView text = (TextView) findViewById(R.id.String);
        text.setText(str);
    /*
      Reader reader = null;
      reader = new InputStreamReader(is, "UTF-8");
      char[] buffer = new char[10000];
      reader.read(buffer);
      str = new String(buffer); */
    }

    public class DownloadFromUrl extends AsyncTask<String, Void, Void> {

        Handler handler = new Handler();

        public void DownloadCompletedToast() {
            handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(MainActivity.this,
                            "Download Completed", Toast.LENGTH_SHORT).show();
                }
            });
        }


        public void download(String imageURL) {
            try {
                URL url = new URL(imageURL);
                // File file = new File("Statuss.json");

                //File file = File.createTempFile("txt", ".json");


                URLConnection ucon = url.openConnection();

                InputStream is = ucon.getInputStream();
              //  BufferedInputStream bis = new BufferedInputStream(is);

            /*    ByteArrayBuffer baf = new ByteArrayBuffer(50);
                int current = 0;
                while ((current = bis.read()) != -1) {
                    baf.append((byte) current);
                }

                */
                Reader reader = null;
                reader = new InputStreamReader(is, "UTF-8");
                char[] buffer = new char[10000];
                reader.read(buffer);
                str = new String(buffer);

                Log.d("DownloadFromUrl","Before writing: " + str);

                FileOutputStream fOut = openFileOutput("status.json",
                        MODE_WORLD_READABLE);
                OutputStreamWriter osw = new OutputStreamWriter(fOut);

                // Write the string to the file
                osw.write(str);
                osw.flush();
                osw.close();

                Log.d("DownloadFromUrl","After writing: " + str);




             /* FileOutputStream fos = new FileOutputStream(file);
              fos.write(baf.toByteArray());
              fos.close(); */
                Log.d("DownloadFromUrl", "download completed");
                DownloadCompletedToast();


            } catch (IOException e) {
                Log.d("DownloadFromUrl", "Error: " + e);
            }

        }

        @Override
        protected Void doInBackground(String... urls) {

            String urldisplay = urls[0];
            try {
                download(urldisplay);


            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }


            return null;
        }


    }
}


