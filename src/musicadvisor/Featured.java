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
import java.util.List;

public class Featured implements Viewers {
    private static List<String> songName;
    private static List<String> songLink;
    private static int pages;
    private static int currentStartPage;
    private static int prevStartPage;
    private static int nextStartPage;

    public Featured() {
        songName = new ArrayList<>();
        songLink = new ArrayList<>();
        pages = Main.PAGES;
        currentStartPage = 0;
        prevStartPage = -pages;
        nextStartPage = pages;
    }

    public void print() {
        for (int i = currentStartPage; i < nextStartPage; i++) {
            if (i >= songName.size()) break;
            System.out.println(songName.get(i));
            System.out.println(songLink.get(i) + "\n");
        }
        System.out.println("---PAGE " + (currentStartPage / pages + 1) + " OF " + songName.size() / pages + "---");
    }

    public void next() {
        if (nextStartPage >= songName.size()) {
            System.out.println("No more pages.");
        } else {
            prevStartPage = currentStartPage;
            currentStartPage = nextStartPage;
            nextStartPage = Math.min(nextStartPage + pages, songLink.size());
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
        prepareFeaturedList();
        print();
    }

    private void prepareFeaturedList() throws IOException, InterruptedException {
        String path = Authorization.API_SERVER_PATH + "/v1/browse/featured-playlists";
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Authorization.accessToken)
                .uri(URI.create(path))
                .GET()
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonString = response.body();
        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
        JsonObject playlists = json.getAsJsonObject("playlists");
        JsonElement items = playlists.getAsJsonArray("items");
        for (JsonElement element : items.getAsJsonArray()) {
            if (element.isJsonObject()) {
                songName.add(element.getAsJsonObject().get("name").getAsString());
                songLink.add(element.getAsJsonObject().get("external_urls")
                        .getAsJsonObject().get("spotify").getAsString());
            }
        }
    }
}