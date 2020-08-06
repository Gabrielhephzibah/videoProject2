package com.cherish.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> celebURLs =new ArrayList<String>();
    ArrayList<String>celeNames = new ArrayList<String>();
    int chosenCeleb = 0;
    Button nextPage;
    ImageView image;
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    String[] answers = new String[4];
    int locationOfCorrectAnswer = 0;


    public  void  next(View view){
        Intent next = new Intent(getApplicationContext(),WeatherApp.class);
        startActivity(next);

    }

    public  void  clickedCelebrity(View view){
        if (view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer)))
        {
            Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"Wrong,it was"+ celeNames.get(chosenCeleb),Toast.LENGTH_SHORT).show();
        }
        newQuestion();
    }

    public  class ImageDownloader extends AsyncTask<String, Void , Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                        Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                        return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public class  downloader extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in =  urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data!= -1){
                    char current = (char) data;
                    result+=current;
                    data = reader.read();
                }
                return result;

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }


        }
    }

    public  void newQuestion(){
        try {
            Random rand = new Random();
            chosenCeleb = rand.nextInt(celebURLs.size());
            ImageDownloader imageTask = new ImageDownloader();
            Bitmap celebImage = imageTask.execute(celebURLs.get(chosenCeleb)).get();
            image.setImageBitmap(celebImage);
            locationOfCorrectAnswer = rand.nextInt(4);
            int incorrectAnswersLocation;

            for (int i = 0;i<4;i++){
                if (i==locationOfCorrectAnswer){
                    answers[i]= celeNames.get(chosenCeleb);
                }else {
                    incorrectAnswersLocation = rand.nextInt(celebURLs.size());
                    while (incorrectAnswersLocation==chosenCeleb){
                        incorrectAnswersLocation = rand.nextInt(celebURLs.size());
                    }
                    answers[i]=celeNames.get(incorrectAnswersLocation);
                }
            }

            button0.setText(answers[0]);
            button1.setText(answers[1]);
            button2.setText(answers[2]);
            button3.setText(answers[3]);






        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.image);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        nextPage = findViewById(R.id.nextPage);

        downloader task = new downloader();
        String result = null;

//        try {
//           result =task.execute("http://www.posh24.se/kandisar").get();
//           String[] splitResult = result.split("<div class = \"listedArticles\">");
//            Log.i("my message",result);
//            Pattern p = Pattern.compile("img src=\"(.*?)\"");
//            Matcher m = p.matcher(splitResult[0]);
//            while (m.find()){
//                celebURLs.add(m.group(1));
//            }
//
//            p= Pattern.compile("alt=\"(.*?)\"");
//            m = p.matcher(splitResult[0]);
//            while (m.find()){
//               celeNames.add(m.group(1));
//            }
//
//            newQuestion();
//
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }

    }
}
