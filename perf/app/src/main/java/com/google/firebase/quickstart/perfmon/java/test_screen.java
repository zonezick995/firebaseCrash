package com.google.firebase.quickstart.perfmon.java;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.google.firebase.quickstart.perfmon.databinding.TestScreenBinding;

import java.util.Random;
import java.util.concurrent.CountDownLatch;


public class test_screen extends AppCompatActivity {

    private TestScreenBinding binding;
    private Trace mTrace;
    private String STARTUP_TRACE_NAME = "startup_trace";
    private CountDownLatch mNumStartupTasks = new CountDownLatch(2);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= TestScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(test_screen.this, com.google.firebase.quickstart.perfmon.java.MainActivity.class);
                startActivities(new Intent[]{intent});
                mTrace.stop();
                Toast.makeText(test_screen.this, "Trace Stop",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Begin tracing app startup tasks.
        mTrace = FirebasePerformance.getInstance().newTrace(STARTUP_TRACE_NAME);
        mTrace.start();


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://corona-virus-stats.herokuapp.com/api/v1/cases/countries-search?search="+getRandomString(2)+"";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        binding.textViewContent.setText("Response is: "+ response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.textViewContent.setText("That didn't work!");
            }
        });
        queue.add(stringRequest);
        // End request
    }
    private String getRandomString(int length) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }
}

