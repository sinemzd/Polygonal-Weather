package com.sinemdalak.weatherforecasting;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RemoteViews;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.sinemdalak.weatherforecasting.utils.GlideApp;

public class WidgetProvider extends AppWidgetProvider {

    String location;
    String description;
    String icon;
    String temperature;
    String temperatureText;
    Boolean isClicked;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        if (extras != null) {
            location = extras.getString("location");
            description = extras.getString("description");
            icon = extras.getString("icon");
            temperature = extras.getString("temperature");
            isClicked = extras.getBoolean("isClicked");

        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {

            Intent intent = new Intent(context, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
            views.setOnClickPendingIntent(R.id.widget_main_layout, pendingIntent);


            AppWidgetTarget appWidgetTarget = new AppWidgetTarget(context, R.id.widget_weather_icon, views, appWidgetIds);

            if (icon != null) {

                if (icon.contains("01d")) {
                    GlideApp.with(context.getApplicationContext()).asBitmap().load(R.drawable.img_weather_clearsky).into(appWidgetTarget);
                } else if (icon.contains("01n")) {
                    GlideApp.with(context.getApplicationContext()).asBitmap().load(R.drawable.img_weather_moon).into(appWidgetTarget);
                } else if (icon.contains("02d")) {
                    GlideApp.with(context.getApplicationContext()).asBitmap().load(R.drawable.img_weather_fewclouds).into(appWidgetTarget);
                } else if (icon.contains("02n")) {
                    GlideApp.with(context.getApplicationContext()).asBitmap().load(R.drawable.img_weather_mooncloud).into(appWidgetTarget);
                } else if (icon.contains("03d") || icon.contains("03n")) {
                    GlideApp.with(context.getApplicationContext()).asBitmap().load(R.drawable.img_weather_scatteredclouds).into(appWidgetTarget);
                } else if (icon.contains("04d") || icon.contains("04n")) {
                    GlideApp.with(context.getApplicationContext()).asBitmap().load(R.drawable.img_weather_brokenclouds).into(appWidgetTarget);
                } else if (icon.contains("09d") || icon.contains("09n")) {
                    GlideApp.with(context.getApplicationContext()).asBitmap().load(R.drawable.img_weather_showerrain).into(appWidgetTarget);
                } else if (icon.contains("10d")) {
                    GlideApp.with(context.getApplicationContext()).asBitmap().load(R.drawable.img_weather_rain).into(appWidgetTarget);
                } else if (icon.contains("10n")) {
                    GlideApp.with(context.getApplicationContext()).asBitmap().load(R.drawable.img_weather_moonrain).into(appWidgetTarget);
                } else if (icon.contains("11d") || icon.contains("11n")) {
                    GlideApp.with(context.getApplicationContext()).asBitmap().load(R.drawable.img_weather_thunderstorm).into(appWidgetTarget);
                } else if (icon.contains("13d") || icon.contains("13n")) {
                    GlideApp.with(context.getApplicationContext()).asBitmap().load(R.drawable.img_weather_snow).into(appWidgetTarget);
                } else if (icon.contains("50d") || icon.contains("50n")) {
                    GlideApp.with(context.getApplicationContext()).asBitmap().load(R.drawable.img_weather_mist).into(appWidgetTarget);
                }

            }

                if (isClicked) {

                    double tempD = Double.parseDouble(temperature);
                    int tempI = (int) ((tempD - 273.15) * 1.8 + 32);
                    temperatureText = (tempI + "°F");

                } else {

                    double tempD = Double.parseDouble(temperature);
                    int tempI = (int) (tempD - 273.15);
                    temperatureText = (tempI + "°C");
                }


            Log.d("Widget :", intent.toString());

            views.setImageViewBitmap(R.id.widget_location, buildUpdate(context, location, 14));
            views.setImageViewBitmap(R.id.widget_description, buildUpdate(context, description, 14));
            views.setImageViewBitmap(R.id.widget_degree, buildUpdate(context, temperatureText, 14));

            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
    }

    public Bitmap buildUpdate(Context context, String text, float dp) {
        Bitmap myBitmap = Bitmap.createBitmap(500, 50, Bitmap.Config.ARGB_4444);
        Canvas myCanvas = new Canvas(myBitmap);
        Paint paint = new Paint();
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "VAG_Rounded_Bold.ttf");
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(typeface);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(convertDptoPx(dp));
        paint.setTextAlign(Paint.Align.CENTER);
        myCanvas.drawText(text, 250, convertDptoPx(dp), paint);
        return myBitmap;
    }

    public static float convertDptoPx(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}

