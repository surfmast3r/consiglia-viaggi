package com.surfmaster.consigliaviaggi.ui.map;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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

public class AccommodationMapFragment extends Fragment implements ClusterManager.OnClusterItemInfoWindowClickListener<MyClusterItem>{


    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1;
    private static final String TAG = "Map Fragment";
    private GoogleMap gMap;
    private CameraPosition mCameraPosition;
    private ClusterManager<MyClusterItem> mClusterManager;
    private static final int DEFAULT_ZOOM = 11;
    /*edit my location*/
    private Location mLastKnownLocation;
    private  boolean mLocationPermissionGranted;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(45.463619, 9.188120);
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
                ViewModelProviders.of(this).get(AccommodationMapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        final TextView textView = root.findViewById(R.id.text_map);
        accommodationMapViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.clear(); //clear old markers

                gMap = mMap;
                mClusterManager = new ClusterManager<MyClusterItem>(requireActivity(), gMap);
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

                //if(mLastKnownLocation==null)
                // Get the current location of the device and set the position of the map.
                getDeviceLocation();


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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mLocationPermissionGranted = true;
                    gMap.setMyLocationEnabled(true);
                    getDeviceLocation();
                } else {
                    // no granted
                    showDefaultLocation();
                    //new Utils(activity).buildPermissionAlert(activity,"La mappa non può localizzarti i permessi per la localizzazione non sono attivi, attivali dal menu 'Autorizzazioni' di Android");
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
                    Task locationResult = mFusedLocationProviderClient.getLastLocation();
                    locationResult.addOnCompleteListener(requireActivity(), new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {

                                // Set the map's camera position to the current location of the device.
                                mLastKnownLocation = (Location) task.getResult();

                                //Set accommodation list
                                accommodationMapViewModel.setAccommodationList(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()));

                                //move camera
                                CameraPosition myPosition = CameraPosition.builder()
                                        .target(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()))
                                        .zoom(DEFAULT_ZOOM)
                                        .bearing(0)
                                        .tilt(45)
                                        .build();

                                gMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition), 3000, null);
                            } else {
                                Log.d(TAG, "Current location is null. Using defaults.");
                                Log.e(TAG, "Exception: %s", task.getException());
                                CameraPosition defaultPosition = CameraPosition.builder()
                                        .target(mDefaultLocation)
                                        .zoom(DEFAULT_ZOOM)
                                        .bearing(0)
                                        .tilt(45)
                                        .build();

                                gMap.animateCamera(CameraUpdateFactory.newCameraPosition(defaultPosition), 3000, null);
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
        accommodationMapViewModel.getAccommodationList().observe(AccommodationMapFragment.this, new Observer<List>() {

            @Override
            public void onChanged(@Nullable List s) {
                List<Accommodation> accommodationList = s;

                gMap.clear();
                mClusterManager.clearItems();

                for (int i = 0; i < accommodationList.size(); i++) {
                    Accommodation ac = accommodationList.get(i);
                    MyClusterItem myitem = new MyClusterItem(new LatLng(ac.getLatitude(), ac.getLongitude()),
                            ac.getName(),
                            ac.getSubcategory(),
                            ac.getLogoUrl(),
                            ac.getAddress(),
                            ac.getRating(),
                            ac.getId()
                    );
                    mClusterManager.addItem(myitem);

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

}