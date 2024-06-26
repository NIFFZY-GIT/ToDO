package niffzy.todo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherActivity extends AppCompatActivity {

    TextView cityName;
    Button search;
    TextView show;
    String apiKey = "cfa66686c518a7b9c699e90928500194";
    AlertDialog.Builder builder; // Declare AlertDialog.Builder

    class GetWeather extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray weatherArray = jsonObject.getJSONArray("weather");
                JSONObject weatherObject = weatherArray.getJSONObject(0);
                String weatherDescription = weatherObject.getString("description");
                String weatherMain = weatherObject.getString("main");

                JSONObject mainObject = jsonObject.getJSONObject("main");
                double temperature = mainObject.getDouble("temp") - 273.15; // Convert from Kelvin to Celsius

                String weatherInfo = "Weather: " + weatherMain + "\nDescription: " + weatherDescription + "\nTemperature: " + String.format("%.2f", temperature) + "°C";
                show.setText(weatherInfo);

                if (weatherMain.equalsIgnoreCase("Clear") || weatherMain.equalsIgnoreCase("Clouds")) {
                    // Initialize builder if not initialized
                    if (builder == null) {
                        builder = new AlertDialog.Builder(WeatherActivity.this);
                    }

                    builder.setTitle("Weather Update")
                            .setMessage("The weather is good! You can carry out your task today.")
                            .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Your action when OKAY button is clicked
                                }
                            })
                            .show();

//                    Toast.makeText(WeatherActivity.this, "The weather is good! You can carry out your task today.", Toast.LENGTH_LONG).show();
                } else {
                    builder.setTitle("Weather Update")
                            .setMessage("The weather is bad. Try tomorrow.")
                            .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Your action when OKAY button is clicked
                                }
                            })
                            .show();
//                    Toast.makeText(WeatherActivity.this, "The weather is bad. Try tomorrow.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                show.setText("Failed to parse weather data.");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        cityName = findViewById(R.id.cityName);
        search = findViewById(R.id.search);
        show = findViewById(R.id.weather);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WeatherActivity.this, "Loading....", Toast.LENGTH_SHORT).show();
                String city = cityName.getText().toString();
                if (city != null && !city.isEmpty()) {
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
                    GetWeather task = new GetWeather();
                    task.execute(url);
                } else {
                    // Show an alert dialog if the city is empty
                    new AlertDialog.Builder(WeatherActivity.this)
                            .setTitle("Alert")
                            .setMessage("Please enter a city name :).")
                            .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Your action when OKAY button is clicked
                                }
                            })
                            .show();
                }
            }
        });
    }
}
//https://www.youtube.com/watch?v=f2oSRBwN2HY&ab_channel=SandipBhattacharya