package com.surfmaster.consigliaviaggi.models;


import java.util.ArrayList;
import java.util.List;

public class Category  {
    private String categoryName, categoryLabel;
    private List<Category> subcategoryList;

    public Category(String name, String label){
        categoryLabel=label;
        categoryName=name;
        subcategoryList=new ArrayList<>();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryLabel() {
        return categoryLabel;
    }

    public void setCategoryLabel(String categoryLabel) {
        this.categoryLabel = categoryLabel;
    }

    public List<Category> getSubcategoryList(){
        return subcategoryList;
    }

    public static List<Category> createCategoryList(){
        ArrayList<Category> categoryArrayList = new ArrayList<>();
        categoryArrayList.add(createCategory("HOTEL"));
        categoryArrayList.add(createCategory("RESTAURANT"));
        categoryArrayList.add(createCategory("ATTRACTION"));
        return categoryArrayList;
    }

    private static Category createCategory(String name){
        Category blank=new Category("","Tutte");
        Category category = null;
        switch (name) {
            case "HOTEL":
                category=new Category("HOTEL","Hotel");

                category.subcategoryList.add(blank);
                category.subcategoryList.add(new Category("BNB","Bnb") );
                category.subcategoryList.add(new Category("HOSTEL","Ostello") );
                category.subcategoryList.add(new Category("HOTEL","Hotel") );
                break;
            case "RESTAURANT":
                category=new Category("RESTAURANT","Ristoranti");

                category.subcategoryList.add(blank);
                category.subcategoryList.add(new Category("BAR","Bar") );
                category.subcategoryList.add(new Category("PIZZERIA","Pizzeria") );
                category.subcategoryList.add(new Category("TRATTORIA","Trattoria") );
                break;
            case "ATTRACTION":
                category=new Category("ATTRACTION","Attrazioni");

                category.subcategoryList.add(blank);
                category.subcategoryList.add(new Category("PARK","Parco") );
                category.subcategoryList.add(new Category("MUSEUM","Museo") );
                break;
        }
        return category;
    }
}
