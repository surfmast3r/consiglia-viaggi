package com.surfmaster.consigliaviaggi.models.DAO;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.surfmaster.consigliaviaggi.models.CategoryEnum;
import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.models.Subcategory;
import com.surfmaster.consigliaviaggi.models.Accommodation;
import com.surfmaster.consigliaviaggi.models.DTO.JsonPageResponse;
import com.surfmaster.consigliaviaggi.models.SearchParamsAccommodation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccommodationDaoJSON implements AccommodationDao{

    @Override
    public List<Accommodation> getAccommodationList(LatLng latLng) throws DaoException {
        return getAccommodationListLocationJSONParsing(latLng);
    }

    @Override
    public Accommodation getAccommodationById(int id) throws DaoException{
        return getAccommodationJSON(id);
    }

    @Override
    public JsonPageResponse<Accommodation> getAccommodationList(SearchParamsAccommodation params) throws DaoException {
        return  getAccommodationListJSONParsing(params);
    }


    private JsonPageResponse<Accommodation> getAccommodationListJSONParsing(SearchParamsAccommodation params) throws DaoException {
        String urlString;
        try {
            urlString = Constants.GET_ACCOMMODATION_LIST_URL+"?"+
                    Constants.CITY_PARAM +
                    URLEncoder.encode(params.getCurrentCity(), StandardCharsets.UTF_8.name())+"&"+
                    Constants.CATEGORY_PARAM+
                    params.getCurrentCategory()+"&"+
                    Constants.SUBCATEGORY_PARAM+
                    params.getCurrentSubCategory()+"&"+
                    Constants.ORDER_BY_PARAM+
                    params.getOrderBy()+"&"+
                    Constants.MIN_RATING+
                    params.getMinRating()+"&"+
                    Constants.MAX_RATING+
                    params.getMaxRating()+"&"+
                    Constants.DIRECTION_PARAM+
                    params.getDirection()+"&"+
                    Constants.PAGE_PARAM+
                    params.getCurrentPage();
        } catch (UnsupportedEncodingException e) {
            throw new DaoException(DaoException.ERROR,e.getMessage());
        }

        Log.i("Url richiesta pagina", urlString);
        BufferedReader bufferedReader;
        try {
            bufferedReader = getJSONFromUrl(urlString);
        } catch (MalformedURLException e) {
            throw new DaoException(DaoException.ERROR,e.getMessage());

        }
        JsonObject jsonObject= JsonParser.parseReader(bufferedReader).getAsJsonObject();

        return parseAccommodationsPage(jsonObject);
    }

    private List<Accommodation> getAccommodationListLocationJSONParsing(LatLng latLng) throws DaoException {
        String urlString;
        urlString = Constants.GET_ACCOMMODATION_LIST_LOCATION_URL+"?"+
                Constants.LATITUDE_PARAM+latLng.latitude+"&"+
                Constants.LONGITUDE_PARAM+latLng.longitude;

        Log.i("Url richiesta pagina", urlString);
        BufferedReader bufferedReader;
        try {
            bufferedReader = getJSONFromUrl(urlString);
        } catch (MalformedURLException e) {
            throw new DaoException(DaoException.ERROR,e.getMessage());

        }
        JsonElement jsonTree  = JsonParser.parseReader(bufferedReader);
        JsonArray accommodationJsonArray = jsonTree.getAsJsonArray();

        return parseAccommodationsList(accommodationJsonArray);
    }

    private List<Accommodation> parseAccommodationsList(JsonArray array) {
        List<Accommodation> accommodationCollection = new ArrayList<>();
        for (JsonElement jo : array) {
            JsonObject accommodationJson = (JsonObject)jo ;
            accommodationCollection.add(parseAccommodation(accommodationJson));
        }
        return accommodationCollection;
    }

    private Accommodation getAccommodationJSON(int id) throws DaoException {
        String urlString= Constants.GET_ACCOMMODATION_LIST_URL+"?"+Constants.ACCOMMODATION_ID_PARAM+id;

        BufferedReader bufferedReader;
        try {
            bufferedReader = getJSONFromUrl(urlString);
        } catch (MalformedURLException e) {
            throw new DaoException(DaoException.ERROR,e.getMessage());
        }
        JsonElement jsonTree  = JsonParser.parseReader(bufferedReader);
        JsonObject accommodationJSON = jsonTree.getAsJsonObject();
        return parseAccommodation(accommodationJSON);
    }
    private BufferedReader getJSONFromUrl(String urlString) throws MalformedURLException, DaoException {
        URL url = new URL(urlString);
        HttpURLConnection connection;
        int responseCode;
        BufferedReader json;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
            connection.setRequestMethod("GET");
            responseCode=connection.getResponseCode();
            if(responseCode==HttpURLConnection.HTTP_OK)
                json = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            else
                throw new DaoException(DaoException.ERROR,"Server Error");
        } catch (IOException e) {
            throw new DaoException(DaoException.ERROR,e.getMessage());
        }
        return json;
    }
    private JsonPageResponse<Accommodation> parseAccommodationsPage(JsonObject jsonPage){

        List<Accommodation> accommodationCollection = new ArrayList<>();
        JsonArray array= jsonPage.get("content").getAsJsonArray();
        for (JsonElement jo : array) {
            JsonObject accommodationJson = (JsonObject)jo ;
            accommodationCollection.add(parseAccommodation(accommodationJson));
        }
        JsonPageResponse<Accommodation> response = new JsonPageResponse<>();
        response.setContent(accommodationCollection);
        response.setPage(jsonPage.get("page").getAsInt());
        response.setOffset(jsonPage.get("offset").getAsInt());
        response.setPageSize(jsonPage.get("pageSize").getAsInt());
        response.setTotalPages(jsonPage.get("totalPages").getAsInt());
        response.setTotalElements(jsonPage.get("totalElements").getAsInt());
        return response;
    }
    private Accommodation parseAccommodation(JsonObject accommodationJson){
        String name = accommodationJson.get("name").getAsString();
        int id = accommodationJson.get("id").getAsInt();
        String description = accommodationJson.get("description").getAsString();
        Double latitude= accommodationJson.get("latitude").getAsDouble();
        Double longitude= accommodationJson.get("longitude").getAsDouble();
        String address = accommodationJson.get("address").getAsString();
        float rating = accommodationJson.get("rating").getAsFloat();
        String city= accommodationJson.get("city").getAsString();
        String image = accommodationJson.get("images").getAsString();
        CategoryEnum category = CategoryEnum.valueOf(accommodationJson.get("category").getAsString());
        Subcategory subcategory = Subcategory.valueOf(accommodationJson.get("subCategory").getAsString());
        String logoURL="";
        if(accommodationJson.get("logoUrl") .isJsonObject()) {
            logoURL = accommodationJson.get("logoUrl").getAsString();
        }
        return new Accommodation.Builder()
                .setName(name)
                .setId(id)
                .setCity(city)
                .setImages(Collections.singletonList(image))
                .setDescription(description)
                .setLatitude(latitude)
                .setLongitude(longitude)
                .setAddress(address)
                .setRating(rating)
                .setCategory(category)
                .setSubcategory(subcategory)
                .setLogoUrl(logoURL)
                .create();

    }


}
