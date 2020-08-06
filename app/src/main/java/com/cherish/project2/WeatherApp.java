package com.cherish.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class WeatherApp extends AppCompatActivity {
    EditText editText;
    TextView rain;
    String message = "could not find weather, please try again";




    public  void getWeather(View view){
        Toast.makeText(this, editText.getText().toString(),Toast.LENGTH_LONG).show();
        try{

        DownloadData download = new DownloadData();

        download.execute("https://openweathermap.org/data/2.5/weather?q="+editText.getText().toString()+"&appid=b6907d289e10d714a6e88b30761fae22").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        InputMethodManager manager = (InputMethodManager) getSystemService(WeatherApp.this.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(editText.getWindowToken(),0);



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_app);
        editText = (EditText) findViewById(R.id.editText);
        rain = findViewById(R.id.rain);




    }

    public  class DownloadData extends AsyncTask<String,Void,String> {

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url ;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data!= -1){
                    char current = (char) data;
                    result += current;
                    data= reader.read();
                }
                return  result;

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("message",weatherInfo);

                JSONArray arr = new JSONArray(weatherInfo);
                for (int i = 0; i <arr.length(); i++){
                    JSONObject object = arr.getJSONObject(i);
                    Log.i("main",object.getString("main"));
                    Log.i("description", object.getString("description"));
                    String main = object.getString("main");
                    String descrition = object.getString("description");


                    if (!main.equals("") && !descrition.equals("")){
                        rain.setText(main + " : " + descrition + "\r\n");
                    }else {
                        rain.setText("");
                    }

                }
                editText.setText("");

            }catch (Exception e){
                e.printStackTrace();
                rain.setText("Could not find weather, please try again");
            }

//            Log.i("Json Message", s);
        }
    }
}
