package advisor;

import java.io.IOException;

public interface Viewers {
    void prev();
    void next();
    void printPlaylist() throws IOException, InterruptedException;
}