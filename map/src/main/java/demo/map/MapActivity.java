package demo.map;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapActivity extends FragmentActivity {
    GoogleMap map;
    LatLng origin, destination;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        map = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        Intent intent = getIntent();
        String orig = intent.getExtras().getString("origin");
        String dest = intent.getExtras().getString("destination");
        //Toast.makeText(this,orig,Toast.LENGTH_LONG).show();

        try {
            origin = geoLocate(orig);
            destination = geoLocate(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.addMarker(new MarkerOptions()
                .title("Indulás")
                .position(origin));
        map.addMarker(new MarkerOptions()
                .title("Érkezés")
                .position(destination));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 13));
    }

    public LatLng geoLocate(String location) throws IOException{
        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(location,1);
        Address addr = list.get(0);
        double lat = addr.getLatitude();
        double lng = addr.getLongitude();

        LatLng ll = new LatLng(lat, lng);
        return ll;



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
}
