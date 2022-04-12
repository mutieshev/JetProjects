package advisor.commands;

import advisor.Categories;

import java.io.IOException;

import static advisor.Main.current;
import static advisor.Main.isAuthorized;

public class CategoriesCommand implements CommandTemplate {
    @Override
    public void getTemplate() throws IOException, InterruptedException {
        if (isAuthorized) {
            current = new Categories();
            current.printPlaylist();
        } else {
            System.out.println("Please, provide access for application");
        }
    }
}