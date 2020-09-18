package luck.materialdesign.tabsnavigator.tabs;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.core.GeoHash;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.activities.freelancerOtherAccount;
import luck.materialdesign.tabsnavigator.model.accountModel;
import luck.materialdesign.tabsnavigator.utils.GET_CURRENT_LOCATION;

import static android.content.Context.MODE_PRIVATE;


public class freelancerLocationSearchFragment extends Fragment implements OnMapReadyCallback,GoogleMap.OnCameraChangeListener, GoogleMap.OnMarkerClickListener {


    private static GET_CURRENT_LOCATION myLocation;
    SharedPreferences sPref;
    private static GeoLocation INITIAL_CENTER;

    private static int INITIAL_ZOOM_LEVEL = 14;
    //  private static final String GEO_FIRE_DB = "https://publicdata-transit.firebaseio.com";
    //  private static final String GEO_FIRE_REF = GEO_FIRE_DB + "/_geofire";

    DatabaseReference GEO_FIRE_DB;
    GeoFire GEO_FIRE_REF;

    //   myDatabase= FirebaseDatabase.getInstance().getReference();
    // geofire_Donors = new GeoFire(myDatabase.child("Donors_Locations"));
    private GoogleMap map;
    private Circle searchCircle;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    int page = 0;
    String child;
    private GoogleMap mMap;
    // private Map<String,Marker> markers;
    DatabaseReference myDatabase;
    //  GeoFire geofire_Donors;
    private HashMap<String, Marker> markers = new HashMap<>();
    private HashMap<Integer, String> userChilds = new HashMap<>();
    FirebaseDatabase _root;
    DatabaseReference databaseForTittle;



    String markTittle;
    //  MapView mMapView;

    public static freelancerLocationSearchFragment newInstance(int position, String group) {

        Bundle args = new Bundle();
        args.putInt("page", position);
        args.putString("group", group);
        freelancerLocationSearchFragment fragment = new freelancerLocationSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.search_freelancer_location, container, false);
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

//        page = getArguments().getInt("page");
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        child = getArguments().getString("group");

//        mapFragment.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                if (googleMap == null) {
//                    Log.d("MAPA_FRAGMENT", "Sorry! unable to create maps");
//                } else {
//                    map = googleMap;
//                }
//            }
//        });

        _root = FirebaseDatabase.getInstance();

