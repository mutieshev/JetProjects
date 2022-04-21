package blockchain;

import blockchain.difficulty.DifficultyModifier;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    public static int difficulty = 0;
    private final List<Block> blocks = new ArrayList<>();
    private final List<Miner> miners = new ArrayList<>();
    private final List<String> data = new ArrayList<>();
    private final int blockCount = 5;
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

    public synchronized void addBlock(Block block) throws InstantiationException, IllegalAccessException {
        if (!running) {
            return;
        }
        if (!Block.verityBlock(getLastBlock(), block, difficulty)) {
            return;
        }

        String modifier = DifficultyModifier.changeDifficulty(block, getLastBlock(), difficulty);

        blocks.add(block);
        if (blocks.size() == blockCount) {
            running = false;
            miners.forEach(m -> m.running = false);
        }

        difficulty = DifficultyModifier.getDifficultyByCode(modifier).getCommandTemplate().changeDifficulty();
        System.out.println(toStringBlock(block, modifier) + "\n");
        miners.forEach(n -> {
            n.getDifficulty(difficulty);
            n.getLastBlock(block);
        });
    }

    private String toStringBlock(Block block, String modifier) throws InstantiationException, IllegalAccessException {
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

    private String toStringDifficulty(String modifier) throws InstantiationException, IllegalAccessException {
        return DifficultyModifier.getDifficultyByCode(modifier).getCommandTemplate().getDifficulty();
    }

    private Block getLastBlock() {
        if (blocks.isEmpty()) {
            return null;
        }
        return blocks.get(blocks.size() - 1);
    }
}