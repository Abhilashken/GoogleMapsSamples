package com.flaierovation_studios.googlemapssamples;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * Created by Abhilash on 23-Dec-16.
 */


public class MarkerEventMap extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mGoogleMap;

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

    private void setMarker(String strLocation, String strLocality, double lat, double lng) {
        if (marker != null) {
            marker.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions()
                .title(strLocation)
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .position(new LatLng(lat, lng))
                .snippet(strLocation + "," + strLocality);
        mGoogleMap.addMarker(markerOptions);
    }

    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng mLatLng = new LatLng(lat, lng);
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngZoom(mLatLng, zoom);
        mGoogleMap.animateCamera(mCameraUpdate);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (mGoogleMap!=null){
            mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    Geocoder mGeocoder= new Geocoder(getApplicationContext());
                    LatLng mLatLng= marker.getPosition();
                    List<Address> addressList2 = null;

                    try {
                        addressList2= mGeocoder.getFromLocation(mLatLng.latitude,mLatLng.longitude,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Address address=addressList2.get(0);
                    marker.setTitle(address.getSubLocality()+","+address.getLocality());
                    marker.showInfoWindow();

                }
            });
        }



        // process for info window with marker as follows
        if (mGoogleMap != null) {

            mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View view = getLayoutInflater().inflate(R.layout.show_info_window, null);
                    TextView tv_locality = view.findViewById(R.id.tv_locality);
                    TextView tv_lat = view.findViewById(R.id.tv_latitude);
                    TextView tv_lng = view.findViewById(R.id.tv_longitude);
                    TextView tv_snippet = view.findViewById(R.id.tv_snippet);

                    LatLng mLatLng = marker.getPosition();

                    tv_locality.setText(marker.getTitle());
                    tv_lat.setText("Latitude: " + mLatLng.latitude);
                    tv_lng.setText("Longitude: " + mLatLng.longitude);
                    tv_snippet.setText(marker.getSnippet());


                    return view;
                }
            });
        }
    }
}
