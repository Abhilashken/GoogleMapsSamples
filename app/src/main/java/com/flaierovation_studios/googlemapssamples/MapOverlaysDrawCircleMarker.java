package com.flaierovation_studios.googlemapssamples;

import android.app.Dialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * Created by Abhilash on 23-Dec-16.
 */

public class MapOverlaysDrawCircleMarker extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mGoogleMap;
    Circle mCircle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleServiceAvailabilityCheck()) {
            Toast.makeText(this, "Perfect !!", Toast.LENGTH_LONG).show();
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
            Toast.makeText(this, "Can't connect to Play Service !!", Toast.LENGTH_SHORT).show();
        }
        return false;
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


    Marker marker;

    public void geoLocate(View view) throws IOException {
        EditText eTLocation = findViewById(R.id.editText);
        String strLocation = eTLocation.getText().toString();

        //Using Geocoder class to convert any string(entered location) to
        //its recognised latitude and longitude

        Geocoder mGeocoder = new Geocoder(this);
        List<Address> addressList = mGeocoder.getFromLocationName(strLocation, 1);
        Address address = addressList.get(0);
        String strLocality = address.getLocality();
        Toast.makeText(this, "Locality: " + strLocality, Toast.LENGTH_LONG).show();

        double lat = address.getLatitude();
        double lng = address.getLongitude();

        goToLocationZoom(lat, lng, 15);

        //calling Marker method
        setMarker(strLocation, strLocality, lat, lng);

    }

    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng mLatLng = new LatLng(lat, lng);
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngZoom(mLatLng, zoom);
        mGoogleMap.animateCamera(mCameraUpdate);
    }

    //Marker definition method with drawCircle called inside
    private void setMarker(String strLocation, String strLocality, double lat, double lng) {
        if (marker != null) {
            removeEverything();
        }
        MarkerOptions markerOptions = new MarkerOptions()
                .title(strLocation)
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .position(new LatLng(lat, lng))
                .snippet(strLocation + "," + strLocality);
        mGoogleMap.addMarker(markerOptions);
        mCircle = drawCircle(new LatLng(lat, lng));

    }

    //method to remove everything previous i.e. marker and circle
    private void removeEverything() {
        marker.remove();
        marker = null;
        mCircle.remove();
        mCircle = null;
    }

    private Circle drawCircle(LatLng latLng) {
        CircleOptions mCircleOptions = new CircleOptions()
                .center(latLng)
                .radius(1000)
                .fillColor(0x33FF0000)
                .strokeColor(Color.BLUE)
                .strokeWidth(3);
        return mGoogleMap.addCircle(mCircleOptions);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (mGoogleMap != null) {

            mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    Geocoder mGeocoder = new Geocoder(getApplicationContext());

                    LatLng mLatLng = marker.getPosition();
                    List<Address> addressList2 = null;
                    try {
                        addressList2 = mGeocoder.getFromLocation(mLatLng.latitude, mLatLng.longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Address address = addressList2.get(0);
                    marker.setTitle(address.getSubLocality() + ", " + address.getLocality());
                    marker.showInfoWindow();


                }
            });

        }
    }
}
