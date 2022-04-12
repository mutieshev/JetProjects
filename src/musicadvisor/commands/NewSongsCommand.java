package advisor.commands;

import advisor.NewSongs;

import java.io.IOException;

import static advisor.Main.current;
import static advisor.Main.isAuthorized;

public class NewSongsCommand implements CommandTemplate {
    @Override
    public void getTemplate() throws IOException, InterruptedException {
        if (isAuthorized) {
            current = new NewSongs();
            current.printPlaylist();
        } else {
            System.out.println("Please, provide access for application");
        }
    }
}