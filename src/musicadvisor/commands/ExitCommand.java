package advisor.commands;

import java.io.IOException;

public class ExitCommand implements CommandTemplate {
    @Override
    public void getTemplate() throws IOException, InterruptedException {
        System.exit(0);
    }
}