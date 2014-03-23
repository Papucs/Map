package demo.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;



public class MainActivity extends Activity {

    String orig , dest;
    EditText o,d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        o = (EditText) findViewById(R.id.origin);
        d = (EditText) findViewById(R.id.destination);
        //loc.setOrigin(getCoordinates(orig.getText().toString()));
        //loc.seLatLng origintDestination(getCoordinates(dest.getText().toString()));
        //String o = origin.toString();

    }


    public void newActivity(View v){
        orig = o.getText().toString();

        dest = d.getText().toString();

        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        intent.putExtra("origin",orig);
        intent.putExtra("destination", dest);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
