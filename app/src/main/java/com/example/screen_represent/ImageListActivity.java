package com.example.screen_represent;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImageListActivity extends AppCompatActivity {

    private RecyclerView imageRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        imageRecyclerView = (RecyclerView) findViewById(R.id.imageListRecyclerView);
        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch images from API and display them in RecyclerView
        new FetchImagesTask().execute();
    }


    private class FetchImagesTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> imageUrls = new ArrayList<>();

            try {
                // Simulate API call to fetch image URLs
                String apiUrl = "https://jsonplaceholder.typicode.com/photos";
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                StringBuilder response = new StringBuilder();
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse JSON response to extract image URLs
                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String imageUrl = jsonObject.getString("url");
                    imageUrls.add(imageUrl);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return imageUrls;
        }

        @Override
        protected void onPostExecute(List<String> imageUrls) {
            // Update RecyclerView with fetched image URLs
            ImageAdapter imageAdapter = new ImageAdapter(imageUrls);
            imageRecyclerView.setAdapter(imageAdapter);
        }
    }
}

