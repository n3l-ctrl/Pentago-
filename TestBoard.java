import com.codingame.game.Board;

public class TestBoard {
    public static void main(String[] args) {
        Board board = new Board(2, 6);
        board.place(0, 0, 0); // Player 0 at 0,0
        board.rotate(0, "R"); // Rotate top-left block Right
        
        int[][] grid = board.getGrid();
        System.out.println("After turn 1:");
        printGrid(grid);
        
        board.place(0, 0, 1); // Player 1 at 0,0
        board.rotate(0, "L"); // Rotate top-left block Left
        
        System.out.println("After turn 2:");
        printGrid(grid);
    }
    
    static void printGrid(int[][] grid) {
        for (int y=0; y<grid.length; y++) {
            for (int x=0; x<grid[y].length; x++) {
                if (grid[y][x] == -1) System.out.print(". ");
                else System.out.print(grid[y][x] + " ");
            }
            System.out.println();
        }
    }
}
