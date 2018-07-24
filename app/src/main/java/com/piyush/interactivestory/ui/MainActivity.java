package com.piyush.interactivestory.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.piyush.interactivestory.R;
import com.piyush.interactivestory.nsd.NSDHelper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private EditText nameInputText;
    private static final String TAG = MainActivity.class.getSimpleName();
    private NSDHelper nsdHelper;

    OkHttpClient client = new OkHttpClient();

    public String url = "http://192.168.4.1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = findViewById(R.id.buttonTextView);
        nameInputText = findViewById(R.id.nameTextView);
        nsdHelper = new NSDHelper(this);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameInputText.getText().toString();
                Toast.makeText(MainActivity.this, name, Toast.LENGTH_LONG).show();
                showStoryActivity(name);
                nsdHelper.discoverServices();
            }
        });

    }

    private void showStoryActivity(String name) {
        Intent intent = new Intent(MainActivity.this, StoryActivity.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }
}
    class OkHttpHandler extends AsyncTask {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Object doInBackground(Object[] objects) {
            Request.Builder builder = new Request.Builder();
            builder.url((String)objects[0]);
            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }
