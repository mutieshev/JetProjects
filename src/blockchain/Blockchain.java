package blockchain;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private final List<Block> blocks = new ArrayList<>();
    private final List<Miner> miners = new ArrayList<>();
    private final List<String> data = new ArrayList<>();
    private int difficulty = 0;
    private final int blockCount = 15;
    private boolean running = true;

    public void start() {
        miners.forEach(Thread::start);
    }

    public void waitStop() {
        while (miners.stream().anyMatch(Thread::isAlive)) {
        }
    }

    public void registerMiner(Miner miner) {
        miners.add(miner);
    }

    public void addData(String data) {
        synchronized (this.data) {
            this.data.add(data);
        }
    }

    public synchronized void addBlock(Block block) {
        if (!running) {
            return;
        }
        if (!Block.verityBlock(getLastBlock(), block, difficulty)) {
            return;
        }

        DifficultyModifier modifier = DifficultyModifier.of(block, getLastBlock(), difficulty);

        blocks.add(block);
        if (blocks.size() == blockCount) {
            running = false;
            miners.forEach(m -> m.running = false);
        }

        difficulty = modifier.apply(difficulty);
        System.out.println(toStringBlock(block, modifier) + "\n");
        miners.forEach(n -> {
            n.getDifficulty(difficulty);
            n.getLastBlock(block);
        });
    }

    private String toStringBlock(Block block, DifficultyModifier modifier) {
        return "Block:" +
                "\nCreated by: miner" + block.minerId +
                "\nminer" + block.minerId + " gets 100 VC" +
                "\nId: " + block.id +
                "\nTimestamp: " + block.timestamp +
                "\nMagic number: " + block.magicNumber +
                "\nHash of the previous block:\n" +
                block.prevHash +
                "\nHash of the block:\n" +
                block.hash +
                toStringBlockData(block) +
                "\nBlock was generating for " + block.duration.getSeconds() + " seconds\n" +
                toStringDifficulty(modifier);
    }

    private String toStringBlockData(Block block) {
        if (block.data.isEmpty()) {
            return "\nBlock data:\nno messages";
        }
        return "\nBlock data:\n" + String.join("\n", block.data);
    }

    private String toStringDifficulty(DifficultyModifier modifier) {
        switch (modifier) {
            case INCREASE:
                return "N was increased to " + difficulty;
            case DECREASE:
                return "N was decreased to " + difficulty;
            default:
                return "N stays the same";
        }
    }

    private Block getLastBlock() {
        if (blocks.isEmpty()) {
            return null;
        }
        return blocks.get(blocks.size() - 1);
    }
}