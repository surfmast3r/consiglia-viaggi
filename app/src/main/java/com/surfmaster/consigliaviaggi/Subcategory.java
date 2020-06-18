package com.surfmaster.consigliaviaggi;

import java.util.EnumSet;
import java.util.Set;

public enum Subcategory {

    //enums
    BLANK_RESTAURANT(CategoryEnum.RESTAURANT, ""),
    BAR(CategoryEnum.RESTAURANT, "Bar"),
    PIZZERIA(CategoryEnum.RESTAURANT, "Pizzeria"),
    TRATTORIA(CategoryEnum.RESTAURANT, "Trattoria"),

    BLANK_HOTEL(CategoryEnum.HOTEL, ""),
    BNB(CategoryEnum.HOTEL, "BnB"),
    HOSTEL(CategoryEnum.HOTEL, "Ostello"),
    HOTEL(CategoryEnum.HOTEL, "Hotel"),

    BLANK_ATTRACTION(CategoryEnum.ATTRACTION, ""),
    PARK(CategoryEnum.ATTRACTION, "Parco"),
    MUSEUM(CategoryEnum.ATTRACTION, "Museo");

    private final String categoryName;
    private final String subCategoryName;
    public static final Set<Subcategory> restaurants = EnumSet.range(BLANK_RESTAURANT, TRATTORIA);
    public static final Set<Subcategory> hotels = EnumSet.range(BLANK_HOTEL, HOTEL);
    public static final Set<Subcategory> attractions = EnumSet.range(BLANK_ATTRACTION, MUSEUM);

    @Override
    public String toString() {
        return subCategoryName;
    }

    Subcategory(CategoryEnum cat, String subCat) {

        this.categoryName = cat.getCategoryName();
        subCategoryName = subCat;
    }
}

