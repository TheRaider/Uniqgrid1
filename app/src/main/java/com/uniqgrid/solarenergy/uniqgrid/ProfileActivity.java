package com.uniqgrid.solarenergy.uniqgrid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class ProfileActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener  {

    private TextView mPlaceDetailsText;
    private TextView mPlaceAttribution;
    NestedScrollView nsAddEst;
    HashMap<String,Place> markersList = new HashMap<>();



    private GoogleMap mMap;
    private GoogleMap googleMap;

    SupportMapFragment mapFragment;

    MapView mapView;
    LatLng placeLatLng  =  null;

    int PLACE_PICKER_REQUEST = 1;


    ImageView ivBack;


    FloatingActionButton fabForward;

    TextView tvEstName,tvSegment,tvSancLoad;
    TextView tvOwnerName,tvOwnerPhone,tvOwnerEmail;
    TextView tvPOCName,tvPOCPhone,tvPOCEmail;
    TextView tvStreetAddress,tvArea,tvCity,tvState,tvPostalCode,tvGpsCoordinates;

    String estName ="-" ,segment ="-" ,sancLoad ="-" ,ownerName ="-" ,ownerPhone ="-" ;
    String ownerEmail ="-" ,pocName ="-" ,pocPhone ="-" ,pocEmail ="-" ;
    String streetAddress ="-" ,area ="-" ,city ="-" ,state ="-" ,postalCode ="-" ,gpsCoordinates ="-" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        overridePendingTransition(R.anim.slide_in_up,R.anim.hold);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Profile");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Retrieve the TextViews that will display details about the selected place.
        mPlaceDetailsText = (TextView) findViewById(R.id.place_details);
        mPlaceAttribution = (TextView) findViewById(R.id.place_attribution);

        nsAddEst = (NestedScrollView) findViewById(R.id.nsAddEst);


        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });











        // Solving clash between scrolls of map and scroll view
        final ImageView transparentImageView = (ImageView) findViewById(R.id.transparent_image);

        transparentImageView.setOnTouchListener(
                new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                                // Disallow ScrollView to intercept touch events.
                                nsAddEst.requestDisallowInterceptTouchEvent(true);
                                // Disable touch on transparent view
                                return false;

                            case MotionEvent.ACTION_UP:
                                v.performClick();
                                // Allow ScrollView to intercept touch events.
                                nsAddEst.requestDisallowInterceptTouchEvent(false);
                                return true;

                            case MotionEvent.ACTION_MOVE:
                                nsAddEst.requestDisallowInterceptTouchEvent(true);
                                return false;

                            default:
                                return true;
                        }

                    }
                });






        // Initialise views
        tvEstName = (TextView) findViewById(R.id.tvEstName);
        tvSegment = (TextView) findViewById(R.id.tvSegment);
        tvSancLoad = (TextView) findViewById(R.id.tvSancLoad);
        tvOwnerName = (TextView) findViewById(R.id.tvOwnerName);
        tvOwnerPhone = (TextView) findViewById(R.id.tvOwnerPhone);
        tvOwnerEmail = (TextView) findViewById(R.id.tvOwnerEmail);
        tvPOCName = (TextView) findViewById(R.id.tvPOCName);
        tvPOCPhone = (TextView) findViewById(R.id.tvPOCPhone);
        tvPOCEmail = (TextView) findViewById(R.id.tvPOCEmail);
        tvStreetAddress = (TextView) findViewById(R.id.tvStreetAddress);
        tvArea = (TextView) findViewById(R.id.tvArea);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvState = (TextView) findViewById(R.id.tvState);
        tvPostalCode = (TextView) findViewById(R.id.tvPostalCode);
        tvGpsCoordinates = (TextView) findViewById(R.id.tvGpsCoordinates);



        prepareData();
        loadData();



    }



    public  void prepareData(){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
        String content = app_preferences.getString("content","abcd");
        if(!content.equals("abcd")){
            try {

                JSONObject contentJson = new JSONObject(content);
                estName = contentJson.getString("Name of the establishment");
                segment = contentJson.getString("Segment");
                sancLoad = contentJson.getString("Sanctioned load in kW");
                ownerName = contentJson.getString("Owner");
                ownerPhone = contentJson.getString("Owner's Phone");
                ownerEmail = contentJson.getString("Email");
                pocName = contentJson.getString("Point of Contact (PoC)");
                pocPhone = contentJson.getString("PoC's Phone");
                streetAddress = contentJson.getString("Street Address");
                area = contentJson.getString("Area");
                city = contentJson.getString("City");
                state = contentJson.getString("State");
                postalCode = contentJson.getString("Postal Code");
                gpsCoordinates = contentJson.getString("GPS Coordinate");

                setLatLng(gpsCoordinates);




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void setLatLng(String coordinates){

        try{
            double lat = Double.parseDouble(coordinates.split(",")[0].trim());
            double longit = Double.parseDouble(coordinates.split(",")[1].trim());
            placeLatLng = new LatLng(lat,longit);
        }catch (Exception e){

        }
    }

    public  void loadData(){
        tvEstName.setText(estName);
        tvSegment.setText(segment);
        tvSancLoad.setText(sancLoad);
        tvOwnerName.setText(ownerName);
        tvOwnerPhone.setText(ownerPhone);
        tvOwnerEmail.setText(ownerEmail);
        tvPOCName.setText(pocName);
        tvPOCPhone.setText(pocPhone);
        tvPOCEmail.setText(pocEmail);
        tvStreetAddress.setText(streetAddress);
        tvArea.setText(area);
        tvCity.setText(city);
        tvState.setText(state);
        tvPostalCode.setText(postalCode);
        tvGpsCoordinates.setText(gpsCoordinates);




    }
    public void getLocation(){

        mapFragment.getMapAsync(this);
        setLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // Get the SupportMapFragment and request notification
                // when the map is ready to be used.
                mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);

                mapFragment.getMapAsync(ProfileActivity.this);
            }
        },1000);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setLocation();
        // Zoom Buttons
       // mMap.getUiSettings().setZoomControlsEnabled(true);
        // Setting Map Type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

      /* // Requesting Permissions for location
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);


        }else {
            ActivityCompat.requestPermissions(AddEstablishmentActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
        */

        mMap.setOnMarkerClickListener(this);
        setLocation();

    }



    public void setLocation(){
        if(placeLatLng !=null) {
            try {
                mMap.clear();
                MarkerOptions markerOptions = new MarkerOptions().position(placeLatLng).title(estName);
                //   markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_trends));
                Marker marker = mMap.addMarker(markerOptions);

                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                        placeLatLng).zoom(14).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }catch (Exception e){
                LatLng tempLatLng = new LatLng(25.317645,82.973914);
                mMap.clear();
                MarkerOptions markerOptions = new MarkerOptions().position(tempLatLng).title(estName);
                //   markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_trends));
                Marker marker = mMap.addMarker(markerOptions);

                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                        tempLatLng).zoom(14).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return true;
        }
    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.hold, R.anim.slide_down);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }
}
