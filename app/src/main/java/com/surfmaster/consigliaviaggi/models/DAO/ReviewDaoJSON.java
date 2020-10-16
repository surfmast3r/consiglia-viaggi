package com.surfmaster.consigliaviaggi.models.DAO;

import android.util.Log;

import com.google.gson.Gson;
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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewDaoJSON implements ReviewDao{
    private String token;
    @Override
    public List<Review> getReviewList(int accommodationId) throws DaoException {
        return getReviewListJSONParsing(accommodationId).getContent();
    }

    @Override
    public Boolean postReview(Review review, String token) throws  DaoException {
        this.token=token;
        JsonObject jsonReview=encodeReview(review);
        try {
            return createReviewJSON(jsonReview);
        } catch (IOException e) {
            throw new DaoException(DaoException.ERROR, "Errore di rete");
        }
    }

    private Boolean createReviewJSON(JsonObject reviewJson) throws IOException,DaoException {
        int responseCode;
        HttpURLConnection connection = createAuthenticatedConnection(Constants.CREATE_REVIEW_URL, "POST");
        writeOutputStream(connection, reviewJson.toString());
        responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            return true;

        } else if(responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR){
            return false;

        }else if (responseCode == 401) {
            throw new DaoException(DaoException.FORBIDDEN_ACCESS, "Non autorizzato");
        } else {
            throw new DaoException(DaoException.ERROR, "Errore di rete");
        }
    }

    private JsonPageResponse<Review> getReviewListJSONParsing(int accommodationId) throws DaoException {
        String urlString= Constants.GET_REVIEW_LIST_URL+"?";
        urlString+=Constants.ACCOMMODATION_ID_PARAM+accommodationId;
        urlString+="&"+Constants.STATUS_PARAM+"APPROVED";
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
        response.setPage(jsonPage.get("page").getAsInt());
        response.setOffset(jsonPage.get("offset").getAsInt());
        response.setPageSize(jsonPage.get("pageSize").getAsInt());
        response.setTotalPages(jsonPage.get("totalPages").getAsInt());
        response.setTotalElements(jsonPage.get("totalElements").getAsInt());
        return response;
    }

    private Review parseReview(JsonObject reviewJson) throws DaoException {

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

    private BufferedReader getJSONFromUrl(String urlString) throws MalformedURLException, DaoException {
        URL url = new URL(urlString);
        HttpURLConnection connection;
        int responseCode;
        BufferedReader json = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
            connection.setRequestMethod("GET");
            responseCode=connection.getResponseCode();
            if(responseCode==HttpURLConnection.HTTP_OK)
                json = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            else if(responseCode==HttpURLConnection.HTTP_UNAUTHORIZED)
                throw new DaoException(DaoException.ERROR,"Unauthorized");
            else
                throw new DaoException(DaoException.ERROR,"Server Error");
        } catch (IOException e) {
            throw new DaoException(DaoException.ERROR,e.getMessage());
        }
        return json;
    }

    private String formatDate(String creationDate) throws DaoException {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(creationDate);
        } catch (ParseException e) {
            throw new DaoException(DaoException.ERROR, e.getMessage());
        }
        return new SimpleDateFormat("dd/MM/yyyy, HH:mm").format(date);

    }

    // Authenticate for Create/Delete/Edit methods
    private HttpURLConnection createAuthenticatedConnection(String urlString,String requestMethod) throws IOException {
        //TO DO: Autenticazione
        URL url = new URL(urlString);
        HttpURLConnection connection;
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);
        connection.setDoOutput(true);
        connection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
        connection.setRequestProperty("Authorization","Bearer "+token);
        connection.setRequestProperty("Content-Type","application/json");
        Log.i("Query create review",connection.toString());
        return connection;
    }
    private void writeOutputStream(HttpURLConnection connection,String stream) throws IOException {
        OutputStream os = connection.getOutputStream();
        byte[] input = stream.getBytes(StandardCharsets.UTF_8);
        os.write(input, 0, input.length);
    }

    private JsonObject encodeReview(Review review) {
        Gson gson = new Gson();
        JsonObject reviewJson = JsonParser.parseString(gson.toJson(review)).getAsJsonObject();
        System.out.println("\nAuto encoded: " + reviewJson);

        return reviewJson;
    }
}
