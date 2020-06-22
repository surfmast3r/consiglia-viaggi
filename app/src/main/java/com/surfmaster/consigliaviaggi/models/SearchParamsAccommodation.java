package com.surfmaster.consigliaviaggi.models;

public class SearchParamsAccommodation {

    private String currentSearchString,currentCategory,currentSubCategory;
    private String orderBy;
    private String direction;
    long currentPage;

    public SearchParamsAccommodation(Builder builder) {
        this.currentCategory=builder.currentCategory;
        this.currentPage =builder.currentPage;
        this.currentSubCategory=builder.currentSubCategory;
        this.currentSearchString=builder.currentSearchString;
        this.direction=builder.direction;
        this.orderBy=builder.orderBy;
    }



    public static class Builder {
        private String currentSearchString="",currentCategory="",currentSubCategory="";
        private String orderBy="id";
        private String direction="DESC";
        long currentPage =0;

        public Builder setCurrentSearchString(String currentSearchParam) {
            this.currentSearchString = currentSearchParam;
            return this;
        }

        public Builder setCurrentCategory(String currentCategory) {
            this.currentCategory = currentCategory;
            return this;
        }

        public Builder setCurrentSubCategory(String currentSubCategory) {
            this.currentSubCategory = currentSubCategory;
            return this;
        }

        public Builder setCurrentPage(long currentPage) {
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



        public SearchParamsAccommodation create() {
            return new SearchParamsAccommodation(this);
        }

    }

    public String getCurrentSearchString() {
        return currentSearchString;
    }

    public String getCurrentCategory() {
        return currentCategory;
    }

    public String getCurrentSubCategory() {
        return currentSubCategory;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getDirection() {
        return direction;
    }

    public void setCurrentSearchString(String currentSearchString) {
        this.currentSearchString = currentSearchString;
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

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }
}
