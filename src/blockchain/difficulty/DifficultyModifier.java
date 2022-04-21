package blockchain.difficulty;

import blockchain.Block;

import java.time.Duration;
import java.util.Objects;

public enum DifficultyModifier {
    INCREASE("increase", IncreaseDifficulty.class),
    DECREASE("decrease", DecreaseDifficulty.class),
    DO_NOTHING("nothing", Difficulty.class);

    private String type;
    private Class<? extends CommandTemplate> commandTemplate;

    DifficultyModifier(String type, Class<? extends CommandTemplate> commandTemplate) {
        this.type = type;
        this.commandTemplate = commandTemplate;
    }

    public static DifficultyModifier getDifficultyByCode(String code) {
        for (DifficultyModifier type : DifficultyModifier.values()) {
            if(Objects.equals(type.type, code)) {
                return type;
            }
        }
        return DO_NOTHING;
    }

    public CommandTemplate getCommandTemplate() throws InstantiationException, IllegalAccessException {
        return commandTemplate.newInstance();
    }

    public static String changeDifficulty(Block newBlock, Block lastBlock, int difficulty) {
        if (lastBlock == null) {
            return changeDifficulty(difficulty, newBlock.duration);
        }
        return changeDifficulty(difficulty, Duration.ofMillis(newBlock.timestamp - lastBlock.timestamp));
    }

    public static String changeDifficulty(int difficulty, Duration generationTime) {
        if (generationTime.getSeconds() < 10) {
            return "increase";
        }
        else if (generationTime.getSeconds() >= 10 && difficulty > 0) {
            return "decrease";
        }
        return "nothing";
    }
}