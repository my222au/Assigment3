package project.my222au.assignment3.cityMap;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

import project.my222au.assignment3.R;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private CityHandler mCityHandler;
    private MarkerOptions mMarker;
    private MarkerOptions marker2;
    private String mCityName;
    private double mDistance;
    private double mLatitude;
    private double mLongitude;
    private LatLng mCurrentCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citymap);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mCityHandler = new CityHandler();
        mCityHandler.parseFile(getApplicationContext());

    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        for (int i = 0; i < mCityHandler.getlatitudes().size(); i++) {
            mCityHandler.setCity(mCityHandler.getCites().get(i));
            final LatLng cites = new LatLng(mCityHandler.getlatitudes().get(i), mCityHandler.getLongitudes().get(i));
            mMarker = new MarkerOptions().position(cites).title(mCityHandler.getCites().get(i));
            mMap.addMarker(mMarker);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(56.124186886491565, 14.114947579801083), 7));
            mMap.getUiSettings().setZoomControlsEnabled(true);


            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if(mCityHandler.getCity()==null){
                    if(marker.getTitle().equals(mCityHandler.getCity()))
                    Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
                    return true;
                    }
                    return false;
                }
            });

        }
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_searching_black_36dp))
                .anchor(0.5f, 0.5f)

                .position(mMap.getCameraPosition().target));


        getDistance();
    }


    private void getDistance() {
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            Toast toast;
            int cityRaduis = 5;  // city area

            @Override
            public void onCameraMove() {
                boolean isCitynear = false;
                double nearCityPos = 0;
                if (toast != null) {
                    toast.cancel();  // stops repeating Toast
                }
                LatLng center = mMap.getCameraPosition().target;
                toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
                for (int i = 0; i < mCityHandler.getCites().size(); i++) {
                    mLatitude = mCityHandler.getlatitudes().get(i);
                    mLongitude = mCityHandler.getLongitudes().get(i);
                    mCityName = mCityHandler.getCites().get(i);
                    mCurrentCity = new LatLng(mLatitude, mLongitude);
                    mDistance = mesureDistance(center, mCurrentCity);
                    DecimalFormat df = new DecimalFormat("#.00");
                    String distanceString = df.format(mDistance);
                    if (nearCityPos > mDistance || !isCitynear) { //  when the map is created the nearCityPos = 0  and city near is not set
                        if (mDistance < cityRaduis) {         // if  we are on the city area print only the city name
                            toast.setText(mCityName);
                        } else { // else show distance to the city
                            toast.setText(distanceString + "km " + "to " + mCityName);
                        }
                        isCitynear = true;
                        nearCityPos = mDistance;
                    }
                }
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            }


        });



        // Add back option to mainList


    }

    private double mesureDistance(LatLng currentCity, LatLng center) {
        Location centerloc = new Location("center");
        centerloc.setLatitude(center.latitude);
        centerloc.setLongitude(center.longitude);
        Location cityLoc = new Location("city");
        cityLoc.setLatitude(currentCity.latitude);
        cityLoc.setLongitude(currentCity.longitude);
        double distance = centerloc.distanceTo(cityLoc);
        return distance / 1000;
    }


}

