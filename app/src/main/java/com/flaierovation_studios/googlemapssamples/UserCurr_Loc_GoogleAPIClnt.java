package com.flaierovation_studios.googlemapssamples;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by Abhilash on 22-Dec-17.
 */

public class UserCurr_Loc_GoogleAPIClnt extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleApiClient mGoogleApiClient;
    GoogleMap mGoogleMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleServiceAvailabilityCheck()) {
            Toast.makeText(this, "Perfect !!", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activities_dynamic_location);

            initMap();
        }

    }


    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragmentDynamic);
        mapFragment.getMapAsync(this);
    }

    private boolean googleServiceAvailabilityCheck() {
        GoogleApiAvailability mGoogleApiAvailability = GoogleApiAvailability.getInstance();
        int isAvailable = mGoogleApiAvailability.isGooglePlayServicesAvailable(this);

        //check if Connection successful
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (mGoogleApiAvailability.isUserResolvableError(isAvailable)) {
            Dialog mDialog = mGoogleApiAvailability.getErrorDialog(this, isAvailable, 0);
            mDialog.show();
        } else {
            Toast.makeText(this, "Can't connect to Play Service", Toast.LENGTH_LONG).show();
        }
        return false;
    }


    public void geoLocate(View view) throws IOException {
        EditText eTLocation = findViewById(R.id.editText);
        String strLocation = eTLocation.getText().toString();

        //Using Geocoder class to convert any string(entered location) to
        //to its recognised latitude and longitude
        Geocoder mGeocoder = new Geocoder(this);
        List<Address> addressList = mGeocoder.getFromLocationName(strLocation, 1);
        Address address = addressList.get(0);
        String strLocality = address.getLocality();
        Toast.makeText(this, "" + strLocality, Toast.LENGTH_LONG).show();
        double lat = address.getLatitude();
        double lng = address.getLongitude();

        goToLocationZoom(lat, lng, 15);

    }

    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng mLatLng = new LatLng(lat, lng);
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngZoom(mLatLng, zoom);
        mGoogleMap.animateCamera(mCameraUpdate);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        //Initialising GoogleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

    //Using class LocationRequest to request user location

    LocationRequest mLocationRequest;

    //Initialising LocationRequest object mLocationRequest after Connection Successful i.e. onConnected()
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(this, "Can't get current location !!", Toast.LENGTH_SHORT).show();
        }else {
            LatLng mLatLng= new LatLng(location.getLatitude(),location.getLongitude());
            CameraUpdate mCameraUpdate= CameraUpdateFactory.newLatLngZoom(mLatLng,15);
            mGoogleMap.animateCamera(mCameraUpdate);
        }

    }
}
