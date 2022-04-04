package musicadvisor;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        musicadvisor.Authorization authorization = new musicadvisor.Authorization();
        Scanner scan = new Scanner(System.in);
        String menuRequest;
        boolean on = true;
        boolean auth = false;
        while (!auth) {
            menuRequest = scan.nextLine();
            switch (menuRequest) {
                case "auth":
                    authorization.getAccessCode();
                    authorization.getToken();
                    auth = true;
                    break;
                case "exit":
                    System.out.println("---GOODBYE!---");
                    System.exit(-1);
                default:
                    System.out.println("Please, provide access for application.");
            }
        }
        while (on) {
            menuRequest = scan.next();
            switch (menuRequest) {
                case "featured":
                    authorization.getFeatured();
                    break;
                case "new":
                    authorization.getNewReleases();
                    break;
                case "categories":
                    authorization.getCategories();
                    break;
                case "playlists":
                    String categoryName = scan.nextLine();
                    System.out.println(categoryName.trim());
                    String categoryID = authorization.getCategoryID(categoryName.trim());
                    if (!Objects.equals(categoryID, "Unknown category name.")) {
                        authorization.getPlaylistsByName(categoryID);
                    }
                    else {
                        System.out.println(categoryID);
                    }
                    break;
                case "exit":
                    System.out.println("---GOODBYE!---");
                    on = false;
                    break;
                default:
                    System.out.println("You enter wrong command");
            }
        }
    }
}