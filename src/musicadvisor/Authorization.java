package musicadvisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Authorization {
    private String accessCode;
    private String accessToken;
    private final String spotifyApi = "https://api.spotify.com";
    private final HttpClient client = HttpClient.newBuilder().build();

    public void getAccessCode() throws IOException, InterruptedException {
        System.out.println("https://accounts.spotify.com/authorize?" +
                "client_id=e200443d9f17416589e98951db551839" +
                "&redirect_uri=http://localhost:8080/&response_type=code\n" +
                "waiting for code...");

        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);
        server.createContext("/",
                new HttpHandler() {
                    public void handle(HttpExchange exchange) throws IOException {
                        String query = exchange.getRequestURI().getQuery();
                        String request;
                        if(query != null && query.contains("code")) {
                            accessCode = query.substring(5);
                            System.out.println("code received");
                            request = "Got the code. Return back to your program.";
                        } else {
                            request = "Authorization code not found. Try again.";
                        }
                        exchange.sendResponseHeaders(200, request.length());
                        exchange.getResponseBody().write(request.getBytes());
                        exchange.getResponseBody().close();
                    }
                }
        );
        server.start();
        while (accessCode == null) {
            Thread.sleep(100);
        }
        server.stop(1);
    }

    public void getToken() throws IOException, InterruptedException {
        System.out.println("making http request for access_token...");
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create("https://accounts.spotify.com/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "grant_type=authorization_code"
                                + "&code=" + accessCode
                                + "&client_id=e200443d9f17416589e98951db551839"
                                + "&client_secret=e8f2d6158aad4dadb61b7d3676e909dd"
                                + "&redirect_uri=http://localhost:8080/"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject token =
                JsonParser.parseString(response.body()).getAsJsonObject();
        accessToken = token.get("access_token").getAsString();
        System.out.println("Success!");
    }

    public void getFeatured() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(spotifyApi + "/v1/browse/featured-playlists"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject featured = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject playlists = featured.getAsJsonObject("playlists");
        JsonArray item = playlists.getAsJsonArray("items");
        List<JsonObject> albums = new ArrayList<>();
        for (int i = 0; i < item.size(); i++) {
            albums.add(item.get(i).getAsJsonObject());
        }
        List<String> names = new ArrayList<>();
        List<JsonObject> extUrls = new ArrayList<>();
        for (JsonObject album : albums) {
            names.add(album.get("name").getAsString());
            extUrls.add(album.getAsJsonObject("external_urls"));
        }
        List<String> playlistUrls = new ArrayList<>();
        for (JsonObject extUrl : extUrls) {
            playlistUrls.add(extUrl.get("spotify").getAsString());
        }
        for (int i = 0; i < names.size(); i++) {
            System.out.println(names.get(i));
            System.out.println(playlistUrls.get(i) + "\n");
        }
    }

    public void getNewReleases() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(spotifyApi + "/v1/browse/new-releases"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject newReleases = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject albums = newReleases.getAsJsonObject("albums");
        JsonArray item = albums.getAsJsonArray("items");
        List<JsonObject> songs = new ArrayList<>();
        for (int i = 0; i < item.size(); i++) {
            songs.add(item.get(i).getAsJsonObject());
        }
        List<String> names = new ArrayList<>();
        List<List<String>> artistsNames = new ArrayList<>();
        List<JsonArray> artists = new ArrayList<>();
        List<JsonObject> extUrls = new ArrayList<>();
        for (JsonObject song : songs) {
            names.add(song.get("name").getAsString());
            artists.add(song.getAsJsonArray("artists"));
            artistsNames.add(new ArrayList<>());
            extUrls.add(song.getAsJsonObject("external_urls"));
        }
        for (int i = 0; i < artists.size(); i++) {
            for (int j = 0; j < artists.get(i).size(); j++) {
                artistsNames.get(i).add(artists.get(i).get(j)
                        .getAsJsonObject().get("name").getAsString());
            }
        }
        List<String> songUrls = new ArrayList<>();
        for (JsonObject extUrl : extUrls) {
            songUrls.add(extUrl.get("spotify").getAsString());
        }
        for (int i = 0; i < names.size(); i++) {
            System.out.println(names.get(i));
            System.out.println(artistsNames.get(i).toString());
            System.out.println(songUrls.get(i) + "\n");
        }
    }

    public void getCategories() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(spotifyApi + "/v1/browse/categories"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject allCategories =
                JsonParser.parseString(response.body()).getAsJsonObject().getAsJsonObject("categories");
        JsonArray items = allCategories.getAsJsonArray("items");
        List<JsonObject> categories = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            categories.add(items.get(i).getAsJsonObject());
        }
        List<String> categoriesNames = new ArrayList<>();
        for (JsonObject category : categories) {
            categoriesNames.add(category.get("name").getAsString());
        }
        for (String categoriesName : categoriesNames) {
            System.out.println(categoriesName);
        }
    }

    public String getCategoryID(String categoryName) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(spotifyApi + "/v1/browse/categories"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject allCategories =
                JsonParser.parseString(response.body()).getAsJsonObject().getAsJsonObject("categories");
        JsonArray items = allCategories.getAsJsonArray("items");
        List<JsonObject> categories = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            categories.add(items.get(i).getAsJsonObject());
        }
        String categoryID;
        for (JsonObject category : categories) {
            if (Objects.equals(categoryName, category.get("name").getAsString())) {
                categoryID = category.get("id").getAsString();
                return categoryID;
            }
        }
        return "Unknown category name.";
    }

    public void getPlaylistsByName(String categoryID) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(spotifyApi + "/v1/browse/categories/" + categoryID + "/playlists"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject allPlaylists =
                JsonParser.parseString(response.body()).getAsJsonObject().getAsJsonObject("playlists");
        JsonArray items = allPlaylists.getAsJsonArray("items");
        List<JsonObject> playlists = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            playlists.add(items.get(i).getAsJsonObject());
        }
        List<String> playlistsNames = new ArrayList<>();
        List<JsonObject> extUrls = new ArrayList<>();
        for (JsonObject playlist : playlists) {
            playlistsNames.add(playlist.get("name").getAsString());
            extUrls.add(playlist.getAsJsonObject("external_urls"));
        }
        List<String> playlistUrls = new ArrayList<>();
        for (JsonObject extUrl : extUrls) {
            playlistUrls.add(extUrl.get("spotify").getAsString());
        }
        for (int i = 0; i < playlistsNames.size(); i++) {
            System.out.println(playlistsNames.get(i));
            System.out.println(playlistUrls.get(i) + "\n");
        }
    }
}