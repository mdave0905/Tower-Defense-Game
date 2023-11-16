package md.tower.defense.tdgame.Objects;

import md.tower.defense.tdgame.Helpers.Point;
import md.tower.defense.tdgame.Helpers.TMXReader;

import java.util.ArrayList;
import java.util.List;

public class Level {
    List<Point> pathPositions;
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    ArrayList<Wave> waves = new ArrayList<>();

    public Level(String mapFile, int level) {
        this.pathPositions = TMXReader.read(mapFile);
        if (pathPositions != null) {
            for (Point p : pathPositions) {
                if (p.isStartTile) {
                    startX = p.getX();
                    startY = p.getY();
                    continue;
                }
                if (p.isEndTile) {
                    endX = p.getX();
                    endY = p.getY();
                }
            }
        }
        for (int i = 1; i <= (level * 2); i++) {
            waves.add(new Wave(5 * (level + i - 1), 2 * (level + i - 1), 6 * (level + i - 2), 1 * (level + i - 2)));
        }
    }

    public ArrayList<Wave> getWaves() {
        return waves;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public List<Point> getPathPositions() {
        return pathPositions;
    }


}
