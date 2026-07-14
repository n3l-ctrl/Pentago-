package com.codingame.game;

import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

public class Board {
    private final int size;
    private final int blocksPerRow;
    private final int[][] grid;

    public Board(int numPlayers, int boardSize) {
        if (boardSize == 6) {
            size = 6;
            blocksPerRow = 2;
        } else {
            size = 9;
            blocksPerRow = 3;
        }
        grid = new int[size][size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                grid[y][x] = -1;
            }
        }
    }

    public int getSize() {
        return size;
    }

    public int getBlocksPerRow() {
        return blocksPerRow;
    }

    public int[][] getGrid() {
        return grid;
    }

    public boolean place(int x, int y, int playerId) {
        if (x < 0 || x >= size || y < 0 || y >= size || grid[y][x] != -1) {
            return false;
        }
        grid[y][x] = playerId;
        return true;
    }

    public boolean rotate(int blockId, String dir) {
        int maxBlocks = blocksPerRow * blocksPerRow;
        if (blockId < 0 || blockId >= maxBlocks) {
            return false;
        }
        if (!dir.equals("L") && !dir.equals("R")) {
            return false;
        }

        int blockX = blockId % blocksPerRow;
        int blockY = blockId / blocksPerRow;
        int startX = blockX * 3;
        int startY = blockY * 3;

        int[][] temp = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                temp[i][j] = grid[startY + i][startX + j];
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (dir.equals("R")) {
                    grid[startY + j][startX + 2 - i] = temp[i][j];
                } else {
                    grid[startY + 2 - j][startX + i] = temp[i][j];
                }
            }
        }
        return true;
    }

    public boolean swap(int block1, int block2) {
        int maxBlocks = blocksPerRow * blocksPerRow;
        if (block1 < 0 || block1 >= maxBlocks || block2 < 0 || block2 >= maxBlocks) {
            return false;
        }
        if (block1 == block2) {
            return false;
        }
        
        int b1X = block1 % blocksPerRow;
        int b1Y = block1 / blocksPerRow;
        int b2X = block2 % blocksPerRow;
        int b2Y = block2 / blocksPerRow;
        
        // Ensure they are adjacent (Manhattan distance = 1)
        if (Math.abs(b1X - b2X) + Math.abs(b1Y - b2Y) != 1) {
            return false;
        }
        
        int startX1 = b1X * 3;
        int startY1 = b1Y * 3;
        int startX2 = b2X * 3;
        int startY2 = b2Y * 3;
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int temp = grid[startY1 + i][startX1 + j];
                grid[startY1 + i][startX1 + j] = grid[startY2 + i][startX2 + j];
                grid[startY2 + i][startX2 + j] = temp;
            }
        }
        return true;
    }

    public List<WinLine> getWinningLines() {
        List<WinLine> winningLines = new ArrayList<>();
        
        // Check horizontal, vertical, diagonal
        // Horizontal
        for (int y = 0; y < size; y++) {
            for (int x = 0; x <= size - 5; x++) {
                checkLine(winningLines, x, y, 1, 0);
            }
        }
        // Vertical
        for (int x = 0; x < size; x++) {
            for (int y = 0; y <= size - 5; y++) {
                checkLine(winningLines, x, y, 0, 1);
            }
        }
        // Diagonal 1 (Top-Left to Bottom-Right)
        for (int y = 0; y <= size - 5; y++) {
            for (int x = 0; x <= size - 5; x++) {
                checkLine(winningLines, x, y, 1, 1);
            }
        }
        // Diagonal 2 (Top-Right to Bottom-Left)
        for (int y = 0; y <= size - 5; y++) {
            for (int x = 4; x < size; x++) {
                checkLine(winningLines, x, y, -1, 1);
            }
        }

        return winningLines;
    }

    public List<Integer> getWinningPlayers() {
        List<Integer> winners = new ArrayList<>();
        List<WinLine> lines = getWinningLines();
        for (WinLine line : lines) {
            if (!winners.contains(line.playerId)) {
                winners.add(line.playerId);
            }
        }
        return winners;
    }

    private void checkLine(List<WinLine> winningLines, int startX, int startY, int dx, int dy) {
        int player = grid[startY][startX];
        if (player == -1) return;
        
        List<Point> linePoints = new ArrayList<>();
        linePoints.add(new Point(startX, startY));
        
        for (int i = 1; i < 5; i++) {
            if (grid[startY + dy * i][startX + dx * i] != player) {
                return;
            }
            linePoints.add(new Point(startX + dx * i, startY + dy * i));
        }
        
        winningLines.add(new WinLine(player, linePoints));
    }

    public boolean isFull() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (grid[y][x] == -1) return false;
            }
        }
        return true;
    }
}
