package project.my222au.assignment3.WeatherWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import project.my222au.assignment3.R;


public class Widget extends AppWidgetProvider {


    public static String timeUpdated;


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            Intent uppdateIntent = new Intent(context, WeatherService.class);
            uppdateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            uppdateIntent.putExtra(WeatherService.INTENT_CHOICE, WeatherService.UPPDATE);
            uppdateIntent.putExtra("appwidgetid",appWidgetId);
            context.startService(uppdateIntent);
               RemoteViews view = buildremoteviews(context,appWidgetId);

            appWidgetManager.updateAppWidget(appWidgetIds, view);
        }
    }


    private RemoteViews buildremoteviews(Context context, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        int [] ids = {appWidgetId};
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        timeUpdated = formatter.format(new Date(calendar.getTimeInMillis()));
        Intent updatetIntent = new Intent(context, Widget.class);
        updatetIntent .setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updatetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        PendingIntent updatePen = PendingIntent.getBroadcast(context, appWidgetId, updatetIntent, 0);
        views.setOnClickPendingIntent(R.id.uppdatebutton, updatePen);
        return views;


    }





}





