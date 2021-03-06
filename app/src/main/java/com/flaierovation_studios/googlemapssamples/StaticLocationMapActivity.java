package com.flaierovation_studios.googlemapssamples;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class StaticLocationMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (googleServicesAvailabilityCheck()){
            Toast.makeText(this, "Perfect !!", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_static_location_map);

            initMap();
        }else {
            //No google map layout
        }
    }

    private void initMap() {
        MapFragment mapFragment= (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragmentStatic);
        mapFragment.getMapAsync(this); //this will implement a callback method onMapReady
    }

    private boolean googleServicesAvailabilityCheck() {
        GoogleApiAvailability mGoogleApiAvailability= GoogleApiAvailability.getInstance();
        int isAvailable= mGoogleApiAvailability.isGooglePlayServicesAvailable(this);

        //check if Connection success
        if (isAvailable== ConnectionResult.SUCCESS){
            return  true;
        }else if (mGoogleApiAvailability.isUserResolvableError(isAvailable)){
            Dialog mDialog=mGoogleApiAvailability.getErrorDialog(this,isAvailable,0);
            mDialog.show();

        }else {
            Toast.makeText(this, "Can't connect to Play Service", Toast.LENGTH_LONG).show();
        }
        return false;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;
        goToLocationZoom(28.583391, 77.085736,15);


    }

    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng mLatLng=new LatLng(lat,lng);
        CameraUpdate mCameraUpdate= CameraUpdateFactory.newLatLngZoom(mLatLng,zoom);
        mGoogleMap.animateCamera(mCameraUpdate);
    }

    //A simple goToLocation(lat, lng) can also be used without zoom parameter


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mapTypeNone:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;

            case R.id.mapTypeNormal:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;

            case R.id.mapTypeSatellite:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;

            case R.id.mapTypeTerrain:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;

            case R.id.mapTypeHybrid:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
