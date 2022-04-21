package blockchain.difficulty;

import static blockchain.Blockchain.difficulty;

public class IncreaseDifficulty implements CommandTemplate {
    @Override
    public int changeDifficulty() {
        return difficulty + 1;
    }
    @Override
    public String getDifficulty() {
        return "N was increased to " + difficulty;
    }
}