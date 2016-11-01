package project.my222au.assignment3.theRoadMap;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import project.my222au.assignment3.R;

public class TheRoadmap extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = TheRoadmap.class.getSimpleName();
    private GoogleMap mMap;
    private MarkerOptions mMarkerOptions;

    private final static String STOCKHOLM = "stockolm";
    private final static String CHOPENHAGEN = "cophengaen";
    private final static String ODESSA = "oddesa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_the_roadmap);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(57.817690723318336,15.732004456222057),6));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                Log.i(TAG, mMap.getCameraPosition().toString());
            }
        });


        if(isNetworkAvailable()) {
            getRouteURL(STOCKHOLM);
        } else{
                Toast.makeText(getApplicationContext(),R.string.network_unavailable_message,Toast.LENGTH_SHORT).show();
            }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // An XML based menu specification.
        // See res/menu/action_menu.xml for details
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toStockholm:
                   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(57.817690723318336,15.732004456222057),6));
                Toast.makeText(getApplicationContext(), " Vaxjo to Stockholm", Toast.LENGTH_SHORT).show();

                getRouteURL(STOCKHOLM);
                break;
            case R.id.toCopenHagen:
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(56.4931036629422,13.593082576990128),7));
                Toast.makeText(getApplicationContext(), " Vaxjo to Copenhagen", Toast.LENGTH_SHORT).show();
                getRouteURL(CHOPENHAGEN);
                break;
            case R.id.toOddesa:
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.11222612028326,21.79277591407299),4));
                Toast.makeText(getApplicationContext(), " Vaxjo to Odessa", Toast.LENGTH_SHORT).show();
                getRouteURL(ODESSA);

                // add Intent backhom to the mainList
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    private  void getRouteURL(String destination) {
        String urlstr = null;
        if (destination.equals(STOCKHOLM) )
            urlstr = "http://cs.lnu.se/android/VaxjoToStockholm.kml";
        if (destination.equals(CHOPENHAGEN))
            urlstr = "http://cs.lnu.se/android/VaxjoToCopenhagen.kml";
        if (destination.equals(ODESSA))
            urlstr = "http://cs.lnu.se/android/VaxjoToOdessa.kml";



            try {

                URL url = new URL(urlstr);
                new RouteDownloader().execute(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
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


    private class RouteDownloader extends AsyncTask<URL, Void, File> {
        private RouteHandler handler = new RouteHandler();
        private String coordinats;
        private Route mRoute = new Route();


        @Override
        protected File doInBackground(URL... url) {
            return handler.getKMLfile(url[0], getApplicationContext());

        }


        @Override
        protected void onPostExecute(File file) {

            super.onPostExecute(file);
            if (file == null) {
                return;
            }
            try {
                handler.parseKmlFile(new FileInputStream(file));
                coordinats = handler.getCoordinates().get(0);
                String[] arr = coordinats.split(",");
                String startlongitude = arr[0];
                String startlatitude = arr[1];
                String endLatitude = arr[arr.length - 2];   // lattidue posestion in xml file
                String ZeroLongitude = arr[arr.length - 3]; // contains 0.0 18,1312313
                String[] arr2 = ZeroLongitude.split(" ");   // get rid of zeros
                String endlongitude = arr2[1];
                Log.i(TAG, "XX" + endLatitude + endlongitude);
                mRoute.setStartCoodrnation(startlatitude, startlongitude);
                mRoute.setEndCoodinations(endLatitude, endlongitude);



                // GET The Whole Route
                String[] routeCoordWithZero = coordinats.split(" ");
                for (int i = 0; i < routeCoordWithZero.length; i++) {
                    String[] routeCoord = routeCoordWithZero[i].split(",");
                    String listLatitude = routeCoord[1];
                    String listLongitude = routeCoord[0];
                    mRoute.setRouteCoordinates(listLatitude, listLongitude);
                }

                 mMap.clear();
                // draw the route  on the map.
                mMap.addPolyline(new PolylineOptions().addAll(mRoute.getRouteCoordinates()).width(8).color(Color.BLUE));
                mMap.addMarker(new MarkerOptions().position(mRoute.getStartCoordinates()));
                mMap.addMarker(new MarkerOptions().position(mRoute.getEndCoordinates()));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


}