        databaseForTittle = _root.getReference();

        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        return view;


    }


    private void animateMarkerTo(final Marker marker, final double lat, final double lng) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long DURATION_MS = 3000;
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final LatLng startPosition = marker.getPosition();
        handler.post(new Runnable() {
            @Override
            public void run() {
                float elapsed = SystemClock.uptimeMillis() - start;
                float t = elapsed/DURATION_MS;
                float v = interpolator.getInterpolation(t);

                double currentLat = (lat - startPosition.latitude) * v + startPosition.latitude;
                double currentLng = (lng - startPosition.longitude) * v + startPosition.longitude;
                marker.setPosition(new LatLng(currentLat, currentLng));

                // if animation is not finished yet, repeat
                if (t < 1) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }
    private double zoomLevelToRadius(double zoomLevel) {
        // Approximation to fit circle into view
        return 16384000/Math.pow(2, zoomLevel);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        // Update the search criteria for this geoQuery and the circle on the map
        LatLng center = cameraPosition.target;
        double radius = zoomLevelToRadius(cameraPosition.zoom);
        this.searchCircle.setCenter(center);
        this.searchCircle.setRadius(radius);
//        if (center != null) {
        this.geoQuery.setCenter(new GeoLocation(center.latitude, center.longitude));
        // radius in km
        this.geoQuery.setRadius(radius / 1000);
//        }
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        map = googleMap;
        map.setOnMarkerClickListener(this);
        myLocation = new GET_CURRENT_LOCATION(getContext());

        INITIAL_CENTER = new GeoLocation(myLocation.getLatitude(), myLocation.getLongitude());
        Log.d("POSITION!!!!!", String.valueOf(page));

        LatLng latLngCenter = new LatLng(INITIAL_CENTER.latitude, INITIAL_CENTER.longitude);
        this.searchCircle = this.map.addCircle(new CircleOptions().center(latLngCenter).radius(1000));
        this.searchCircle.setFillColor(Color.argb(66, 255, 0, 255));
        this.searchCircle.setStrokeColor(Color.argb(66, 0, 0, 0));
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCenter, INITIAL_ZOOM_LEVEL));
        this.map.setOnCameraChangeListener(this);
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.map.setMyLocationEnabled(true);
        //  FirebaseOptions options = new FirebaseOptions.Builder().setApplicationId("geofire").setDatabaseUrl(GEO_FIRE_DB).build();
        // FirebaseApp app = FirebaseApp.initializeApp(this, options);

        geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference("locations").child(child));


        Log.d("GEO FIRE POSITION", page+ " " + geoFire.toString());
        // radius in km
        this.geoQuery = this.geoFire.queryAtLocation(INITIAL_CENTER, 1);
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getActivity(), freelancerOtherAccount.class);
                intent.putExtra("childname", String.valueOf(userChilds.get(marker.hashCode())));
                getActivity().startActivity(intent);
            }
        });
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                Log.d("GeoFire", key);
                Log.d("GeoFire", location.toString());
                try
                {
                FirebaseDatabase.getInstance().getReference("users").child(key.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null){
                            accountModel user = dataSnapshot.getValue(accountModel.class);
                            Calendar cl = Calendar.getInstance();
                            cl.setTimeInMillis(Long.parseLong(user.getLastActive()));
                            Calendar cl2 = Calendar.getInstance();
                            cl2.setTimeInMillis(System.currentTimeMillis());
                            Log.d("CAKLKK", "TRUE");
                            Log.d("CALENDAR1", user.getLastActive());
                            Log.d("CALENDAR2", String.valueOf(System.currentTimeMillis()));
                            if (converteTimestamp(user.getLastActive())) {
                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(new LatLng(location.latitude, location.longitude))
                                        .title(user.getName());
                                Marker marker = map.addMarker(markerOptions);
//                                userChilds.put(marker.hashCode(), key);
                                markers.put(key, marker);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                }catch (NullPointerException e){

                }

            }

            @Override
            public void onKeyExited(String key) {
                Marker marker = markers.get(key);
                if (marker != null) {
                    marker.remove();
                    markers.remove(key);
                }
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                Marker marker =markers.get(key);
                if (marker != null) {
                    animateMarkerTo(marker, location.latitude, location.longitude);
                }
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Error")
                        .setMessage("There was an unexpected error querying GeoFire: " + error.getMessage())
                        .setPositiveButton(android.R.string.ok, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

    }

    private boolean converteTimestamp(String mileSegundos){
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(Long.parseLong(mileSegundos));
        //here your time in miliseconds
        Calendar cl2 = Calendar.getInstance();
        cl2.setTimeInMillis(System.currentTimeMillis());
        Log.d("DIFFER", String.valueOf(cl2.get(Calendar.MINUTE) - cl.get(Calendar.MINUTE)));
        if(cl2.get(Calendar.MINUTE) - cl.get(Calendar.MINUTE) < 15){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    INITIAL_CENTER = new GeoLocation(myLocation.getLatitude(), myLocation.getLongitude());

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    Toast.makeText(getContext(), "Please enable location for better performance", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

//        searchTab searchTab = new searchTab();
////        Retrofit retrofit = new Retrofit.Builder()
////                .baseUrl("https://maps.googleapis.com")
////                .addConverterFactory(GsonConverterFactory.create())
////                .build();
////
////        RouteApi routeService = retrofit.create(RouteApi.class);
////        RouteResponse routeResponse = routeService.getRoute(myLocation.getLatitude() + " " + myLocation.getLongitude(), marker.getPosition().latitude + " " + marker.getPosition().longitude, true, "ru");
////
//
//        sPref = getActivity().getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor ed = sPref.edit();
//        ed.putString("child", child);
//        ed.putString("key", marker.getTitle());
//        ed.putString("lat1", String.valueOf(myLocation.getLatitude()));
//        ed.putString("long1", String.valueOf(myLocation.getLongitude()));
//        ed.putString("lat2", String.valueOf(marker.getPosition().latitude));
//        ed.putString("long2", String.valueOf(marker.getPosition().longitude));
//        ed.commit();
//        FragmentTransaction fTrans;
//        fTrans = getFragmentManager().beginTransaction();
//        fTrans.remove(hospitalSearch.this);
//        fTrans.replace(R.id.frame_container, new marketFragment() );
//        fTrans.addToBackStack(null);
//        fTrans.commit();
//        searchTab.setMarkerFragment(child, marker.getTitle());
        return false;
    }




}


