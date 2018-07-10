package com.example.sinemdalak.weatherforecasting;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sinemdalak.weatherforecasting.model.Example;
import java.text.ParseException;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    TextView changingText;
    FloatingActionButton fab;
    //String url = "https://api.openweathermap.org/data/2.5/forecast?id=745044&appid=4509d4e4fe84d0523805a73785201aae";
    TextView text, text2, text3, text4, text5, text6, txtCity;
    ImageView image1, image2, image3;
    Button button;
    String urlImage = "https://openweathermap.org/img/w/";
    private Context context ;
    ApiInterface apiService;
    Example example;
    int temperatureInteger, temperatureInteger2, temperatureInteger3;
    String newDateStr, newDateStr2, newDateStr3;
    String icon, icon2, icon3;
    StringBuilder sb, sb2, sb3;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;
    String lattitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=getApplication();
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

        txtCity = findViewById(R.id.text_city);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        },REQUEST_LOCATION);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Example> call;
        call = apiService.getExampleResponse("745044","4509d4e4fe84d0523805a73785201aae");
        call.enqueue(new Callback<Example>() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call call, retrofit2.Response response) {
                Log.d("Response :", response.body().toString());
                example = (Example) response.body();
                Log.d("Example :", response.body().toString());

                double temperature = ((example.getList().get(0).getMain().getTemp()) - 273.15);
                double temperature2 = ((example.getList().get(8).getMain().getTemp()) - 273.15);
                double temperature3 = ((example.getList().get(16).getMain().getTemp()) - 273.15);

                temperatureInteger = (int) temperature;
                temperatureInteger2 = (int) temperature2;
                temperatureInteger3 = (int) temperature3;

                String time = example.getList().get(0).getDtTxt();
                String time2 = example.getList().get(8).getDtTxt();
                String time3 = example.getList().get(16).getDtTxt();

                //time1
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = null;
                try {
                    date = formatter.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat postFormatter = new SimpleDateFormat("EEE, MMMM dd");
                newDateStr = postFormatter.format(date);

                //time2
                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date2 = null;
                try {
                    date2 = formatter2.parse(time2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat postFormatter2 = new SimpleDateFormat("EEE, MMMM dd");
                newDateStr2 = postFormatter2.format(date2);

                //time3
                SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date3 = null;
                try {
                    date3 = formatter3.parse(time3);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat postFormatter3 = new SimpleDateFormat("EEE, MMMM dd");
                newDateStr3 = postFormatter3.format(date3);

                icon = example.getList().get(0).getWeather().get(0).getIcon();
                icon2 = example.getList().get(8).getWeather().get(0).getIcon();
                icon3 = example.getList().get(16).getWeather().get(0).getIcon();

                sb = new StringBuilder(urlImage);
                sb2 = new StringBuilder(urlImage);
                sb3 = new StringBuilder(urlImage);

                sb.append(String.format(icon + ".png"));
                sb2.append(String.format(icon2 + ".png"));
                sb3.append(String.format(icon3 + ".png"));

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("Error service :", t.toString());
            }
        });

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

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    buildAlertMessageNoGps();
                }else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    getLocation();
                }

                txtCity.setText(lattitude+" "+longitude);

                changingText.setText("Three Day Weather Forcast");
                text.setText(newDateStr);
                text2.setText(temperatureInteger + "℃");
                text3.setText(newDateStr2);
                text4.setText(temperatureInteger2 + "℃");
                text5.setText(newDateStr3);
                text6.setText(temperatureInteger3 + "℃");

                GlideApp.with(context).load(sb.toString()).into(image1);
                GlideApp.with(context).load(sb2.toString()).into(image2);
                GlideApp.with(context).load(sb3.toString()).into(image3);



            }
        });

    }

    private void getLocation(){
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },REQUEST_LOCATION);
        }else{
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(location != null){
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);


            }else{
                Toast.makeText(this,"Unable to trace your location",Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void buildAlertMessageNoGps(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please turn on your GPS connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {

                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.cancel();

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
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
