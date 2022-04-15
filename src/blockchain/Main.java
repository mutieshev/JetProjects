package blockchain;

public class Main {
    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();
        for (int i = 0; i < 9; i++) {
            blockchain.registerMiner(new Miner(blockchain));
        }
        blockchain.start();
        blockchain.waitStop();
    }
}