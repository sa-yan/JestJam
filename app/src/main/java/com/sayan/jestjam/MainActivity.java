package com.sayan.jestjam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.ReferenceQueue;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    Button refreshBtn;
    TextView  setupLine, punchLine;

    Dialog dialog;
    String[] types={"Programming","Miscellaneous","Pun","Spooky", "Christmas", "Any"};

    String url ="https://v2.jokeapi.dev/joke/Any?blacklistFlags=nsfw,religious,political,racist,sexist,explicit&type=twopart";

    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner=findViewById(R.id.spinner);
        refreshBtn = findViewById(R.id.refresh);
        setupLine=findViewById(R.id.setupJoke);
        punchLine=findViewById(R.id.punchLineJoke);

        queue= Volley.newRequestQueue(getApplicationContext());


        // To hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Get spinner item
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loadJoke();
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = types[position];
                //Load Joke
                loadJokeCategorywise(text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                loadJokeCategorywise("Programming");
            }
        });


        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadJoke();
            }
        });


    }

    void loadJoke(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.has("setup") && response.has("delivery")){
                                setupLine.setText(response.getString("setup"));
                                punchLine.setText(response.getString("delivery"));
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", error.toString());

                    }
                });

        queue.add(jsonObjectRequest);

    }

    void loadJokeCategorywise(String category){
        url = "https://v2.jokeapi.dev/joke/"+category+"?blacklistFlags=nsfw,religious,political,racist,sexist,explicit&type=twopart";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.has("setup") && response.has("delivery")){
                                setupLine.setText(response.getString("setup"));
                                punchLine.setText(response.getString("delivery"));
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", error.toString());

                    }
                });

        queue.add(jsonObjectRequest);
    }



}