import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

class Player {
    static int size;
    static int[][] grid;
    
    static class Move {
        int x, y, block, b2;
        String dir;
        boolean isSwap;
        
        Move(int x, int y, int block, String dir) {
            this.x = x; this.y = y; this.block = block; this.dir = dir; this.isSwap = false;
        }
        
        Move(int x, int y, int b1, int b2, boolean isSwap) {
            this.x = x; this.y = y; this.block = b1; this.b2 = b2; this.isSwap = true;
        }
        
        public String toString() {
            if (isSwap) {
                return x + " " + y + " SWAP " + block + " " + b2;
            }
            return x + " " + y + " " + block + " " + dir;
        }
    }
    
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        if (!in.hasNextInt()) return;
        int numPlayers = in.nextInt();
        int myId = in.nextInt();
        while (in.hasNext()) {
            size = in.nextInt();
            grid = new int[size][size];
            List<int[]> emptyCells = new ArrayList<>();
            int totalPieces = 0;
            
            for (int y = 0; y < size; y++) {
                String row = in.next();
                for (int x = 0; x < size; x++) {
                    char c = row.charAt(x);
                    if (c == '.') {
                        grid[y][x] = -1;
                        emptyCells.add(new int[]{x, y});
                    } else {
                        grid[y][x] = c - '0';
                        totalPieces++;
                    }
                }
            }
            
            
            List<Move> allMoves = new ArrayList<>();
            int blocks = (size == 6) ? 4 : 9;
            for (int[] cell : emptyCells) {
                for (int b = 0; b < blocks; b++) {
                    allMoves.add(new Move(cell[0], cell[1], b, "L"));
                    allMoves.add(new Move(cell[0], cell[1], b, "R"));
                }
                
                // Swaps are only introduced in League 3. League 2 boss does not use swaps.
            }
            
            Move bestMove = null;
            // 1. Can I win?
            for (Move m : allMoves) {
                if (simWin(myId, m)) {
                    bestMove = m;
                    break;
                }
            }
            
            // 2. Can ANY opponent win? (Block them)
            if (bestMove == null) {
                for (int p = 0; p < numPlayers; p++) {
                    if (p == myId) continue;
                    for (Move m : allMoves) {
                        if (simWin(p, m)) {
                            bestMove = m;
                            break;
                        }
                    }
                    if (bestMove != null) break;
                }
            }
            
            // 3. Fallback: random move
            if (bestMove == null && !allMoves.isEmpty()) {
                bestMove = allMoves.get((int)(Math.random() * allMoves.size()));
            }
            
            System.out.println(bestMove);
        }
    }
    
    static boolean simWin(int playerIdx, Move m) {
        int[][] temp = new int[size][size];
        for(int i=0; i<size; i++) System.arraycopy(grid[i], 0, temp[i], 0, size);
        
        temp[m.y][m.x] = playerIdx;
        
        int blocksPerRow = (size == 6) ? 2 : 3;
        
        if (m.isSwap) {
            int bx1 = m.block % blocksPerRow;
            int by1 = m.block / blocksPerRow;
            int startX1 = bx1 * 3;
            int startY1 = by1 * 3;
            
            int bx2 = m.b2 % blocksPerRow;
            int by2 = m.b2 / blocksPerRow;
            int startX2 = bx2 * 3;
            int startY2 = by2 * 3;
            
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int val1 = temp[startY1 + i][startX1 + j];
                    temp[startY1 + i][startX1 + j] = temp[startY2 + i][startX2 + j];
                    temp[startY2 + i][startX2 + j] = val1;
                }
            }
        } else {
            int bx = m.block % blocksPerRow;
            int by = m.block / blocksPerRow;
            int startX = bx * 3;
            int startY = by * 3;
            
            int[][] blockCells = new int[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    blockCells[i][j] = temp[startY + i][startX + j];
                }
            }
            
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (m.dir.equals("R")) {
                        temp[startY + j][startX + 2 - i] = blockCells[i][j];
                    } else {
                        temp[startY + 2 - j][startX + i] = blockCells[i][j];
                    }
                }
            }
        }
        
        return checkWin(temp, playerIdx);
    }
    
    static boolean checkWin(int[][] g, int p) {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x <= size - 5; x++) {
                if (checkLine(g, p, x, y, 1, 0)) return true;
            }
        }
        for (int y = 0; y <= size - 5; y++) {
            for (int x = 0; x < size; x++) {
                if (checkLine(g, p, x, y, 0, 1)) return true;
            }
        }
        for (int y = 0; y <= size - 5; y++) {
            for (int x = 0; x <= size - 5; x++) {
                if (checkLine(g, p, x, y, 1, 1)) return true;
            }
        }
        for (int y = 0; y <= size - 5; y++) {
            for (int x = 4; x < size; x++) {
                if (checkLine(g, p, x, y, -1, 1)) return true;
            }
        }
        return false;
    }
    
    static boolean checkLine(int[][] g, int p, int x, int y, int dx, int dy) {
        for (int i = 0; i < 5; i++) {
            if (g[y + dy * i][x + dx * i] != p) return false;
        }
        return true;
    }
}
