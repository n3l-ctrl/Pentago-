import com.codingame.game.Board;
import java.util.Random;

public class TestViewer {
    public static void main(String[] args) {
        Board board = new Board(2, 6);
        int[] blockRotations = new int[4];
        boolean[][] spriteAlpha = new boolean[4][9];
        
        Random rng = new Random(0);
        int player = 0;
        
        for (int turn = 0; turn < 10000; turn++) {
            if (board.isFull()) break;
            
            // Pick a random empty cell
            int rx, ry;
            while (true) {
                rx = rng.nextInt(6);
                ry = rng.nextInt(6);
                if (board.getGrid()[ry][rx] == -1) break;
            }
            
            // Place
            board.place(rx, ry, player);
            
            // drawMarble logic
            int bx = rx / 3;
            int by = ry / 3;
            int blockId = by * 2 + bx;
            int localX = rx % 3;
            int localY = ry % 3;
            
            int px = localX;
            int py = localY;
            for (int i = 0; i < blockRotations[blockId]; i++) {
                int temp = px;
                px = py;
                py = 2 - temp;
            }
            
            int spriteIndex = py * 3 + px;
            if (spriteAlpha[blockId][spriteIndex]) {
                System.out.println("BUG! Sprite " + spriteIndex + " in block " + blockId + " was overwritten! Turn: " + turn);
                System.out.println("Played at: " + rx + ", " + ry + " with rotation: " + blockRotations[blockId]);
                return;
            }
            spriteAlpha[blockId][spriteIndex] = true;
            
            // Rotate
            int rBlock = rng.nextInt(4);
            String dir = rng.nextBoolean() ? "R" : "L";
            board.rotate(rBlock, dir);
            
            // animateRotation logic
            if (dir.equals("R")) {
                blockRotations[rBlock] = (blockRotations[rBlock] + 1) % 4;
            } else {
                blockRotations[rBlock] = (blockRotations[rBlock] + 3) % 4;
            }
            
            player = 1 - player;
        }
        System.out.println("Test passed successfully!");
    }
}
