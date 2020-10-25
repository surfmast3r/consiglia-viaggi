package com.surfmaster.consigliaviaggi.ui.map;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.R;

class MyClusterRenderer extends DefaultClusterRenderer<MyClusterItem> {

    public MyClusterRenderer(Context context, GoogleMap map,
                             ClusterManager<MyClusterItem> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(MyClusterItem item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);

        markerOptions.title(item.getTitle())
        .snippet(item.getAddress());
        markerOptions.icon(getMarker(item.getCategory().toString()));
    }

    @Override
    protected void onClusterItemRendered(MyClusterItem clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
        //here you have access to the marker itself
    }
    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        return cluster.getSize() > 2;
    }

    public BitmapDescriptor getMarker(String cat){

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.default_marker);
        switch (cat) {
            case Constants.TRATTORIA:
                icon = BitmapDescriptorFactory.fromResource(R.drawable.restaurant_marker);
                break;
            case Constants.PIZZERIA:
                icon = BitmapDescriptorFactory.fromResource(R.drawable.pizza_marker);
                break;
            case Constants.BAR:
                icon = BitmapDescriptorFactory.fromResource(R.drawable.bar_marker);
                break;
            case Constants.MUSEUM:
                icon = BitmapDescriptorFactory.fromResource(R.drawable.attraction_marker);
                break;
            case Constants.PARK:
                icon = BitmapDescriptorFactory.fromResource(R.drawable.park_marker);
                break;
            case Constants.BNB:
                icon = BitmapDescriptorFactory.fromResource(R.drawable.beb_marker);
                break;
            case Constants.HOSTEL:
            case Constants.CATEGORY_HOTEL:
                icon = BitmapDescriptorFactory.fromResource(R.drawable.hotel_marker);
                break;
        }
        return icon;
    }


}


