package advisor.commands;

import advisor.Featured;

import java.io.IOException;

import static advisor.Main.current;
import static advisor.Main.isAuthorized;

public class FeaturedCommand implements CommandTemplate {
    @Override
    public void getTemplate() throws IOException, InterruptedException {
        if (isAuthorized) {
            current = new Featured();
            current.printPlaylist();
        } else {
            System.out.println("Please, provide access for application");
        }
    }
}