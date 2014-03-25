package demo.map;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class MapActivity extends FragmentActivity {
    GoogleMap map;
    private ArrayList<LatLng> wayPoints;
    private LatLng origin, destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        map = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        Intent intent = getIntent();
        String orig = intent.getExtras().getString("origin");
        String dest = intent.getExtras().getString("destination");


        origin = geoLocate(orig);
        destination = geoLocate(dest);

        String o = origin.latitude + "," + origin.longitude;
        String d = destination.latitude + "," + destination.longitude;

        map.addMarker(new MarkerOptions()
                .title("Indulás")
                .position(origin));
        map.addMarker(new MarkerOptions()
                .title("Érkezés")
                .position(destination));

        PolylineOptions line = new PolylineOptions()
                /*.add(origin)
                .add(destination)
                .color(Color.BLUE)*/;


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 10));

        new HttpAsyncTask().execute();

        Toast.makeText(this, getDirections().get(1).toString(), Toast.LENGTH_LONG).show();

    }


    public ArrayList<LatLng> getDirections() {

        String orig = origin.latitude + "," + origin.longitude;
        String dest = destination.latitude + "," + destination.longitude;
        Document doc = null;

        String uri =
                "http://maps.google.com/maps/api/directions/xml?origin=" + orig + "&destination=" + dest + "&sensor=false&mode=drive";

        try {
            URL url = new URL(uri);

            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = null;

            httpResponse = httpClient.execute(new HttpGet(uri));

            InputStream xml = httpResponse.getEntity().getContent();


            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;

            db = dbf.newDocumentBuilder();


            doc = db.parse(xml);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        NodeList nList = doc.getElementsByTagName("overview_polyline");
        Node polyline = nList.item(0);
        NodeList pList = polyline.getChildNodes();

        String points = pList.item(1).getTextContent();

        wayPoints=decodePoly(points);
        return wayPoints;

    }

    public LatLng geoLocate(String location) {
        Geocoder gc = new Geocoder(this);
        List<Address> list = null;
        try {
            list = gc.getFromLocationName(location, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address addr = list.get(0);
        double lat = addr.getLatitude();
        double lng = addr.getLongitude();

        LatLng ll = new LatLng(lat, lng);
        return ll;

    }

    //code source: http://www.geekyblogger.com/2010/12/decoding-polylines-from-google-maps.html
    public static ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        double lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            double dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            double dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng(((lat / 1E5)),
                    ((lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class HttpAsyncTask extends AsyncTask<Void, Void, ArrayList<LatLng>> {
        @Override
        protected ArrayList<LatLng> doInBackground(Void... params) {

            return getDirections();
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(ArrayList<LatLng> result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();

        }

    }

}


