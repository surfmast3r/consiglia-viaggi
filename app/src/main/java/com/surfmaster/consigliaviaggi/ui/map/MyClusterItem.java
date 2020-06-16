package com.surfmaster.consigliaviaggi.ui.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.surfmaster.consigliaviaggi.Subcategory;


public class MyClusterItem implements ClusterItem {

    private LatLng position;
    private String title;
    private Subcategory category;
    private float rating;
    private String address;
    private int id;
    private String logo;



    public MyClusterItem(LatLng latLng, String title, Subcategory cat, String logo, String address, float acRating, int id) {
        position = latLng;
        this.title=title;
        this.category=cat;
        this.address=address;
        this.rating=acRating;
        this.id=id;
        this.logo=logo;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    public void setPosition( LatLng position ) {
        this.position = position;
    }

    public Subcategory getCategory() {
        return category;
    }
    public String getAddress() {
        return address;
    }
    public float getRating() {
        return rating;
    }
    public String getTitle() {
        return title;
    }
    public int getId() {
        return id;
    }
    public String getLogo() {
        return logo;
    }


    @Override
    public String getSnippet() {
        return null;
    }




}