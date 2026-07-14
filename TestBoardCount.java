import com.codingame.game.Board;
import java.util.Random;

public class TestBoardCount {
    public static void main(String[] args) {
        Random rng = new Random(0);
        for (int test = 0; test < 1000; test++) {
            Board board = new Board(2, 6); // 6x6 board
            int marblesPlaced = 0;
            int player = 0;
            
            for (int turn = 0; turn < 36; turn++) { // 36 is max turns for 6x6
                if (board.isFull()) break;
                
                int rx, ry;
                while (true) {
                    rx = rng.nextInt(6);
                    ry = rng.nextInt(6);
                    if (board.getGrid()[ry][rx] == -1) break;
                }
                
                board.place(rx, ry, player);
                marblesPlaced++;
                
                int rBlock = rng.nextInt(4);
                String dir = rng.nextBoolean() ? "R" : "L";
                board.rotate(rBlock, dir);
                
                // Count marbles on board
                int count = 0;
                for (int y = 0; y < 6; y++) {
                    for (int x = 0; x < 6; x++) {
                        if (board.getGrid()[y][x] != -1) count++;
                    }
                }
                
                if (count != marblesPlaced) {
                    System.out.println("BUG! Marbles placed: " + marblesPlaced + ", counted: " + count);
                    return;
                }
                player = 1 - player;
            }
        }
        System.out.println("Count test passed successfully!");
    }
}
