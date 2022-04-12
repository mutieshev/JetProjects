package advisor.commands;

import java.io.IOException;

public class WrongCommand implements CommandTemplate {
    @Override
    public void getTemplate() throws IOException, InterruptedException {
        System.out.println("Enter valid command");
    }
}