package project.my222au.assignment3.cityMap;


import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import project.my222au.assignment3.R;

public class CityHandler {
    private static final String TAG = MapsActivity.class.getCanonicalName() ;
    private double mLatitude;
    private double mLongitude;
    private String city;
    private List<Double> mlatitudes = new ArrayList<Double>();
    private List<Double> mLongitudes = new ArrayList<Double>();
    private List<String> mCites = new ArrayList<String >();

    public double getLatitude() {
        return mLatitude;
    }

    public List<Double> getlatitudes() {
        return mlatitudes;
    }

    public void setlatitudes(List<Double> mlatitudes) {
        this.mlatitudes = mlatitudes;
    }

    public List<Double> getLongitudes() {
        return mLongitudes;
    }

    public void setLongitudes(List<Double> longitudes) {
        mLongitudes = longitudes;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public List<String> getCites() {
        return mCites;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

public void parseFile(Context context) {
        try {
        InputStream  inputstream = context.getResources().openRawResource(R.raw.cities);
        BufferedReader readar = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"));

            String line;
            while((line=readar.readLine())!=null){
                String [] arr = line.split(":");
                String  city = arr[0];
                String  latitude  =arr[1] ;
                String  longitude = arr[2];
              mlatitudes.add(Double.parseDouble(latitude));
              mLongitudes.add(Double.parseDouble(longitude));
              mCites.add(city) ;
            }

            readar.close();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public String getCity() {

        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}