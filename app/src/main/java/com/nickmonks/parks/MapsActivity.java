package com.nickmonks.parks;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nickmonks.parks.adapter.CustomInfoWindow;
import com.nickmonks.parks.controller.AppController;
import com.nickmonks.parks.data.AsyncResponse;
import com.nickmonks.parks.data.Repository;
import com.nickmonks.parks.model.Park;
import com.nickmonks.parks.model.ParkViewModel;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {


    private GoogleMap mMap;
    private ParkViewModel parkViewModel;
    private List<Park> parkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Set the park ViewModel to exchange dats
        parkViewModel = new ViewModelProvider(MapsActivity.this)
                .get(ParkViewModel.class);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); // we send this since this class implements the onMapReady Callback


        // Bottom Navegation Bar
        BottomNavigationView bottomNavigationView =
                findViewById(R.id.bottom_navigation);

        // Select one of the navegation buttons
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                // Fragment of the Park
                Fragment selectedFragment = null;

                int id = item.getItemId();

                if (id == R.id.maps_nav_button){

                    // Clean up the map
                    mMap.clear();

                    // show map view
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.map, mapFragment)
                            .commit();

                    mapFragment.getMapAsync(MapsActivity.this); // we send this since this class implements the onMapReady Callback

                    return true;

                } else if (id == R.id.parks_bav_button){
                    // show park view and show the fragment
                    selectedFragment = ParksFragment.newInstance();

                }

                // inflate the fragment! - we bbegin a transaction to replace the map to the selected fragment
                // we do this to quickly swap them
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map, selectedFragment)
                        .commit();

                return true;
            }
        });


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //set the adapter of our Custom Info Window, so when we click it we retrieve the desired
        // layout
        mMap.setInfoWindowAdapter(new CustomInfoWindow(getApplicationContext()));

        // register click event - this class implements its so we pass this
        mMap.setOnInfoWindowClickListener(this);

        parkList = new ArrayList<>();
        parkList.clear(); // clean slate! - to not duplicate the data

        Repository.getParks(new AsyncResponse() {
            @Override
            public void processPark(List<Park> parks) {

                parkList = parks;

                for (Park park : parks){

                    LatLng park_location =
                            new LatLng(Double.parseDouble(park.getLatitude()),
                                    Double.parseDouble(park.getLongitude()));

                    //Add metadata in our marker - this is how to add properties to the marker
                    MarkerOptions markerOptions =
                            new MarkerOptions()
                            .position(park_location)
                            .title(park.getName())
                                    .icon(BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_VIOLET
                                    ))
                            .snippet(park.getStates());

                    // We set more info on the marker - for example, the tag to use it
                    Marker marker = mMap.addMarker(markerOptions);
                    marker.setTag(park);

                    mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(park_location, 5));

                    Log.d("Parks", "processPark: "+ park.getFullName());
                }

                // We will get all the data and the parks here - so we can retrieve it in the viewAdapter
                parkViewModel.setSelectedParks(parkList);
            }
        });
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        goToDetailsFragment(marker);
    }

    private void goToDetailsFragment(@NonNull Marker marker) {
        // go to details fragment - we need to set the Park from the ViewModel (which implements observer)
        // We only have the marker as input,
        parkViewModel.setSelectedPark((Park) marker.getTag());

        // change the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map, new DetailsFragment())
                .commit();
    }
}