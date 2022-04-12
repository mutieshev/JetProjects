package advisor.commands;

import advisor.Authorization;

import java.io.IOException;

public class AuthCommand implements CommandTemplate {
    @Override
    public void getTemplate() throws IOException, InterruptedException {
        Authorization authorisation = new Authorization();
        authorisation.getAccessCode();
        authorisation.getAccessToken();
    }
}