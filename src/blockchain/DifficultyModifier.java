package blockchain;

import java.time.Duration;

public enum DifficultyModifier {
    INCREASE, DECREASE, DO_NOTHING;

    public int apply(int difficulty) {
        switch (this) {
            case INCREASE:
                return difficulty + 1;
            case DECREASE:
                return difficulty - 1;
            default:
                return difficulty;
        }
    }

    public static DifficultyModifier of(Block newBlock, Block lastBlock, int difficulty) {
        if (lastBlock == null) {
            return of(difficulty, newBlock.duration);
        }
        return of(difficulty, Duration.ofMillis(newBlock.timestamp - lastBlock.timestamp));
    }

    public static DifficultyModifier of(int difficulty, Duration generationTime) {
        if (generationTime.getSeconds() < 10) {
            return INCREASE;
        }
        else if (generationTime.getSeconds() >= 10 && difficulty > 0) {
            return DECREASE;
        }
        return DO_NOTHING;
    }
}