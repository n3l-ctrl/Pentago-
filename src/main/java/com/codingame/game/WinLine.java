package com.codingame.game;

import java.awt.Point;
import java.util.List;

public class WinLine {
    public int playerId;
    public List<Point> points;

    public WinLine(int playerId, List<Point> points) {
        this.playerId = playerId;
        this.points = points;
    }
}
