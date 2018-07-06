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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView changingText;
    FloatingActionButton fab;
    String url = "https://api.openweathermap.org/data/2.5/forecast?id=745044&appid=4509d4e4fe84d0523805a73785201aae";
    private OkHttpClient client;
    TextView text, text2, text3, text4, text5, text6;
    ImageView image1, image2, image3;
    Button button;
    String key, key2, key3;
    String urlImage = "https://openweathermap.org/img/w/";
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
        image2 = findViewById(R.id.image_2);
        image3 = findViewById(R.id.image_3);

        button = findViewById(R.id.button);

        client = new OkHttpClient();
        changeText();


}

    private void changeText() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "My action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changingText.setText("Three Day Weather Forcast");

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

                            //text
                            JSONObject jsonObject = new JSONObject(tempResponse);
                            JSONObject jsonObject2 = new JSONObject(tempResponse);
                            JSONObject jsonObject3 = new JSONObject(tempResponse);

                            //image
                            JSONObject jsonObjectImage = new JSONObject(tempResponse);
                            JSONObject jsonObjectImage2 = new JSONObject(tempResponse);
                            JSONObject jsonObjectImage3 = new JSONObject(tempResponse);

                            //time
                            JSONObject jsonObjectTime = new JSONObject(tempResponse);
                            JSONObject jsonObjectTime2 = new JSONObject(tempResponse);
                            JSONObject jsonObjectTime3 = new JSONObject(tempResponse);

                            //text
                            JSONArray jsonArray = new JSONArray (jsonObject.getString("list"));
                            JSONArray jsonArray2 = new JSONArray (jsonObject2.getString("list"));
                            JSONArray jsonArray3 = new JSONArray (jsonObject3.getString("list"));

                            //image
                            JSONArray jsonArrayImage = new JSONArray(jsonObjectImage.getString("list"));
                            JSONArray jsonArrayImage2 = new JSONArray(jsonObjectImage2.getString("list"));
                            JSONArray jsonArrayImage3 = new JSONArray(jsonObjectImage3.getString("list"));

                            //time
                            JSONArray jsonArrayTime = new JSONArray (jsonObjectTime.getString("list"));
                            JSONArray jsonArrayTime2 = new JSONArray (jsonObjectTime2.getString("list"));
                            JSONArray jsonArrayTime3 = new JSONArray (jsonObjectTime3.getString("list"));


                            //text
                            JSONObject array = new JSONObject(jsonArray.getString(0));
                            JSONObject array2 = new JSONObject(jsonArray2.getString(8));
                            JSONObject array3 = new JSONObject(jsonArray3.getString(16));

                            //image
                            JSONObject array4 = new JSONObject(jsonArrayImage.getString(0));
                            JSONObject array5 = new JSONObject(jsonArrayImage2.getString(0));
                            JSONObject array6 = new JSONObject(jsonArrayImage3.getString(0));

                            //image
                            JSONArray arrayImage = new JSONArray(array4.getString("weather"));
                            JSONArray arrayImage2 = new JSONArray(array5.getString("weather"));
                            JSONArray arrayImage3 = new JSONArray(array6.getString("weather"));

                            //time
                            JSONObject timeObject = new JSONObject(jsonArrayTime.getString(0));
                            JSONObject timeObject2 = new JSONObject(jsonArrayTime2.getString(8));
                            JSONObject timeObject3 = new JSONObject(jsonArrayTime3.getString(16));

                            //text
                            JSONObject weatherData = new JSONObject(array.getString("main"));
                            JSONObject weatherData2 = new JSONObject(array2.getString("main"));
                            JSONObject weatherData3 = new JSONObject(array3.getString("main"));

                            //image
                            JSONObject weatherPicture = new JSONObject(arrayImage.getString(0));
                            JSONObject weatherPicture2 = new JSONObject(arrayImage2.getString(0));
                            JSONObject weatherPicture3 = new JSONObject(arrayImage3.getString(0));

                            //time
                            String dateStr = timeObject.getString("dt_txt");
                            String dateStr2 = timeObject2.getString("dt_txt");
                            String dateStr3 = timeObject3.getString("dt_txt");

                            //text
                            Double temperature = Double.parseDouble(weatherData.getString("temp"));
                            Double temperature2 = Double.parseDouble(weatherData2.getString("temp"));
                            Double temperature3 = Double.parseDouble(weatherData3.getString("temp"));

                            //image
                            key = weatherPicture.getString("icon");
                            key2 = weatherPicture2.getString("icon");
                            key3 = weatherPicture3.getString("icon");

                            //time1
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date dateObj = formatter.parse(dateStr);
                            SimpleDateFormat postFormatter = new SimpleDateFormat("EEE, MMMM dd");
                            String newDateStr = postFormatter.format(dateObj);

                            //time2
                            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date dateObj2 = formatter2.parse(dateStr2 );
                            SimpleDateFormat postFormatter2 = new SimpleDateFormat("EEE, MMMM dd");
                            String newDateStr2 = postFormatter2.format(dateObj2);

                            //time3
                            SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date dateObj3 = formatter3.parse(dateStr3);
                            SimpleDateFormat postFormatter3 = new SimpleDateFormat("EEE, MMMM dd");
                            String newDateStr3 = postFormatter3.format(dateObj3);

                            int temperatureInt = (int) (temperature - 273.15);
                            int temperatureInt2 = (int) (temperature2 - 273.15);
                            int temperatureInt3 = (int) (temperature3 - 273.15);

                            //city
                            JSONObject cityData = new JSONObject(jsonObject.getString("city"));
                            String placeName = cityData.getString("name");

                            text.setText(newDateStr);
                            text2.setText(String.valueOf(temperatureInt + "℃"));
                            text3.setText(newDateStr2);
                            text4.setText(String.valueOf(temperatureInt2 + "℃"));
                            text5.setText(newDateStr3);
                            text6.setText(String.valueOf(temperatureInt3 + "℃"));


                            StringBuilder sb = new StringBuilder(urlImage);
                            StringBuilder sb2 = new StringBuilder(urlImage);
                            StringBuilder sb3 = new StringBuilder(urlImage);

                            sb.append(String.format(key+".png"));
                            sb2.append(String.format(key2+".png"));
                            sb3.append(String.format(key3+".png"));

                            GlideApp.with(context).load(sb.toString()).into(image1);
                            GlideApp.with(context).load(sb2.toString()).into(image2);
                            GlideApp.with(context).load(sb3.toString()).into(image3);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
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
        changingText.setText("Welcome Back!");

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
