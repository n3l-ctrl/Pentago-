package com.codingame.game;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Rectangle;
import com.codingame.gameengine.module.entities.Circle;
import com.codingame.gameengine.module.entities.Text;
import com.google.inject.Inject;

public class Referee extends AbstractReferee {
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;

    private Board board;
    private int currentPlayerIndex = 0;
    
    private Group[] blockGroups;
    private int[] blockRotations;
    private Group[][] marbles;
    private Circle[][] marbleBases;
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
        graphicEntityModule.createRectangle()
                .setWidth(1920)
                .setHeight(1080)
                .setFillColor(0x222222);
                
        graphicEntityModule.createText("VERSION 5")
                .setX(50)
                .setY(50)
                .setFontSize(80)
                .setFillColor(0xff0000)
                .setZIndex(100);

        int blocksPerRow = board.getBlocksPerRow();
        int maxBlocks = blocksPerRow * blocksPerRow;
        blockGroups = new Group[maxBlocks];
        blockRotations = new int[maxBlocks];
        marbles = new Group[maxBlocks][9];
        marbleBases = new Circle[maxBlocks][9];
        
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
                    .setY(startY + by * (blockSize + gap) + blockSize/2);
            
            group.add(graphicEntityModule.createSprite()
                    .setImage("wood_texture.png")
                    .setBaseWidth(blockSize)
                    .setBaseHeight(blockSize)
                    .setX(-blockSize/2)
                    .setY(-blockSize/2)
                    .setZIndex(-10));
            
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int cx = -blockSize/2 + j * cellSize + cellSize/2;
                    int cy = -blockSize/2 + i * cellSize + cellSize/2;
                    
                    // Procedural hole shadow
                    group.add(graphicEntityModule.createCircle()
                            .setRadius(cellSize/2 - 5)
                            .setX(cx)
                            .setY(cy)
                            .setFillColor(0x1a0f00)
                            .setAlpha(0.8)
                            .setZIndex(-5));
                            
                    // 3D Glass Marble Effect
                    Group marbleGroup = graphicEntityModule.createGroup()
                            .setX(cx)
                            .setY(cy)
                            .setAlpha(0)
                            .setZIndex(0);
                            
                    int R = cellSize/2 - 8;
                    
                    Circle base = graphicEntityModule.createCircle()
                            .setRadius(R)
                            .setFillColor(0xffffff)
                            .setZIndex(0);
                            
                    Circle highlight1 = graphicEntityModule.createCircle()
                            .setRadius((int)(R * 0.4))
                            .setX((int)(-R * 0.3))
                            .setY((int)(-R * 0.3))
                            .setFillColor(0xffffff)
                            .setAlpha(0.4)
                            .setZIndex(1);
                            
                    Circle highlight2 = graphicEntityModule.createCircle()
                            .setRadius((int)(R * 0.15))
                            .setX((int)(-R * 0.5))
                            .setY((int)(-R * 0.5))
                            .setFillColor(0xffffff)
                            .setAlpha(0.7)
                            .setZIndex(2);
                            
                    marbleGroup.add(base, highlight1, highlight2);
                    
                    marbles[b][i * 3 + j] = marbleGroup;
                    marbleBases[b][i * 3 + j] = base;
                    group.add(marbleGroup);
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
        System.err.println("REFEREE: gameTurn(" + turn + ") started for player " + currentPlayerIndex);
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
        
        Group marbleGroup = marbles[blockId][py * 3 + px];
        Circle base = marbleBases[blockId][py * 3 + px];
        
        base.setFillColor(gameManager.getPlayer(playerIndex).getColorToken());
        marbleGroup.setAlpha(1);
        
        graphicEntityModule.commitEntityState(0.5, marbleGroup, base);
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
        
        Group[] tempMarbles = marbles[b1];
        marbles[b1] = marbles[b2];
        marbles[b2] = tempMarbles;
        
        Circle[] tempBases = marbleBases[b1];
        marbleBases[b1] = marbleBases[b2];
        marbleBases[b2] = tempBases;
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
        List<Integer> winners = board.getWinningPlayers();
        if (!winners.isEmpty()) {
            for (Player p : gameManager.getPlayers()) {
                if (winners.contains(p.getIndex())) {
                    p.setScore(1);
                } else if (p.isActive()) {
                    p.setScore(0);
                }
            }
            gameManager.endGame();
            return true;
        } else if (board.isFull()) {
            // Draw
            for (Player p : gameManager.getActivePlayers()) {
                p.setScore(1);
            }
            gameManager.endGame();
            return true;
        }
        return false;
    }
}
