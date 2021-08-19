package com.example.spinner;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String item;
    public void addCountry(String country){
        countries.add(country);
    }

    ArrayList<String> countries = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addCountry("");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://restcountries.eu/rest/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TextView nameTXT = (TextView) findViewById(R.id.nameText);
        TextView populationTXT = (TextView) findViewById(R.id.populationText);
        TextView regionTXT = (TextView) findViewById(R.id.regionText);
        TextView subregionTXT = (TextView) findViewById(R.id.subregionText);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        JsonPlaceHolderApi jsonPlaceHolderApi1 = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<API>> call1 = jsonPlaceHolderApi1.getAllPosts();

        call1.enqueue(new Callback<List<API>>() {
            @Override
            public void onResponse(Call<List<API>> call, Response<List<API>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), response.code(),
                            Toast.LENGTH_SHORT).show();
                }
                List<API> posts = response.body();
                countries.clear();
                for(API country:posts){
                    addCountry(country.getName());
                }

            }
            @Override
            public void onFailure(Call<List<API>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = (String)parent.getItemAtPosition(position);
                nameTXT.setText(item);

                JsonPlaceHolderApi jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi.class);
                Call<List<API>> call2 = jsonPlaceHolderApi2.getAllPosts();

                call2.enqueue(new Callback<List<API>>() {
                    @Override
                    public void onResponse(Call<List<API>> call, Response<List<API>> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(getApplicationContext(), response.code(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        List<API> posts = response.body();
                        for(API post:posts){
                            if(nameTXT.getText().equals(post.getName())) {
                                populationTXT.setText(Integer.toString(post.getPopulation()));
                                regionTXT.setText(post.getRegion());
                                subregionTXT.setText(post.getSubRegion());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<API>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }
}