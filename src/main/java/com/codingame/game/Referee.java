package com.codingame.game;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Rectangle;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;
import com.google.inject.Inject;

public class Referee extends AbstractReferee {
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;

    private Board board;
    private int currentPlayerIndex = 0;
    
    private Group[] blockGroups;
    private int[] blockRotations;
    private Sprite[][] marbles;
    private Text[] playerActionTexts;

    @Override
    public void init() {
        System.err.println("REFEREE: init() started");
        try {
            int numPlayers = gameManager.getPlayerCount();
            int boardSize = 9;
            try {
                int league = gameManager.getLeagueLevel();
                if (league == 1) {
                    boardSize = 6;
                } else if (league > 1) {
                    boardSize = 9;
                } else {
                    // Fallback to game parameters if league is 0 (local test)
                    if (gameManager.getGameParameters() != null) {
                        boardSize = Integer.valueOf(gameManager.getGameParameters().getProperty("board_size", "9"));
                    }
                }
            } catch (Throwable t) {
                boardSize = 9;
            }
            board = new Board(numPlayers, boardSize);

            gameManager.setMaxTurns(200);
            gameManager.setTurnMaxTime(50);
            gameManager.setFirstTurnMaxTime(1000);
            
            drawBoard();
        } catch (Throwable t) {
            graphicEntityModule.createText("INIT ERR: " + t.getMessage())
                    .setX(100).setY(100).setFontSize(40).setFillColor(0xff0000);
        }
        System.err.println("REFEREE: init() finished");
    }
    
    private void drawBoard() {
        graphicEntityModule.createSprite()
                .setImage("modern_bg.jpg")
                .setZIndex(-100);
                

        int blocksPerRow = board.getBlocksPerRow();
        int maxBlocks = blocksPerRow * blocksPerRow;
        blockGroups = new Group[maxBlocks];
        blockRotations = new int[maxBlocks];
        marbles = new Sprite[maxBlocks][9];
        
        int cellSize = 100;
        int blockSize = cellSize * 3;
        int gap = 10;
        int boardSizePixels = blocksPerRow * blockSize + (blocksPerRow - 1) * gap;
        int startX = (1920 - boardSizePixels) / 2;
        int startY = (1080 - boardSizePixels) / 2;
        
        for (int b = 0; b < maxBlocks; b++) {
            int bx = b % blocksPerRow;
            int by = b / blocksPerRow;
            
            Group group = graphicEntityModule.createGroup()
                    .setX(startX + bx * (blockSize + gap) + blockSize/2)
                    .setY(startY + by * (blockSize + gap) + blockSize/2)
                    .setScale(0); // Hidden for intro animation
            
            group.add(graphicEntityModule.createSprite()
                    .setImage("modern_block.png")
                    .setBaseWidth(blockSize)
                    .setBaseHeight(blockSize)
                    .setX(-blockSize/2)
                    .setY(-blockSize/2)
                    .setZIndex(-10));
            
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int cx = -blockSize/2 + j * cellSize + cellSize/2;
                    int cy = -blockSize/2 + i * cellSize + cellSize/2;
                    
                    // 3D Glass Marble Effect
                    Sprite marbleSprite = graphicEntityModule.createSprite()
                            .setImage("red_marble.png")
                            .setX(cx)
                            .setY(cy)
                            .setAnchor(0.5)
                            .setBaseWidth(cellSize)
                            .setBaseHeight(cellSize)
                            .setAlpha(0)
                            .setZIndex(0);
                            
                    marbles[b][i * 3 + j] = marbleSprite;
                    group.add(marbleSprite);
                }
            }
            
