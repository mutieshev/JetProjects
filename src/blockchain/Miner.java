package blockchain;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class Miner extends Thread {
    private static final AtomicInteger idGenerator = new AtomicInteger();
    private final int id;
    private final Blockchain chain;
    private int difficulty;
    private int cash;
    private Block lastBlock;
    public boolean running = true;

    public Miner(Blockchain chain) {
        this.id = idGenerator.incrementAndGet();
        this.chain = chain;
        this.cash = 100;
    }

    @Override
    public void run() {
        while (running) {
            Block block = newBlock();
            chain.addBlock(block);
        }
    }

    public Block newBlock() {
        var startTime = System.currentTimeMillis();
        while (true) {
            Block block = new Block(
                    lastBlock == null ? 1 : lastBlock.id + 1,
                    lastBlock == null ? "0" : lastBlock.hash,
                    id
            );

            if (Block.verityBlock(lastBlock, block, difficulty)) {
                block.duration = Duration.ofMillis(System.currentTimeMillis() - startTime);
                this.cash += 100;
                return block;
            }
        }
    }

    public void getDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void getLastBlock(Block lastBlock) {
        this.lastBlock = lastBlock;
    }
}