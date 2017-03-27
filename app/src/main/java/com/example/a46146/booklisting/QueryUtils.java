package com.example.a46146.booklisting;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    private QueryUtils() {
    }

    public static List<Book> fetchBookData(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        URL url = createUrl(json);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("debug", "jsonResponse:" + jsonResponse);
        List<Book> books = extractFeatureFromJson(jsonResponse);
        return books;
    }

    private static List<Book> extractFeatureFromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            Log.d("debug", "0");
            return null;
        }
        List<Book> books = new ArrayList<>();
        JSONObject baseJsonResponse = null;
        Log.d("debug", "1");
        try {
            baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray bookArray = baseJsonResponse.getJSONArray("items");

            Log.d("debug", "bookArray:" + bookArray.length());
            for (int i = 0; i < bookArray.length(); i++) {
                Log.d("debug", "i======" + i);

                JSONObject currentBook = bookArray.getJSONObject(i);

                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");

                Log.d("debug", "title:" + title);
                JSONArray authors = volumeInfo.getJSONArray("authors");
                String author = authors.getString(0);
                for (int j = 1; j < authors.length(); j++) {
                    author = author + ", " + authors.getString(j);
                }
                JSONObject saleInfo = currentBook.getJSONObject("saleInfo");
                JSONArray offers;
                String price = null;
                if (saleInfo.has("offers")) {
                    offers = saleInfo.getJSONArray("offers");
                    JSONObject offer = offers.getJSONObject(0);
                    JSONObject retailPrice = offer.getJSONObject("retailPrice");
                    if (retailPrice.has("amountInMicros")) {
                        price = retailPrice.getString("amountInMicros") + "$";
                        Log.d("debug", "price存在");
                    } else {
                        price = "无";
                        Log.d("debug", "price不存在");
                    }
                } else {
                    price = "无";
                }
                Book book = new Book(title, author, price);
                books.add(book);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("debug", "3");
        return books;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {

                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            // TODO: Handle the exception
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = null;
            try {
                line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return output.toString();
    }

}