            blockGroups[b] = group;
        }

        playerActionTexts = new Text[gameManager.getPlayerCount()];
        for (Player p : gameManager.getPlayers()) {
            int idx = p.getIndex();
            int x = (idx % 2 == 0) ? 250 : 1920 - 250;
            int y = (idx < 2) ? 300 : 1080 - 300;

            String avatar = p.getAvatarToken();
            if (avatar != null) {
                graphicEntityModule.createSprite()
                        .setImage(avatar)
                        .setX(x)
                        .setY(y - 100)
                        .setAnchorX(0.5)
                        .setAnchorY(0.5)
                        .setBaseWidth(150)
                        .setBaseHeight(150);
            }

            String nickname = p.getNicknameToken();
            graphicEntityModule.createText(nickname != null ? nickname : "Player " + idx)
                    .setX(x)
                    .setY(y + 20)
                    .setAnchorX(0.5)
                    .setFontSize(40)
                    .setFillColor(p.getColorToken());
                    
            playerActionTexts[idx] = graphicEntityModule.createText("Waiting...")
                    .setX(x)
                    .setY(y + 80)
                    .setAnchorX(0.5)
                    .setFontSize(30)
                    .setFillColor(0xffffff);
        }
    }

    @Override
    public void gameTurn(int turn) {
        if (turn == 1) {
            gameManager.setFrameDuration(1500); // 1.5 seconds for first turn to allow intro animation
            for (Group group : blockGroups) {
                group.setScale(1);
                graphicEntityModule.commitEntityState(0.4, group);
            }
        } else {
            gameManager.setFrameDuration(1000);
        }

        Player player = gameManager.getPlayer(currentPlayerIndex);

        if (!player.isActive()) {
            advanceTurn();
            return;
        }

        // Send inputs
        int size = board.getSize();
        player.sendInputLine(String.valueOf(size));
        int[][] grid = board.getGrid();
        for (int y = 0; y < size; y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < size; x++) {
                if (grid[y][x] == -1) {
                    sb.append(".");
                } else {
                    sb.append(grid[y][x]);
                }
            }
            player.sendInputLine(sb.toString());
        }
        System.err.println("REFEREE: executing player...");
        player.execute();
        System.err.println("REFEREE: player executed.");

        try {
            System.err.println("REFEREE: getting outputs...");
            List<String> outputs = player.getOutputs();
            System.err.println("REFEREE: got outputs: " + outputs);
            if (outputs.isEmpty()) {
                throw new Exception("No output provided");
            }
            String output = outputs.get(0).trim();

            Pattern p = Pattern.compile("^(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+([LR])$");
            Matcher m = p.matcher(output);
            
            Pattern pSwap = Pattern.compile("^(\\d+)\\s+(\\d+)\\s+SWAP\\s+(\\d+)\\s+(\\d+)$");
            Matcher mSwap = pSwap.matcher(output);

            boolean isNormal = m.matches();
            boolean isSwap = mSwap.matches();

            if (isNormal || isSwap) {
                int x = Integer.parseInt(isNormal ? m.group(1) : mSwap.group(1));
                int y = Integer.parseInt(isNormal ? m.group(2) : mSwap.group(2));
                boolean swapsAllowed = true;
                try {
                    int league = gameManager.getLeagueLevel();
                    if (league == 3) {
                        swapsAllowed = true;
                    } else if (league > 0) {
                        swapsAllowed = false;
                    } else {
                        // Fallback for local tests
                        if (gameManager.getGameParameters() != null) {
                            swapsAllowed = Boolean.valueOf(gameManager.getGameParameters().getProperty("swaps_allowed", "false"));
                        }
                    }
                } catch (Throwable t) {
                    swapsAllowed = false;
                }
                
                if (isSwap && !swapsAllowed) {
                    player.deactivate("SWAP action is not allowed in this league.");
                    player.setScore(-1);
                    advanceTurn();
                    return;
                }

                boolean placed = board.place(x, y, player.getIndex());
                if (!placed) {
                    player.deactivate(String.format("Invalid placement at %d %d", x, y));
                    player.setScore(-1);
                } else {
                    drawMarble(x, y, player.getIndex());
                    
                    if (isSwap) {
                        int b1 = Integer.parseInt(mSwap.group(3));
                        int b2 = Integer.parseInt(mSwap.group(4));
                        boolean swapped = board.swap(b1, b2);
                        if (!swapped) {
                            player.deactivate(String.format("Invalid swap blocks %d and %d", b1, b2));
                            player.setScore(-1);
                        } else {
                            playerActionTexts[player.getIndex()].setText(String.format("Played %d %d SWAP %d %d", x, y, b1, b2));
                            animateSwap(b1, b2);
                            if (checkWinCondition()) return;
                        }
                    } else {
                        int block = Integer.parseInt(m.group(3));
                        String dir = m.group(4);
                        boolean rotated = board.rotate(block, dir);
                        if (!rotated) {
                            player.deactivate(String.format("Invalid rotation block %d dir %s", block, dir));
                            player.setScore(-1);
                        } else {
                            playerActionTexts[player.getIndex()].setText(String.format("Played %d %d %d %s", x, y, block, dir));
                            animateRotation(block, dir);
                            if (checkWinCondition()) return;
                        }
                    }
                }
            } else {
                player.deactivate("Invalid output format: " + output);
                player.setScore(-1);
            }
        } catch (TimeoutException e) {
            player.deactivate("Timeout!");
            player.setScore(-1);
        } catch (Throwable e) {
            String msg = e.getMessage();
            player.deactivate(msg != null ? msg : e.toString());
            player.setScore(-1);
        }

        System.err.println("REFEREE: advancing turn...");
        advanceTurn();
        System.err.println("REFEREE: gameTurn(" + turn + ") finished");
    }
    
    private void drawMarble(int x, int y, int playerIndex) {
        int blocksPerRow = board.getBlocksPerRow();
        int bx = x / 3;
        int by = y / 3;
        int blockId = by * blocksPerRow + bx;
        
        int cellSize = 100;
        int blockSize = cellSize * 3;
        
        int localX = x % 3;
        int localY = y % 3;
        
        // Inverse rotation so it appears in the correct logical spot!
        int px = localX;
        int py = localY;
        for (int i = 0; i < blockRotations[blockId]; i++) {
            int temp = px;
            px = py;
            py = 2 - temp;
        }
        
        Sprite marbleSprite = marbles[blockId][py * 3 + px];
        
        if (playerIndex == 0) {
            marbleSprite.setImage("red_marble.png");
        } else {
            marbleSprite.setImage("blue_marble.png");
        }
        graphicEntityModule.commitEntityState(0.0, marbleSprite);
        
        marbleSprite.setAlpha(1);
        graphicEntityModule.commitEntityState(0.5, marbleSprite);
    }

    private void animateRotation(int blockId, String dir) {
        Group group = blockGroups[blockId];
        double currentRotation = group.getRotation();
        
        double newRotation = currentRotation + (dir.equals("R") ? Math.PI / 2 : -Math.PI / 2);
        group.setRotation(newRotation);
        
        if (dir.equals("R")) {
            blockRotations[blockId] = (blockRotations[blockId] + 1) % 4;
        } else {
            blockRotations[blockId] = (blockRotations[blockId] + 3) % 4;
        }
        
        graphicEntityModule.commitEntityState(1.0, group);
        
        for (int i = 0; i < 9; i++) {
            marbles[blockId][i].setRotation(-newRotation);
            graphicEntityModule.commitEntityState(1.0, marbles[blockId][i]);
        }
    }

    private void animateSwap(int b1, int b2) {
        Group g1 = blockGroups[b1];
        Group g2 = blockGroups[b2];
        
        int tempX = g1.getX();
        int tempY = g1.getY();
        
        g1.setX(g2.getX());
        g1.setY(g2.getY());
        
        g2.setX(tempX);
        g2.setY(tempY);
        
        graphicEntityModule.commitEntityState(1.0, g1, g2);
        
        blockGroups[b1] = g2;
        blockGroups[b2] = g1;
        
        int tempRot = blockRotations[b1];
        blockRotations[b1] = blockRotations[b2];
        blockRotations[b2] = tempRot;
        
        Sprite[] tempMarbles = marbles[b1];
        marbles[b1] = marbles[b2];
        marbles[b2] = tempMarbles;
    }

    private void advanceTurn() {
        List<Player> active = gameManager.getActivePlayers();
        if (active.size() <= 1) {
            for (Player p : active) p.setScore(1);
            gameManager.endGame();
            return;
        }
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % gameManager.getPlayerCount();
        } while (!gameManager.getPlayer(currentPlayerIndex).isActive());
    }

    private boolean checkWinCondition() {
        List<WinLine> winningLines = board.getWinningLines();
        List<Integer> winners = board.getWinningPlayers();
        
        if (!winners.isEmpty() || board.isFull()) {
            
            gameManager.setFrameDuration(2500); // Give plenty of time for the final animation
            
            // 1. Highlight the winning marbles
            for (WinLine line : winningLines) {
                for (java.awt.Point p : line.points) {
                    highlightMarble(p.x, p.y, line.playerId);
                }
            }
            
            // 2. Cinematic Background overlay
            Rectangle bg = graphicEntityModule.createRectangle()
                .setWidth(1920).setHeight(1080)
                .setFillColor(0x000000).setAlpha(0)
                .setZIndex(100);
            graphicEntityModule.commitEntityState(0.8, bg);
            bg.setAlpha(0.85);
            graphicEntityModule.commitEntityState(1.0, bg);
            
            // 3. Cinematic Text
            String winnerText = "";
            int color = 0xffffff;
            if (winners.isEmpty()) {
                winnerText = "DRAW!";
            } else if (winners.size() == 1) {
                winnerText = gameManager.getPlayer(winners.get(0)).getNicknameToken() + " WINS!";
                color = winners.get(0) == 0 ? 0xff4444 : 0x4444ff;
            } else {
                winnerText = "TIE BREAKER DRAW!";
            }
            
            Text t = graphicEntityModule.createText(winnerText)
                .setX(1920/2).setY(1080/2)
                .setAnchor(0.5).setFontSize(120)
                .setFontFamily("SansSerif").setFontWeight(Text.FontWeight.BOLD)
                .setFillColor(color)
                .setAlpha(0).setZIndex(102);
            graphicEntityModule.commitEntityState(0.8, t);
            t.setAlpha(1);
            t.setScale(1.2);
            graphicEntityModule.commitEntityState(1.0, t);
            
            if (!winners.isEmpty()) {
                for (Player p : gameManager.getPlayers()) {
                    if (winners.contains(p.getIndex())) {
                        p.setScore(1);
                    } else if (p.isActive()) {
                        p.setScore(0);
                    }
                }
            } else {
                for (Player p : gameManager.getActivePlayers()) {
                    p.setScore(1);
                }
            }
            gameManager.endGame();
            return true;
        }
        return false;
    }
    
    private void highlightMarble(int x, int y, int playerIndex) {
        int blocksPerRow = board.getBlocksPerRow();
        int bx = x / 3;
        int by = y / 3;
        int blockId = by * blocksPerRow + bx;
        int localX = x % 3;
        int localY = y % 3;
        int px = localX;
        int py = localY;
        for (int i = 0; i < blockRotations[blockId]; i++) {
            int temp = px;
            px = py;
            py = 2 - temp;
        }
        Sprite marbleSprite = marbles[blockId][py * 3 + px];
        
        graphicEntityModule.commitEntityState(0.7, marbleSprite);
        if (playerIndex == 0) {
            marbleSprite.setImage("red_marble_glow.png");
        } else {
            marbleSprite.setImage("blue_marble_glow.png");
        }
        marbleSprite.setZIndex(101);
        graphicEntityModule.commitEntityState(0.8, marbleSprite);
    }
}
