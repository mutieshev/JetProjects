package blockchain.difficulty;

import static blockchain.Blockchain.difficulty;

public class DecreaseDifficulty implements CommandTemplate {
    @Override
    public int changeDifficulty() {
        return difficulty - 1;
    }
    @Override
    public String getDifficulty() {
        return "N was decreased to " + difficulty;
    }
}