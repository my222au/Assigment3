package project.my222au.assignment3.WeatherWidget;


import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import java.net.MalformedURLException;
import java.net.URL;

import project.my222au.assignment3.R;

public class WeatherService extends Service {
    private static final String TAG = WeatherService.class.getSimpleName();
    public static final String INTENT_CHOICE = "choice";
    public static final int WIDGET_INIT = 0;
    public static final int UPPDATE = 1;
    private WeatherReport report;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null) {
            return START_STICKY;
        }
        int operation = intent.getIntExtra(INTENT_CHOICE, -1);


        if (operation == WIDGET_INIT) {

            int widgetid = intent.getIntExtra("appwidgetid", -1);
            String city = intent.getStringExtra("city");

            Intent widgetintent = new Intent(getApplicationContext(), Widget.class);
            widgetintent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            int[] widgetIds = {widgetid};
            widgetintent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
            sendBroadcast(widgetintent);
        } else if (operation == UPPDATE) {

            String city = intent.getStringExtra("city");
            int appWidgetId = intent.getIntExtra("appwidgetid", -1);
            if (isNetworkAvailable()) {
                uppdateWeahter(this, appWidgetId);
            } else {
                Log.e(TAG, " no network!");
            }


        }
        stopSelf();
        return START_STICKY;


    }


    // uppdates weather
    private void uppdateWeahter(Context context, int widgetid) {
        String cityName = WeatherConfigActivity.loadCityPref(context, widgetid);
        String url = null;
        if (cityName.equalsIgnoreCase("Stockholm")) {
            url = "http://www.yr.no/place/Sweden/Stockholm/Stockholm/forecast.xml";
        }
        if (cityName.equalsIgnoreCase("Jönköping")) {
            url = "http://www.yr.no/place/Sweden/J%C3%B6nk%C3%B6ping/J%C3%B6nk%C3%B6ping/forecast.xml";

        }
        if (cityName.equalsIgnoreCase("Kiruna")) {
            url = "http://www.yr.no/place/Sweden/Norrbotten/Kiruna/forecast.xml";
        }
        try {
            URL Url = new URL(url);
            new WeatherRetriever(widgetid, cityName).execute(Url);
        } catch (MalformedURLException e) {

            e.printStackTrace();
        }


    }

    private class WeatherRetriever extends AsyncTask<URL, Void, WeatherReport> {
        private int mAppwidgetid;
        private String mCityname;
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_layout);

        private WeatherRetriever(int appwidgetid, String cityName) {
            mAppwidgetid = appwidgetid;
            mCityname = cityName;
        }

        protected WeatherReport doInBackground(URL... urls) {

            try {
                return WeatherHandler.getWeatherReport(urls[0]);


            } catch (Exception e) {
                throw new RuntimeException();
            }


        }

        protected void onProgressUpdate(Void... progress) {

        }

        protected void onPostExecute(WeatherReport result) {
            report = result;
            WeatherForecast forcasts;
            if (report != null) {
                forcasts = report.getForecasts().get(0);
                Log.i(TAG, "Temp" + forcasts.getTemperature());
                views.setTextViewText(R.id.timeLable, "Uppd. " + Widget.timeUpdated);  // time for the last update form updatebutto
                setViews(views, forcasts, mCityname);
            }

            // sends to acitvity
            Intent intent = new Intent(WeatherService.this, Weather.class);
            intent.putExtra("city", mCityname);
            PendingIntent pendingIntnet = PendingIntent.getActivity(WeatherService.this, mAppwidgetid, intent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntnet);

            AppWidgetManager appwidgetmanger = AppWidgetManager.getInstance(WeatherService.this);
            appwidgetmanger.updateAppWidget(mAppwidgetid, views);


        }


    }

    private void setViews(RemoteViews views, WeatherForecast forcasts, String city) {

        char degree = 0x00B0; // degree char
        views.setTextViewText(R.id.temperatureLabel1, forcasts.getTemperature() + "" + degree);
        views.setTextViewText(R.id.dayNumberLabel, forcasts.getStartYYMMDD());
        views.setTextViewText(R.id.dayLabel, forcasts.getstarttday());
        views.setTextViewText(R.id.rainLabel, forcasts.getRain() + "");
        views.setTextViewText(R.id.cityLabel, city.toUpperCase());

        updateviews(views, forcasts);
    }


    /***
     * @param views
     * @param weatherForecast Uppdates the viwe depending on the weather, The icons is font icon from https://erikflowers.github.io/weather-icons/
     *                        which are in value Folder
     */

    private void updateviews(RemoteViews views, WeatherForecast weatherForecast) {
        int weatheCode = weatherForecast.getWeatherCode();

        if (weatheCode == 1) {
            views.setImageViewResource(R.id.imageView, R.drawable.day_sunnay);
            views.setTextViewText(R.id.summaryLabel, getResources().getString(R.string.summary_sunny));
            if (weatherForecast.getPeriodCode() == 3 || weatherForecast.getPeriodCode() == 0) {
                views.setImageViewResource(R.id.imageView, R.drawable.night_clear);
            }


        }
        if (weatheCode == 3) {
            views.setImageViewResource(R.id.imageView, R.drawable.cloudy_windy_day);
            views.setTextViewText(R.id.summaryLabel, getResources().getString(R.string.summary_partly_cloudy));
            if (weatherForecast.getPeriodCode() == 3 || weatherForecast.getPeriodCode() == 0) {
                views.setImageViewResource(R.id.imageView, R.drawable.cloufy_windy_night);
            }

        }
        if (weatheCode == 4 || weatheCode == 15) {
            views.setImageViewResource(R.id.imageView, R.drawable.cloud1);
            views.setTextViewText(R.id.summaryLabel, getResources().getString(R.string.summary_cloudy));
            if (weatherForecast.getPeriodCode() == 3 || weatherForecast.getPeriodCode() == 0) {
                views.setImageViewResource(R.id.imageView, R.drawable.cloud1);
            }


        }
        if (weatheCode == 10 || weatheCode == 41 || weatheCode == 6 || weatheCode == 9 || weatheCode == 5) {
            views.setImageViewResource(R.id.imageView, R.drawable.rain);
            views.setTextViewText(R.id.summaryLabel, getResources().getString(R.string.summary_rainy));
            if (weatherForecast.getPeriodCode() == 3 || weatherForecast.getPeriodCode() == 0) {
                views.setImageViewResource(R.id.imageView, R.drawable.rain);
            }

        }


        if (weatheCode == 50) {
            views.setImageViewResource(R.id.imageView, R.drawable.snow);
            views.setTextViewText(R.id.summaryLabel, getResources().getString(R.string.summary_snow));

            if (weatherForecast.getPeriodCode() == 3 || weatherForecast.getPeriodCode() == 0) {
                views.setImageViewResource(R.id.imageView, R.drawable.snow);
            }
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;

        }

        return isAvailable;

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}
