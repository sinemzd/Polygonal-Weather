package com.example.sinemdalak.weatherforecasting;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    int counter = 0;
    TextView changingText;
    FloatingActionButton fab;
    String url = "https://api.openweathermap.org/data/2.5/forecast?id=745044&appid=4509d4e4fe84d0523805a73785201aae";
    private OkHttpClient client;
    TextView text;
    TextView text2;
    TextView text3;
    TextView text4;
    TextView text5;
    TextView text6;
    ImageView image1;
    String key;
    String urlImage = "http://openweathermap.org/img/w/" ;
    private Context context = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        changingText = findViewById(R.id.text_to_change);

        text = findViewById(R.id.text_1);
        text2 = findViewById(R.id.text_2);
        text3 = findViewById(R.id.text_3);
        text4 = findViewById(R.id.text_4);
        text5 = findViewById(R.id.text_5);
        text6 = findViewById(R.id.text_6);

        image1 = findViewById(R.id.image_1);

        client = new OkHttpClient();
        changeText();

}

    private void changeText() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "My action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                counter++;
                changingText.setText(counter + "Rainy");

                getWebService();
            }
        });

    }


    private void getWebService() {
        Request request = new Request.Builder()
                .url(url).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setText("Failure");
                        text2.setText("Failure");
                        text3.setText("Failure");
                        text4.setText("Failure");
                        text5.setText("Failure");
                        text6.setText("Failure");
                    }
                });

            }

            @Override
            public void onResponse(Call call,  Response response) throws IOException {
                final String tempResponse=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {


                            JSONObject jsonObject = new JSONObject(tempResponse);
                            JSONObject jsonObject2 = new JSONObject(tempResponse);
                            JSONObject jsonObject3 = new JSONObject(tempResponse);

                            JSONObject jsonObjectimage = new JSONObject(tempResponse);

                            JSONArray jsonArray = new JSONArray((jsonObject.getString("list")));
                            JSONArray jsonArray2 = new JSONArray((jsonObject2.getString("list")));
                            JSONArray jsonArray3 = new JSONArray((jsonObject3.getString("list")));

                            JSONArray jsonArrayImage = new JSONArray(jsonObjectimage.getString("list"));


                            JSONObject array = new JSONObject(jsonArray.getString(0));
                            JSONObject array2 = new JSONObject(jsonArray2.getString(8));
                            JSONObject array3 = new JSONObject(jsonArray3.getString(16));

                            JSONObject array4 = new JSONObject(jsonArrayImage.getString(0));

                            JSONArray arrayImage = new JSONArray(array4.getString("weather"));


                            JSONObject weatherData = new JSONObject(array.getString("main"));
                            JSONObject weatherData2 = new JSONObject(array2.getString("main"));
                            JSONObject weatherData3 = new JSONObject(array3.getString("main"));

                            JSONObject weatherPicture = new JSONObject(arrayImage.getString(0));

                            Double temperature = Double.parseDouble(weatherData.getString("temp"));
                            Double temperature2 = Double.parseDouble(weatherData2.getString("temp"));
                            Double temperature3 = Double.parseDouble(weatherData3.getString("temp"));

                            key = weatherPicture.getString("icon");

                            int temperatureInt = (int) (temperature - 273.15);
                            int temperatureInt2 = (int) (temperature2 - 273.15);
                            int temperatureInt3 = (int) (temperature3 - 273.15);

                            JSONObject cityData = new JSONObject(jsonObject.getString("city"));
                            String placeName = cityData.getString("name");
                            text.setText(placeName);
                            text2.setText(String.valueOf(temperatureInt));
                            text3.setText(placeName);
                            text4.setText(String.valueOf(temperatureInt2));
                            text5.setText(placeName);
                            text6.setText(String.valueOf(temperatureInt3));


                            StringBuilder sb = new StringBuilder(urlImage);
                            sb.append(String.format(key+".png"));

                            Glide.with(context).load(sb.toString()).into(image1);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        counter += counter;
        changingText.setText(counter + " Sinem");

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
}
