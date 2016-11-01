package project.my222au.assignment3.theRoadMap;


import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class RouteHandler {
    private static final String TAG = RouteHandler.class.getSimpleName();
    private List<String> mDestinationNames = new ArrayList<String>();
    private List<String> mCoordinates = new ArrayList<String>();


    private BufferedReader mBufferReader;
    private FileOutputStream mFileOutputStream;
    private InputStream mInputStream;

    public File getKMLfile(URL url, Context context) {

        try {



            URLConnection urlconnection = url.openConnection();
            urlconnection.connect();
            mInputStream = urlconnection.getInputStream();
            mBufferReader = new BufferedReader(new InputStreamReader(mInputStream));

            // Open a private file associated with this Context's application package for writing. Creates the file if it doesn't already exist.
            mFileOutputStream = context.openFileOutput("kmlroute.xml", Context.MODE_PRIVATE);
            String line = null;
            while ((line = mBufferReader.readLine()) != null) {
                mFileOutputStream.write(line.getBytes());
            }
            mFileOutputStream.close();
            mBufferReader.close();
            mInputStream.close();

        } catch (IOException e) {
            Log.e(TAG, "IOException caught", e);
        }
        return context.getFileStreamPath("kmlroute.xml");

    }


    public void parseKmlFile(InputStream input) {
        XmlPullParserFactory factory = null;
        XmlPullParser xmlParser = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            xmlParser = factory.newPullParser();
            xmlParser.setInput(input,null);
            while (xmlParser.getEventType()!= XmlResourceParser.END_DOCUMENT) {
                String tag = xmlParser.getName();
                if (xmlParser.getEventType() == XmlResourceParser.START_TAG) {

                    if (tag.equals("name")) {

                        mDestinationNames.add(xmlParser.nextText());
                    }
                    if (tag.equals("coordinates")) {


                        getCoordinates().add(xmlParser.nextText());


                    }
                }

                xmlParser.next();



            }


        } catch (XmlPullParserException e) {
            Log.e(TAG, "XMLpullparserException caught", e);
        } catch (IOException e) {
            Log.e(TAG, "IOException caught", e);
        }


    }


    public List<String> getDestinationNames() {
        return mDestinationNames;
    }

    public void setDestinationNames(List<String> destinationNames) {
        mDestinationNames = destinationNames;
    }

    public List<String> getCoordinates() {
        return mCoordinates;
    }

    public void setCoordinates(List<String> coordinates) {
        mCoordinates = coordinates;
    }

}





