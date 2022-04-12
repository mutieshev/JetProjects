package advisor.commands;

import advisor.ParticularPlaylist;

import java.io.IOException;

import static advisor.Main.*;

public class PlaylistsCommand implements CommandTemplate {
    @Override
    public void getTemplate() throws IOException, InterruptedException {
        if (isAuthorized) {
            String type = scanner.nextLine().trim();
            current = new ParticularPlaylist(type);
            current.printPlaylist();
        } else {
            System.out.println("Please, provide access for application");
        }
    }
}