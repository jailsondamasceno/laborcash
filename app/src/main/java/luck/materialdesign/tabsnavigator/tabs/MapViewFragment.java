package luck.materialdesign.tabsnavigator.tabs;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

import luck.materialdesign.tabsnavigator.R;
import luck.materialdesign.tabsnavigator.activities.freelancerOtherAccount;
import luck.materialdesign.tabsnavigator.model.accountModel;
import luck.materialdesign.tabsnavigator.utils.GET_CURRENT_LOCATION;

/**
 * Created by BeS on 16.09.2017.
 */

public class MapViewFragment extends Fragment{

    MapView mMapView;
    private GoogleMap googleMap;

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

    public static MapViewFragment newInstance(int position, String group) {

        Bundle args = new Bundle();
        args.putInt("page", position);
        args.putString("group", group);
        MapViewFragment fragment = new MapViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_freelancer_location, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                map = mMap;
                map.setMyLocationEnabled(true);
                myLocation = new GET_CURRENT_LOCATION(getContext());

                INITIAL_CENTER = new GeoLocation(myLocation.getLatitude(), myLocation.getLongitude());
                Log.d("POSITION!!!!!", String.valueOf(page));

                LatLng latLngCenter = new LatLng(INITIAL_CENTER.latitude, INITIAL_CENTER.longitude);
                searchCircle = map.addCircle(new CircleOptions().center(latLngCenter).radius(1000));
                searchCircle.setFillColor(Color.argb(66, 255, 0, 255));
                searchCircle.setStrokeColor(Color.argb(66, 0, 0, 0));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCenter, INITIAL_ZOOM_LEVEL));
                map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition cameraPosition) {
                        LatLng center = cameraPosition.target;
                        double radius = zoomLevelToRadius(cameraPosition.zoom);
                        searchCircle.setCenter(center);
                        searchCircle.setRadius(radius);
//        if (center != null) {
                        geoQuery.setCenter(new GeoLocation(center.latitude, center.longitude));
                        // radius in km
                        geoQuery.setRadius(radius / 1000);
                    }
                });
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
                map.setMyLocationEnabled(true);
                //  FirebaseOptions options = new FirebaseOptions.Builder().setApplicationId("geofire").setDatabaseUrl(GEO_FIRE_DB).build();
                // FirebaseApp app = FirebaseApp.initializeApp(this, options);

                geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference("locations").child(child));


                Log.d("GEO FIRE POSITION", page+ " " + geoFire.toString());
                // radius in km
                geoQuery = geoFire.queryAtLocation(INITIAL_CENTER, 1);
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
                        FirebaseDatabase.getInstance().getReference("users").child(key.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null){
                                    accountModel user = dataSnapshot.getValue(accountModel.class);
                                    Calendar cl = Calendar.getInstance();
                                    cl.setTimeInMillis(Long.parseLong(user.getLastActive()));
                                    Calendar cl2 = Calendar.getInstance();
                                    cl2.setTimeInMillis(System.currentTimeMillis());
                                    Long milliseconds = System.currentTimeMillis() - Long.parseLong(user.getLastActive());
                                    Log.d("DIFFER", String.valueOf(milliseconds / (60 * 1000)));
                                    if (milliseconds / (60 * 1000) < 15) {
                                        MarkerOptions markerOptions = new MarkerOptions()
                                                .position(new LatLng(location.latitude, location.longitude))
                                                .title(user.getName());

                                        Marker marker = map.addMarker(markerOptions);
                                        userChilds.put(marker.hashCode(), key);
                                        markers.put(key, marker);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

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
        });




        return rootView;
    }




    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
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




}
