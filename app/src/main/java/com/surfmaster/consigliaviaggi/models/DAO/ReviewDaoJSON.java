package com.surfmaster.consigliaviaggi.models.DAO;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.models.DTO.JsonPageResponse;
import com.surfmaster.consigliaviaggi.models.Review;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewDaoJSON implements ReviewDao{
    @Override
    public List<Review> getReviewList(int accommodationId) throws DaoException {
        return getReviewListJSONParsing(accommodationId).getContent();
    }

    @Override
    public Review getReviewById(int id) {
        return null;
    }

    @Override
    public boolean postReview(Review review) {
        return false;
    }

    private JsonPageResponse<Review> getReviewListJSONParsing(int accommodationId) throws DaoException {
        String urlString= Constants.GET_REVIEW_LIST_URL+"?";
        urlString+=Constants.ACCOMMODATION_ID_PARAM+accommodationId;
        System.out.println(urlString);

        BufferedReader bufferedReader;
        try {
            bufferedReader = getJSONFromUrl(urlString);
        } catch (MalformedURLException e) {
            throw new DaoException(DaoException.ERROR,e.getMessage());

        }
        JsonObject jsonObject= JsonParser.parseReader(bufferedReader).getAsJsonObject();

        return parseReviewPage(jsonObject);
    }

    private JsonPageResponse<Review> parseReviewPage(JsonObject jsonPage) throws DaoException {

        List<Review> reviewCollection = new ArrayList<>();
        JsonArray array= jsonPage.get("content").getAsJsonArray();
        for (JsonElement jo : array) {
            JsonObject reviewJson = (JsonObject)jo ;
            reviewCollection.add(parseReview(reviewJson));
        }
        System.out.println("COLLECTION" + reviewCollection);
        JsonPageResponse<Review> response = new JsonPageResponse<>();
        response.setContent(reviewCollection);
        response.setPage(jsonPage.get("page").getAsLong());
        response.setOffset(jsonPage.get("offset").getAsLong());
        response.setPageSize(jsonPage.get("pageSize").getAsLong());
        response.setTotalPages(jsonPage.get("totalPages").getAsLong());
        response.setTotalElements(jsonPage.get("totalElements").getAsLong());
        return response;
    }

    private Review parseReview(JsonObject reviewJson) throws DaoException {
        int id = reviewJson.get("id").getAsInt();
        int accommodationId = reviewJson.get("accommodationId").getAsInt();
        String accommodationName = reviewJson.get("accommodationName").getAsString();
        String name = reviewJson.get("nome").getAsString();
        String creationDate =  reviewJson.get("creationDate").getAsString();
        creationDate = creationDate.substring(0,creationDate.indexOf("+"));
        String formattedDate = formatDate(creationDate);
        float rating = reviewJson.get("rating").getAsFloat();
        String content = reviewJson.get("content").getAsString();

        return new Review.Builder()

                .setAccommodationId(accommodationId)
                .setAccommodationName(accommodationName)
                .setAuthor(name)
                .setData(formattedDate)
                .setRating(rating)
                .setReviewText(content)
                .build();

    }

    private BufferedReader getJSONFromUrl(String urlString) throws MalformedURLException {
        URL url = new URL(urlString);
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader json  = null;
        try {
            json = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String formatDate(String creationDate) throws DaoException {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss.SSS").parse(creationDate);
        } catch (ParseException e) {
            throw new DaoException(DaoException.ERROR, e.getMessage());
        }
        return new SimpleDateFormat("dd/MM/yyyy, HH:mm").format(date);

    }
}
