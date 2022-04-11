package advisor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Categories implements Viewers {
    public static HashMap<Object, Object> categoryId;
    private static List<String> categoriesList;
    private static int pages;
    private static int currentStartPage;
    private static int prevStartPage;
    private static int nextStartPage;

    public Categories() {
        categoryId = new HashMap<>();
        categoriesList = new ArrayList<>();
        pages = Main.PAGES;
        currentStartPage = 0;
        prevStartPage = -pages;
        nextStartPage = pages;
    }

    public void print() {
        for (int i = currentStartPage; i < nextStartPage; i++) {
            if (i >= categoriesList.size()) break;
            System.out.println(categoriesList.get(i));
        }
        System.out.println("---PAGE " + (currentStartPage / pages + 1) + " OF " + categoriesList.size() / pages + "---");
    }

    public void next() {
        if (nextStartPage >= categoriesList.size()) {
            System.out.println("No more pages.");
        } else {
            prevStartPage = currentStartPage;
            currentStartPage = nextStartPage;
            nextStartPage = Math.min(nextStartPage + pages, categoriesList.size());
            print();
        }
    }

    public void prev() {
        if (prevStartPage < 0) {
            System.out.println("No more pages.");
        } else {
            nextStartPage = currentStartPage;
            currentStartPage = prevStartPage;
            prevStartPage = prevStartPage - pages;
            print();
        }
    }

    public void printPlaylist() throws IOException, InterruptedException {
        getAllCategories();
        print();
    }

    static void getAllCategories() throws IOException, InterruptedException {
        String path = Authorization.API_SERVER_PATH + "/v1/browse/categories";
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Authorization.accessToken)
                .uri(URI.create(path))
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonString = response.body();

        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
        JsonObject categories = json.getAsJsonObject("categories");
        JsonElement items = categories.getAsJsonArray("items");

        for (JsonElement element : items.getAsJsonArray()) {
            if (element.isJsonObject()) {
                String playlistName = element.getAsJsonObject().get("name").getAsString();
                String playlistID = element.getAsJsonObject().get("id").getAsString();
                categoryId.put(playlistName, playlistID);
                categoriesList.add(playlistName);
            }
        }
    }
}