package com.surfmaster.consigliaviaggi.models;

public class SearchParamsAccommodation {

    private Float minRating,maxRating;
    private String currentName,currentCategory,currentSubCategory,currentDescription;
    private String orderBy;
    private String direction;
    private Double latitude,longitude;
    private String currentCity;
    int currentPage;

    public SearchParamsAccommodation(Builder builder) {
        this.currentCategory=builder.currentCategory;
        this.currentPage =builder.currentPage;
        this.currentSubCategory=builder.currentSubCategory;
        this.currentName =builder.currentName;
        this.direction=builder.direction;
        this.orderBy=builder.orderBy;
        this.latitude=builder.latitude;
        this.longitude=builder.longitude;
        this.minRating=builder.minRating;
        this.maxRating=builder.maxRating;
        this.currentCity=builder.currentCity;
        this.currentDescription=builder.currentDescription;
    }



    public static class Builder {
        private String currentName ="",currentCategory="",currentSubCategory="",currentDescription="";
        private String currentCity="";
        private String orderBy="name";
        private String direction="DESC";
        private Double latitude=  -200.0,longitude =  -200.0;
        private Float minRating=  0.0f, maxRating= 5.0f;
        int currentPage =0;

        public Builder setCurrentName(String currentSearchParam) {
            this.currentName = currentSearchParam;
            return this;
        }

        public Builder setCurrentCategory(String currentCategory) {
            this.currentCategory = currentCategory;
            return this;
        }

        public Builder setCurrentDescription(String currentDescription) {
            this.currentDescription = currentDescription;
            return this;
        }

        public Builder setCurrentSubCategory(String currentSubCategory) {
            this.currentSubCategory = currentSubCategory;
            return this;
        }

        public Builder setCurrentCity(String currentCity) {
            this.currentCity = currentCity;
            return this;
        }

        public Builder setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
            return this;
        }
        public Builder setOrderBy(String orderBy) {
            this.orderBy = orderBy;
            return this;
        }
        public Builder setDirection(String direction) {
            this.direction = direction;
            return this;
        }

        public Builder setLatitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }
        public Builder setLongitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }
        public Builder setMinRating(Float minRating) {
            this.minRating = minRating;
            return this;
        }
        public Builder setMaxRating(Float maxRating) {
            this.maxRating = maxRating;
            return this;
        }

        public SearchParamsAccommodation create() {
            return new SearchParamsAccommodation(this);
        }

    }

    public String getCurrentCity() {
        return currentCity;
    }

    public String getCurrentDescription() {
        return currentDescription;
    }


    public Double getLatitude() { return latitude; }

    public Double getLongitude() { return longitude; }

    public String getCurrentName() {
        return currentName;
    }

    public String getCurrentCategory() {
        return currentCategory;
    }

    public String getCurrentSubCategory() {
        return currentSubCategory;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getDirection() {
        return direction;
    }

    public Float getMinRating() {
        return minRating;
    }

    public Float getMaxRating() {
        return maxRating;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    public void setCurrentCategory(String currentCategory) {
        this.currentCategory = currentCategory;
    }

    public void setCurrentSubCategory(String currentSubCategory) {
        this.currentSubCategory = currentSubCategory;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setCurrentCity(String city) {
        currentCity=city;
    }

    public void setCurrentDescription(String currentDescription) {
        this.currentDescription = currentDescription;
    }

}
