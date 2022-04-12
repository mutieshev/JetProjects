package advisor.commands;

import java.io.IOException;

import static advisor.Main.current;
import static advisor.Main.isAuthorized;

public class PrevCommand implements CommandTemplate {
    @Override
    public void getTemplate() throws IOException, InterruptedException {
        if (isAuthorized && current != null) {
            current.prev();
        } else if (!isAuthorized) {
            System.out.println("Please, provide access for application");
        } else {
            System.out.println("Select command first");
        }
    }
}