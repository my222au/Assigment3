package project.my222au.assignment3.theRoadMap;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private LatLng mStartCoordinates;
    private LatLng mEndCoordinates;
    private List<LatLng> mRouteCoordinates = new ArrayList<LatLng>();




    public  LatLng getEndCoordinates() {
        return mEndCoordinates;
    }

    public void setEndCoodinations(String latitude, String longitude) {
        mEndCoordinates = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
    }

    public LatLng getStartCoordinates() {
        return mStartCoordinates;
    }

    public void setStartCoodrnation(String latitude, String longitude) {
     mStartCoordinates = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
    }



    public  void setRouteCoordinates(String latitude, String Longitude){
        mRouteCoordinates.add(new LatLng(Double.parseDouble(latitude),Double.parseDouble(Longitude)));

    }



    public List<LatLng> getRouteCoordinates() {
        return mRouteCoordinates;
    }


}
