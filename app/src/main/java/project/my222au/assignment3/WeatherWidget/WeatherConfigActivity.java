package project.my222au.assignment3.WeatherWidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import project.my222au.assignment3.R;

public class WeatherConfigActivity extends AppCompatActivity{
    private static final String TAG = WeatherConfigActivity.class.getSimpleName();
    private static final String PREF_NAME = "assignment3.weatherwidget2.WeatherConfigActivity";
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private int mAppWidgetId;
    private ArrayAdapter<String> adapter;
    public static final String CITY = "city";
    private static final String PREF_KEY = "city";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        String[] cites = {"Stockholm", "Jönköping", "Kiruna"};

        setResult(RESULT_CANCELED);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);


            adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.config_list_iteam, cites);
            mListView = (ListView) findViewById(R.id.Listviewe);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new OnCityClick());


        }
    }
    private class OnCityClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String city = (String) mListView.getItemAtPosition(position);


             savePrefs(mAppWidgetId,city);

            Intent intent = new Intent(WeatherConfigActivity.this, WeatherService.class);
            intent.putExtra(WeatherService.INTENT_CHOICE, WeatherService.WIDGET_INIT);
            intent.putExtra(CITY, city);

            intent.putExtra("appwidgetid", mAppWidgetId);
            getApplicationContext().startService(intent);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();

        }


    }


    public void savePrefs(int appWidgetId2, String city) {
        SharedPreferences.Editor prefs = getBaseContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        prefs.putString(PREF_KEY + appWidgetId2, city);
        prefs.commit();
    }




    static String loadCityPref(Context context,int widgetID) {
        SharedPreferences userDetails =context .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String pref = userDetails.getString(PREF_KEY+widgetID,null);

        if(pref!=null){
            return pref;
        }
        return "NO CTY";
    }


}