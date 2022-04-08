package com.example.kovacskrisztian_restapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.kovacskrisztian_restapi.databinding.ActivityListResultBinding;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListResultActivity extends AppCompatActivity {

    ActivityListResultBinding binding;

    List<City> cities = new ArrayList<>();
    private String URL = "https://retoolapi.dev/7pRtfg/orszagok";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CityTask cityTask = new CityTask();
        cityTask.execute();

        binding.listVisszaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private class CityTask extends AsyncTask<Void, Void, Response> {
        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            try {
                response = RequestHandler.get(URL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            Gson converter = new Gson();
            if (response == null || response.getResponseCode() >= 400){
                Toast.makeText(ListResultActivity.this, "Hiba történt a kérés feldolgozása során.", Toast.LENGTH_SHORT).show();
            }
            else {
                City[] locations = converter.fromJson(response.getContent(), City[].class);
                cities.clear();
                cities.addAll(Arrays.asList(locations));
                ArrayAdapter<City> arrayAdapter = new ArrayAdapter<>(ListResultActivity.this, R.layout.list_item, R.id.itemTextView, cities);
                binding.orszagokListView.setAdapter(arrayAdapter);
            }
        }
    }
}