package advisor;

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

public class Authorization {
    public static String SERVER_PATH = "https://accounts.spotify.com";
    private static final String REDIRECT_URI = "http://localhost:8080/";
    private static final String CLIENT_ID = "e200443d9f17416589e98951db551839";
    private static final String CLIENT_SECRET = "e8f2d6158aad4dadb61b7d3676e909dd";
    protected static final String API_SERVER_PATH = "https://api.spotify.com";
    public static String accessCode = "";
    public static String accessToken = "";

    public void getAccessCode() throws IOException, InterruptedException {
        String uri = SERVER_PATH + "/authorize"
                + "?client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=code";
        System.out.println("use this link to request the access code:");
        System.out.println(uri);
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);
        server.createContext("/",
                new HttpHandler() {
                    public void handle(HttpExchange exchange) throws IOException {
                        String query = exchange.getRequestURI().getQuery();
                        String request;
                        if (query != null && query.contains("code")) {
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
                });
        server.start();
        System.out.println("waiting for code...");
        while (accessCode.length() == 0) {
            Thread.sleep(100);
        }
        server.stop(5);
    }

    public void getAccessToken() throws IOException, InterruptedException {
        System.out.println("making http request for access token...");
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(SERVER_PATH + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "grant_type=authorization_code"
                                + "&code=" + accessCode
                                + "&client_id=" + CLIENT_ID
                                + "&client_secret=" + CLIENT_SECRET
                                + "&redirect_uri=" + REDIRECT_URI))
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        advisor.Main.isAuthorized = true;
        JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
        accessToken = jo.get("access_token").getAsString();
        System.out.println("Success!");
    }
}