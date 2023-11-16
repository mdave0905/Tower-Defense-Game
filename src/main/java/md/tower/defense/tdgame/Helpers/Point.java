package md.tower.defense.tdgame.Helpers;

import java.util.Objects;

public class Point {
    int x, y;

    public boolean isStartTile = false;
    public boolean isEndTile = false;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isStartTile() {
        return isStartTile;
    }

    public void setStartTile(boolean startTile) {
        isStartTile = startTile;
    }

    public boolean isEndTile() {
        return isEndTile;
    }

    public void setEndTile(boolean endTile) {
        isEndTile = endTile;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point point = (Point) obj;
        return Float.compare(point.x, x) == 0 && Float.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
