package com.surfmaster.consigliaviaggi.ui.map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.ClusterManager;
import com.surfmaster.consigliaviaggi.R;
import com.surfmaster.consigliaviaggi.models.Accommodation;

import java.util.List;
import java.util.Random;

public class AccommodationMapFragment extends Fragment implements ClusterManager.OnClusterItemInfoWindowClickListener<MyClusterItem>{


    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1;
    private static final String TAG = "Map Fragment";
    private GoogleMap gMap;
    private CameraPosition mCameraPosition;
    private ClusterManager<MyClusterItem> mClusterManager;
    private Button redoSearchButton;
    private static final int DEFAULT_ZOOM = 11;
    /*edit my location*/
    private Location mLastKnownLocation;
    private  boolean mLocationPermissionGranted;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private LatLng mDefaultLocation;
    /*end edit*/

    private AccommodationMapViewModel accommodationMapViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        accommodationMapViewModel =
                new ViewModelProvider(this).get(AccommodationMapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        final TextView textView = root.findViewById(R.id.text_map);
        redoSearchButton = root.findViewById(R.id.redo_search_button);


        accommodationMapViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        mDefaultLocation=accommodationMapViewModel.getDefLocation();
        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.clear(); //clear old markers

                gMap = mMap;
                mClusterManager = new ClusterManager<>(requireActivity(), gMap);
                mClusterManager.setRenderer(new MyClusterRenderer(requireActivity(), gMap, mClusterManager));
                gMap.setOnCameraIdleListener(mClusterManager);
                gMap.setOnMarkerClickListener(mClusterManager);

                gMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
                gMap.setOnInfoWindowClickListener(mClusterManager);
                mClusterManager.setOnClusterItemInfoWindowClickListener(AccommodationMapFragment.this);

                // Prompt the user for permission.
                getLocationPermission();

                // Turn on the My Location layer and the related control on the map.
                updateLocationUI();

                //showDefaultLocation();

                //if(mLastKnownLocation==null)
                // Get the current location of the device and set the position of the map.
                final LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );

                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    buildAlertMessageNoGps();
                } else{
                    getDeviceLocation();
                }
                //getDeviceLocation();
                redoSearchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        accommodationMapViewModel.setAccommodationList(mMap.getCameraPosition().target);
                        showLocation(mMap.getCameraPosition().target);
                    }
                });

                setMapListObserver();

            }


        });

        return root;
    }

    @Override
    public void onClusterItemInfoWindowClick(MyClusterItem myClusterItem) {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        Bundle args = new Bundle();
        args.putString("title", myClusterItem.getTitle());
        args.putString("category", myClusterItem.getCategory().toString());
        args.putFloat("rating", myClusterItem.getRating());
        args.putString("address", myClusterItem.getAddress());
        args.putInt("id",myClusterItem.getId());
        args.putString("logo",myClusterItem.getLogo());
        bottomSheetFragment.setArguments(args);
        if (getFragmentManager() != null) {
            bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());
        }
    }



    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                    );
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    final LocationManager manager = (LocationManager) requireActivity().getSystemService( Context.LOCATION_SERVICE );

                    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                        buildAlertMessageNoGps();
                    } else{
                        mLocationPermissionGranted = true;
                        gMap.setMyLocationEnabled(true);
                        getDeviceLocation();
                    }

                }
                else {
                    showDefaultLocation();
                }
            }

        }
        updateLocationUI();
    }


    private void updateLocationUI() {
        if (gMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                gMap.setMyLocationEnabled(true);
                gMap.getUiSettings().setMyLocationButtonEnabled(true);
                //getDeviceLocation();
            } else {
                gMap.setMyLocationEnabled(false);
                gMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;

            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void showLocation(LatLng mDefaultLocation) {
        Random rand = new Random(); //instance of random class
        float random = rand.nextFloat();
        CameraPosition defaultPosition = CameraPosition.builder()
                .target(mDefaultLocation)
                .zoom(DEFAULT_ZOOM+random)
                .bearing(0)
                .tilt(45)
                .build();

        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(defaultPosition), 3000, null);
    }
    private void showDefaultLocation(){

        accommodationMapViewModel.setAccommodationList(mDefaultLocation);
        CameraPosition defaultPosition = CameraPosition.builder()
                .target(mDefaultLocation)
                .zoom(DEFAULT_ZOOM)
                .bearing(0)
                .tilt(45)
                .build();

        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(defaultPosition), 3000, null);
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                if(mLastKnownLocation==null){
                    Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                    locationResult.addOnCompleteListener(requireActivity(), new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {

                                // Set the map's camera position to the current location of the device.
                                mLastKnownLocation = (Location) task.getResult();

                                if(mLastKnownLocation!=null) {
                                    LatLng newLatLon = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                                    //Set accommodation list
                                    accommodationMapViewModel.setAccommodationList(newLatLon);
                                    showLocation(newLatLon);
                                    //move camera
                                }else{
                                    Log.d(TAG, "mLastKnownLocation is null. Using defaults.");
                                    Log.e(TAG, "Exception: %s", task.getException());
                                    showDefaultLocation();
                                }



                            } else {
                                Log.d(TAG, "Current location is null. Using defaults.");
                                Log.e(TAG, "Exception: %s", task.getException());
                                showDefaultLocation();

                                gMap.getUiSettings().setMyLocationButtonEnabled(false);
                            }
                        }
                    });

                }else {
                    //move camera
                    CameraPosition myPosition = mCameraPosition;
                    gMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition), 3000, null);

                }

            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void setMapListObserver(){
        accommodationMapViewModel.getAccommodationList().observe(AccommodationMapFragment.this, new Observer<List<Accommodation>>() {

            @Override
            public void onChanged(@Nullable List<Accommodation> accommodationList) {

                gMap.clear();
                mClusterManager.clearItems();

                if(accommodationList!=null) {
                    int i;
                    for (i =0; i < accommodationList.size(); i++) {
                        Accommodation ac = accommodationList.get(i);
                        MyClusterItem myitem = new MyClusterItem(new LatLng(ac.getLatitude(), ac.getLongitude()),
                                ac.getName(),
                                ac.getSubcategory(),
                                ac.getImages().get(0),
                                ac.getAddress(),
                                ac.getRating(),
                                ac.getId()
                        );
                        mClusterManager.addItem(myitem);

                    }

                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (gMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, gMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        showDefaultLocation();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}