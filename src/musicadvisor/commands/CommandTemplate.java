package advisor.commands;

import java.io.IOException;

public interface CommandTemplate {
    void getTemplate() throws IOException, InterruptedException;
}