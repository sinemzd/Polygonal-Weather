package com.sinemdalak.weatherforecasting;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.RemoteViews;

public class AppWidgetConfig extends AppCompatActivity {

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    String description;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.weather_widget);

        Intent configIntent = getIntent();
        Bundle extras = configIntent.getExtras();

        if(extras != null){
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            description = extras.getString("description");
            views.setTextViewText(R.id.weather_Description,description);

        }

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED, resultValue);

        if(appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID){
            finish();
        }
    }
}
