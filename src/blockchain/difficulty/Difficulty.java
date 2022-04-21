package blockchain.difficulty;

import static blockchain.Blockchain.difficulty;

public class Difficulty implements CommandTemplate {
    @Override
    public int changeDifficulty() {
        return difficulty;
    }
    @Override
    public String getDifficulty() {
        return "N stays the same";
    }
}
