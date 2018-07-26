package com.example.sinemdalak.weatherforecasting;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sinemdalak.weatherforecasting.model.Example;
import com.example.sinemdalak.weatherforecasting.utils.GlideApp;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Result ";
    TextView text, text2, text3, text4, text5, text6, text7, text8, text9, text10, txtCity;
    ImageView image1, image2, image3, image4, image5, imgbtn, imgbtn2;
    private Context context ;
    ApiInterface apiService;
    Example example;
    int temperatureInteger, temperatureInteger2, temperatureInteger3, temperatureInteger4, temperatureInteger5;
    String newDateStr, newDateStr2, newDateStr3, newDateStr4, newDateStr5;
    String icon, icon2, icon3, icon4, icon5;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;
    String lattitude, longitude;
    Intent intent;
    String receivedData;
    Typeface typeface;
    RelativeLayout layout, background;
    Boolean isClicked = false;
    LinearLayout overlay;
    Date date;
    Integer result;
    String time;
    AdView adView;
    String mood;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplication();
        imgbtn = findViewById(R.id.imgbtn);
        imgbtn2 = findViewById(R.id.imgbtn2);
        layout = findViewById(R.id.relative_layout_2);
        background = findViewById(R.id.main_layout);
        overlay = findViewById(R.id.linearLayout);

        text = findViewById(R.id.text_1);
        text2 = findViewById(R.id.text_2);
        text3 = findViewById(R.id.text_3);
        text4 = findViewById(R.id.text_4);
        text5 = findViewById(R.id.text_5);
        text6 = findViewById(R.id.text_6);
        text7 = findViewById(R.id.text_7);
        text8 = findViewById(R.id.text_8);
        text9 = findViewById(R.id.text_9);
        text10 = findViewById(R.id.text_10);
        txtCity = findViewById(R.id.text_city);

        typeface = Typeface.createFromAsset(getAssets(), "VAG_Rounded_Bold.ttf");

        text.setTypeface(typeface);
        text2.setTypeface(typeface);
        text3.setTypeface(typeface);
        text4.setTypeface(typeface);
        text5.setTypeface(typeface);
        text6.setTypeface(typeface);
        text7.setTypeface(typeface);
        text8.setTypeface(typeface);
        text9.setTypeface(typeface);
        text10.setTypeface(typeface);
        txtCity.setTypeface(typeface);

        image1 = findViewById(R.id.image_1);
        image2 = findViewById(R.id.image_2);
        image3 = findViewById(R.id.image_3);
        image4 = findViewById(R.id.image_4);
        image5 = findViewById(R.id.image_5);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        },REQUEST_LOCATION);

        clickImageButton();
        clickLayout();

        intent = getIntent();
        if(intent != null){
            receivedData = intent.getStringExtra("ID");

            //klavye kapansın
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            );
            if(receivedData != null){
                getWeatherByID(receivedData);
            }else{
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    buildAlertMessageNoGps();
                }else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    getLocation();
                }
            }
        }

        MobileAds.initialize(this, "ca-app-pub-7683616394936974~2469361229");
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getDate() {
        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        time = simpleDateFormat.format(rightNow.getTime());
        result = Integer.parseInt(time);
    }

    private void clickLayout(){
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isClicked){
                   isClicked = false;
               }else{
                   isClicked = true;
               }

                getTemperature(isClicked);

            }
        });
    }

   /* public void getNotification(View view){
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 49);
        calendar.set(Calendar.SECOND, 30);

        Intent intent = new Intent(getApplicationContext(),Notification.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //alarm will be triggered even if the phone is off
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }*/

    private void clickImageButton() {

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    buildAlertMessageNoGps();
                }else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    getLocation();
                }


            }
        });

        imgbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activity = new Intent(MainActivity.this,AutoComplete.class);
                try {
                    activity.putExtra("icon", icon);
                    activity.putExtra("time", time);
                    activity.setFlags(activity.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(activity);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }catch(Exception e){
                    startActivity(activity);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }
            }
        });


    }

    private void getLocation() {
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
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
                getWeatherByLonLat(latti,longi);


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

    private void getWeatherByLonLat(double latti,double longi){

        HashMap<String,Object> map = new HashMap<>();
        map.put("lat",latti);
        map.put("lon",longi);
        map.put("APPID","4509d4e4fe84d0523805a73785201aae");
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Example> call;
        call = apiService.getExampleResponse(map);
        call.enqueue(new Callback<Example>() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call call, retrofit2.Response response) {
                Log.d("Response :", response.body().toString());
                example = (Example) response.body();
                Log.d("Example :", response.body().toString());
                getData(example);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("Error service :", t.toString());
            }
        });
    }

    private void getWeatherByID(String receivedData){

        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Example> call;
        call = apiService.getCityResponseById(receivedData,"4509d4e4fe84d0523805a73785201aae");
        call.enqueue(new Callback<Example>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                example = response.body();
                getData(example);

            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

            }
        });

    }

   /* public void showNotification(View view){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        //set icon to notification
        //builder.setSmallIcon(R.drawable.app_icon);
        builder.setContentTitle("Weather Forecasting");
        builder.setContentText("Weather mood " + mood);
        Intent intent = new Intent();
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

    }*/

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getData(Example example){

        getTemperature(isClicked);

        String time = example.getList().get(0).getDtTxt();
        String time2 = example.getList().get(8).getDtTxt();
        String time3 = example.getList().get(16).getDtTxt();
        String time4 = example.getList().get(24).getDtTxt();
        String time5 = example.getList().get(32).getDtTxt();

        String location = example.getCity().getName();
        String country = example.getCity().getCountry();

        //time1
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat postFormatter = new SimpleDateFormat("dd MMMM - EEE");
        newDateStr = postFormatter.format(date);

        //time2
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date2 = null;
        try {
            date2 = formatter2.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat postFormatter2 = new SimpleDateFormat("EEE");
        newDateStr2 = postFormatter2.format(date2);

        //time3
        SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date3 = null;
        try {
            date3 = formatter3.parse(time3);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat postFormatter3 = new SimpleDateFormat("EEE");
        newDateStr3 = postFormatter3.format(date3);

        //time4
        SimpleDateFormat formatter4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date4 = null;
        try {
            date4 = formatter4.parse(time4);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat postFormatter4 = new SimpleDateFormat("EEE");
        newDateStr4 = postFormatter4.format(date4);

        //time5
        SimpleDateFormat formatter5 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date5 = null;
        try {
            date5 = formatter5.parse(time5);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat postFormatter5 = new SimpleDateFormat("EEE");
        newDateStr5 = postFormatter5.format(date5);

        icon = example.getList().get(0).getWeather().get(0).getIcon();
        icon2 = example.getList().get(8).getWeather().get(0).getIcon();
        icon3 = example.getList().get(16).getWeather().get(0).getIcon();
        icon4 = example.getList().get(24).getWeather().get(0).getIcon();
        icon5 = example.getList().get(32).getWeather().get(0).getIcon();

        getImage(icon, image1);
        getBackGroundChanger(icon);
        getImage(icon2, image2);
        getImage(icon3, image3);
        getImage(icon4, image4);
        getImage(icon5, image5);

        txtCity.setText(location + " - " + country);

        text.setText(newDateStr);
        text3.setText(newDateStr2);
        text5.setText(newDateStr3);
        text7.setText(newDateStr4);
        text9.setText(newDateStr5);

        mood = example.getList().get(0).getWeather().get(0).getDescription();

    }

    public void getTemperature(boolean isClicked){

        if(isClicked){

            double temperature = (((example.getList().get(0).getMain().getTemp()) - 273.15) * 1.8 + 32);
            double temperature2 = (((example.getList().get(8).getMain().getTemp()) - 273.15) * 1.8 + 32);
            double temperature3 = (((example.getList().get(16).getMain().getTemp()) - 273.15) * 1.8 + 32);
            double temperature4 = (((example.getList().get(24).getMain().getTemp()) - 273.15) * 1.8 + 32);
            double temperature5 = (((example.getList().get(32).getMain().getTemp()) - 273.15) * 1.8 + 32);

            temperatureInteger = (int) temperature;
            temperatureInteger2 = (int) temperature2;
            temperatureInteger3 = (int) temperature3;
            temperatureInteger4 = (int) temperature4;
            temperatureInteger5 = (int) temperature5;

            text2.setText(temperatureInteger + "°F");
            text4.setText(temperatureInteger2 + "°F");
            text6.setText(temperatureInteger3 + "°F");
            text8.setText(temperatureInteger4 + "°F");
            text10.setText(temperatureInteger5 + "°F");

        }else {

            double temperature = ((example.getList().get(0).getMain().getTemp()) - 273.15);
            double temperature2 = ((example.getList().get(8).getMain().getTemp()) - 273.15);
            double temperature3 = ((example.getList().get(16).getMain().getTemp()) - 273.15);
            double temperature4 = ((example.getList().get(24).getMain().getTemp()) - 273.15);
            double temperature5 = ((example.getList().get(32).getMain().getTemp()) - 273.15);

            temperatureInteger = (int) temperature;
            temperatureInteger2 = (int) temperature2;
            temperatureInteger3 = (int) temperature3;
            temperatureInteger4 = (int) temperature4;
            temperatureInteger5 = (int) temperature5;

            text2.setText(temperatureInteger + "°C");
            text4.setText(temperatureInteger2 + "°C");
            text6.setText(temperatureInteger3 + "°C");
            text8.setText(temperatureInteger4 + "°C");
            text10.setText(temperatureInteger5 + "°C");

        }
    }

    public void getImage(String icon, ImageView image){
        if(icon.contains("01d") || icon.contains("01n")){
            GlideApp.with(context).load(R.drawable.img_weather_clearsky).centerInside().into(image);
        }else if(icon.contains("02d") || icon.contains("02n")){
            GlideApp.with(context).load(R.drawable.img_weather_fewclouds).centerInside().into(image);
        }else if(icon.contains("03d") || icon.contains("03n")){
            GlideApp.with(context).load(R.drawable.img_weather_scatteredclouds).centerInside().into(image);
        }else if(icon.contains("04d") || icon.contains("04n")){
            GlideApp.with(context).load(R.drawable.img_weather_brokenclouds).centerInside().into(image);
        }else if(icon.contains("09d")|| icon.contains("09n")){
            GlideApp.with(context).load(R.drawable.img_weather_showerrain).centerInside().into(image);
        }else if(icon.contains("10d") || icon.contains("10n")){
            GlideApp.with(context).load(R.drawable.img_weather_rain).centerInside().into(image);
        }else if(icon.contains("11d") || icon.contains("11n")){
            GlideApp.with(context).load(R.drawable.img_weather_thunderstorm).centerInside().into(image);
        }else if(icon.contains("13d") || icon.contains("13n")){
            GlideApp.with(context).load(R.drawable.img_weather_snow).centerInside().into(image);
        }else if(icon.contains("50d") || icon.contains("50n")){
            GlideApp.with(context).load(R.drawable.img_weather_mist).centerInside().into(image);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getBackGroundChanger(String icon){
        getDate();
        if(result > 19){
            background.setBackgroundResource(R.drawable.night_background);
            overlay.setBackgroundResource(R.drawable.night_overlay);
        }else if(icon.contains("03d") || icon.contains("03n")
                || icon.contains("04d") || icon.contains("04n")
                || icon.contains("09d")|| icon.contains("09n")
                || icon.contains("10d") || icon.contains("10n")
                || icon.contains("11d") || icon.contains("11n")
                || icon.contains("13d") || icon.contains("13n")
                || icon.contains("50d") || icon.contains("50n")){
            background.setBackgroundResource(R.drawable.cold_background);
            overlay.setBackgroundResource(R.drawable.cold_overlay);
        }else if (icon.contains("01d") || icon.contains("01n")
                || icon.contains("02d") || icon.contains("02n")){
            background.setBackgroundResource(R.drawable.summer_background);
            overlay.setBackgroundResource(R.drawable.summer_overlay);
        }
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
