package blockchain;

import java.security.MessageDigest;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Block {
    private static final Random rand = new Random();
    final int minerId;
    final int id;
    public final long timestamp;
    final int magicNumber;
    final String prevHash;
    final String hash;
    public Duration duration;
    List<String> data = Collections.emptyList();

    public Block(int id, String prevHash, int minerId) {
        this.id = id;
        this.timestamp = System.currentTimeMillis();
        this.magicNumber = rand.nextInt();
        this.prevHash = prevHash;
        this.hash = generateSHA256(id + timestamp + magicNumber + prevHash + minerId);
        this.minerId = minerId;
    }

    public static boolean verityBlock(Block prev, Block next, int difficulty) {
        if (prev != null && !prev.hash.equals(next.prevHash)) {
            return false;
        }
        for (int i = 0; i < difficulty; i++) {
            if (next.hash.charAt(i) != '0') {
                return false;
            }
        }
        return true;
    }

    private static String generateSHA256(Object input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.toString().getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte elem : hash) {
                String hex = Integer.toHexString(0xff & elem);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}