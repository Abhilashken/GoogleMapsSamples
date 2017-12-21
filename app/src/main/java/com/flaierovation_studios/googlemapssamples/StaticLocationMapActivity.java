package com.flaierovation_studios.googlemapssamples;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

        if (googleServicesAvaliablityCheck()){
            Toast.makeText(this, "Perfect !!", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_static_location_map);

            initMap();
        }else {
            //No google map layout
        }
    }

    private void initMap() {
        MapFragment mapFragment= (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this); //this will implement a callback method onMapReady
    }

    private boolean googleServicesAvaliablityCheck() {
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

    public void geoLocate(View view) {
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
}
