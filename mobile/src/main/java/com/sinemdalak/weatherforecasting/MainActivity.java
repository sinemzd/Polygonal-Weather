package com.sinemdalak.weatherforecasting;

import android.Manifest;
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
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sinemdalak.weatherforecasting.model.Example;
import com.sinemdalak.weatherforecasting.utils.GlideApp;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Result ";
    TextView text, text2, text3, text4, text5, text6, text7, text8, text9, text10, txtCity;
    ImageView image, image2, image3, image4, image5, imgbtn, imgbtn2;
    private Context context;
    ApiInterface apiService;
    Example example;
    int temperatureInteger, temperatureInteger2, temperatureInteger3, temperatureInteger4, temperatureInteger5;
    String newDateStr, newDateStr2, newDateStr3, newDateStr4, newDateStr5;
    String icon, icon2, icon3, icon4, icon5;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;
     static final int REQUEST_SEARCH_CITY = 2;
    String lattitude, longitude;
    Typeface typeface;
    RelativeLayout background;
    Boolean isClicked = false;
    LinearLayout overlay, overlay_2;
    Date date;
    AdView adView;
    Boolean button = false;



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
        background = findViewById(R.id.main_layout);
        overlay = findViewById(R.id.linearLayout);
        overlay_2 = findViewById(R.id.layout_overlay);

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

        image = findViewById(R.id.image_1);
        image2 = findViewById(R.id.image_2);
        image3 = findViewById(R.id.image_3);
        image4 = findViewById(R.id.image_4);
        image5 = findViewById(R.id.image_5);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }

        clickLocationImageButton();
        clickNextPageImageButton();
        clickText();


        MobileAds.initialize(this, "ca-app-pub-7683616394936974~2469361229");
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SEARCH_CITY) {
            if (resultCode == RESULT_OK) {
                String receivedData = data.getStringExtra("ID");

                if (receivedData != null) {
                    getWeatherByID(receivedData);
                } else {
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        buildAlertMessageNoGps();
                    } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        getLocation();
                    }
                }
            }
        }
    }

    private void clickText() {
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isClicked) {
                    isClicked = false;
                } else {
                    isClicked = true;
                }

                getTemperature(isClicked);

            }
        });
    }

    private void clickLocationImageButton() {

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                }

            }
        });
    }

    private void clickNextPageImageButton() {

        imgbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activity = new Intent(context, AutoComplete.class);
                try {
                    activity.putExtra("icon", icon);
                    startActivityForResult(activity, REQUEST_SEARCH_CITY);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } catch (Exception e) {
                    startActivityForResult(activity, REQUEST_SEARCH_CITY);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                }
                return;
            }
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                getWeatherByLonLat(latti, longi);


            } else {
                Toast.makeText(this, "Unable to trace your location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void buildAlertMessageNoGps() {

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

    private void getWeatherByLonLat(double latti, double longi) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("lat", latti);
        map.put("lon", longi);
        map.put("APPID", "4509d4e4fe84d0523805a73785201aae");
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

    private void getWeatherByID(String receivedData) {

        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Example> call;
        call = apiService.getCityResponseById(receivedData, "4509d4e4fe84d0523805a73785201aae");

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getData(Example example) {

        getTemperature(isClicked);

        String location = example.getCity().getName();
        String country = example.getCity().getCountry();

        txtCity.setText(location + " - " + country);

        //Time and Icon
        if (example.getList().size() >= 0) {

            if (example.getList().get(0) != null) {
                String time = example.getList().get(0).getDtTxt();

                //Time
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = null;
                try {
                    date = formatter.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat postFormatter = new SimpleDateFormat("dd MMMM - EEE");
                newDateStr = postFormatter.format(date);

                text.setText(newDateStr);
                icon = example.getList().get(0).getWeather().get(0).getIcon();

                //Icon
                getImage(icon, image);
                getBackGroundChanger(icon);

            }
        } else {
            text.setText("-");
        }

        //Time 2 and Icon 2
        if (example.getList().size() >= 8) {

            if (example.getList().get(8) != null) {
                String time2 = example.getList().get(8).getDtTxt();

                //Time 2
                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date2 = null;
                try {
                    date2 = formatter2.parse(time2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat postFormatter2 = new SimpleDateFormat("EEE");
                newDateStr2 = postFormatter2.format(date2);

                text3.setText(newDateStr2);
                icon2 = example.getList().get(8).getWeather().get(0).getIcon();

                //Icon 2
                getImage(icon2, image2);
            }
        } else {
            text3.setText("-");
        }

        //Time 3 and Icon 3
        if (example.getList().size() >= 16) {

            if (example.getList().get(16) != null) {
                String time3 = example.getList().get(16).getDtTxt();

                //Time 3
                SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date3 = null;
                try {
                    date3 = formatter3.parse(time3);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat postFormatter3 = new SimpleDateFormat("EEE");
                newDateStr3 = postFormatter3.format(date3);

                text5.setText(newDateStr3);
                icon3 = example.getList().get(16).getWeather().get(0).getIcon();

                //Icon 3
                getImage(icon3, image3);
            }
        } else {
            text5.setText("-");
        }

        //Time 4 and Icon 4
        if (example.getList().size() >= 24) {

            if (example.getList().get(24) != null) {
                String time4 = example.getList().get(24).getDtTxt();

                //Time 4
                SimpleDateFormat formatter4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date4 = null;
                try {
                    date4 = formatter4.parse(time4);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat postFormatter4 = new SimpleDateFormat("EEE");
                newDateStr4 = postFormatter4.format(date4);
                text7.setText(newDateStr4);
                icon4 = example.getList().get(24).getWeather().get(0).getIcon();

                //Icon 4
                getImage(icon4, image4);
            }
        } else {
            text7.setText("-");
        }

        //Time 5 and Icon 5
        if (example.getList().size() >= 32) {

            if (example.getList().get(32) != null) {
                String time5 = example.getList().get(32).getDtTxt();

                //Time 5
                SimpleDateFormat formatter5 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date5 = null;
                try {
                    date5 = formatter5.parse(time5);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat postFormatter5 = new SimpleDateFormat("EEE");
                newDateStr5 = postFormatter5.format(date5);
                text9.setText(newDateStr5);
                icon5 = example.getList().get(32).getWeather().get(0).getIcon();

                //Icon 5
                getImage(icon5, image5);
            }
        } else {
            text9.setText("-");
        }

    }

    double calculateFDegree(double d) {
        return (d - 273.15) * 1.8 + 32;
    }

    double calculateCDegree(double d) {
        return (d - 273.15);
    }

    public void getTemperature(boolean isClicked) {

        if (isClicked) {

            //temperature1 Celcius
            if (example.getList().size() >= 0) {
                if (example.getList().get(0) != null) {
                    double temperature = (calculateFDegree(example.getList().get(0).getMain().getTemp()));
                    temperatureInteger = (int) temperature;
                    text2.setText(temperatureInteger + "°F");
                } else {
                    text2.setText("-");

                }
            } else {
                text2.setText("-");
            }

            //temperature2 Celcius
            if (example.getList().size() >= 8) {
                if (example.getList().get(8) != null) {
                    double temperature2 = (calculateFDegree(example.getList().get(8).getMain().getTemp()));
                    temperatureInteger2 = (int) temperature2;
                    text4.setText(temperatureInteger2 + "°F");
                } else {
                    text4.setText("-");

                }
            } else {
                text4.setText("-");
            }

            //temperature3 Celcius
            if (example.getList().size() >= 16) {
                if (example.getList().get(16) != null) {
                    double temperature3 = (calculateFDegree(example.getList().get(16).getMain().getTemp()));
                    temperatureInteger3 = (int) temperature3;
                    text6.setText(temperatureInteger3 + "°F");
                } else {
                    text6.setText("-");

                }
            } else {
                text6.setText("-");
            }

            //temperature4 Celcius
            if (example.getList().size() >= 24) {
                if (example.getList().get(24) != null) {
                    double temperature4 = (calculateFDegree(example.getList().get(24).getMain().getTemp()));
                    temperatureInteger4 = (int) temperature4;
                    text8.setText(temperatureInteger4 + "°F");
                } else {
                    text8.setText("-");

                }
            } else {
                text8.setText("-");
            }

            //temperature5 Celcius
            if (example.getList().size() >= 32) {
                if (example.getList().get(32) != null) {
                    double temperature5 = (calculateFDegree(example.getList().get(32).getMain().getTemp()));
                    temperatureInteger5 = (int) temperature5;
                    text10.setText(temperatureInteger5 + "°F");
                } else {
                    text10.setText("-");

                }
            } else {
                text10.setText("-");
            }

        } else {

            //temperature1 Fahrenheit
            if (example.getList().size() >= 0) {
                if (example.getList().get(0) != null) {
                    double temperature = (calculateCDegree(example.getList().get(0).getMain().getTemp()));
                    temperatureInteger = (int) temperature;
                    text2.setText(temperatureInteger + "°C");
                } else {
                    text2.setText("-");

                }
            } else {
                text2.setText("-");
            }

            //temperature2 Fahrenheit
            if (example.getList().size() >= 8) {
                if (example.getList().get(8) != null) {
                    double temperature2 = (calculateCDegree(example.getList().get(8).getMain().getTemp()));
                    temperatureInteger2 = (int) temperature2;
                    text4.setText(temperatureInteger2 + "°C");
                } else {
                    text4.setText("-");

                }
            } else {
                text4.setText("-");
            }

            //temperature3 Fahrenheit
            if (example.getList().size() >= 16) {
                if (example.getList().get(16) != null) {
                    double temperature3 = (calculateCDegree(example.getList().get(16).getMain().getTemp()));
                    temperatureInteger3 = (int) temperature3;
                    text6.setText(temperatureInteger3 + "°C");
                } else {
                    text6.setText("-");

                }
            } else {
                text6.setText("-");
            }

            //temperature4 Fahrenheit
            if (example.getList().size() >= 24) {
                if (example.getList().get(24) != null) {
                    double temperature4 = (calculateCDegree(example.getList().get(24).getMain().getTemp()));
                    temperatureInteger4 = (int) temperature4;
                    text8.setText(temperatureInteger4 + "°C");
                } else {
                    text8.setText("-");

                }
            } else {
                text8.setText("-");
            }

            //temperature5 Fahrenheit
            if (example.getList().size() >= 32) {
                if (example.getList().get(32) != null) {
                    double temperature5 = (calculateCDegree(example.getList().get(32).getMain().getTemp()));
                    temperatureInteger5 = (int) temperature5;
                    text10.setText(temperatureInteger5 + "°C");
                } else {
                    text10.setText("-");

                }
            } else {
                text10.setText("-");
            }
        }
    }

    public void getImage(String icon, ImageView image) {
        if (icon.contains("01d")) {
            GlideApp.with(context).load(R.drawable.img_weather_clearsky).centerInside().into(image);
        } else if (icon.contains("01n")) {
            GlideApp.with(context).load(R.drawable.img_weather_moon).centerInside().into(image);
        } else if (icon.contains("02d")) {
            GlideApp.with(context).load(R.drawable.img_weather_fewclouds).centerInside().into(image);
        } else if (icon.contains("02n")) {
            GlideApp.with(context).load(R.drawable.img_weather_mooncloud).centerInside().into(image);
        } else if (icon.contains("03d") || icon.contains("03n")) {
            GlideApp.with(context).load(R.drawable.img_weather_scatteredclouds).centerInside().into(image);
        } else if (icon.contains("04d") || icon.contains("04n")) {
            GlideApp.with(context).load(R.drawable.img_weather_brokenclouds).centerInside().into(image);
        } else if (icon.contains("09d") || icon.contains("09n")) {
            GlideApp.with(context).load(R.drawable.img_weather_showerrain).centerInside().into(image);
        } else if (icon.contains("10d")) {
            GlideApp.with(context).load(R.drawable.img_weather_rain).centerInside().into(image);
        } else if (icon.contains("10n")) {
            GlideApp.with(context).load(R.drawable.img_weather_moonrain).centerInside().into(image);
        } else if (icon.contains("11d") || icon.contains("11n")) {
            GlideApp.with(context).load(R.drawable.img_weather_thunderstorm).centerInside().into(image);
        } else if (icon.contains("13d") || icon.contains("13n")) {
            GlideApp.with(context).load(R.drawable.img_weather_snow).centerInside().into(image);
        } else if (icon.contains("50d") || icon.contains("50n")) {
            GlideApp.with(context).load(R.drawable.img_weather_mist).centerInside().into(image);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getBackGroundChanger(String icon) {
        if (icon.contains("01n") || icon.contains("02n") || icon.contains("03n")
                || icon.contains("04n") || icon.contains("09n") || icon.contains("10n")
                || icon.contains("11n") || icon.contains("13n") || icon.contains("50n")) {
            background.setBackgroundResource(R.drawable.night_background_main);
            overlay.setBackgroundResource(R.drawable.night_overlay_big);
            overlay_2.setBackgroundResource(R.drawable.night_overlay);
        } else if (icon.contains("03d") || icon.contains("04d")
                || icon.contains("09d") || icon.contains("10d")
                || icon.contains("11d") || icon.contains("13d")
                || icon.contains("50d")) {
            background.setBackgroundResource(R.drawable.cold_background_main);
            overlay.setBackgroundResource(R.drawable.cold_overlay_big);
            overlay_2.setBackgroundResource(R.drawable.cold_overlay);
        } else if (icon.contains("01d") || icon.contains("02d")) {
            background.setBackgroundResource(R.drawable.summer_background_main);
            overlay.setBackgroundResource(R.drawable.summer_overlay_big);
            overlay_2.setBackgroundResource(R.drawable.summer_overlay);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
