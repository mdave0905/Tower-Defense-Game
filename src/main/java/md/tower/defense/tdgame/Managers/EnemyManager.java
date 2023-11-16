package md.tower.defense.tdgame.Managers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import md.tower.defense.tdgame.Controllers.GameScene;
import md.tower.defense.tdgame.Objects.*;
import md.tower.defense.tdgame.Helpers.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static md.tower.defense.tdgame.Helpers.Constants.Direction.*;
import static md.tower.defense.tdgame.Helpers.Constants.Enemies.*;

public class EnemyManager {

    GameScene scene;
    private final Image[] enemyImages;
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    List<Point> pathPositions;
    Level level;
    private final int HPBarWidth = 24;

    public EnemyManager(GameScene scene, Level level) {
        this.scene = scene;
        this.level = level;
        if (level.getPathPositions() != null) {
            pathPositions = level.getPathPositions();
        }
        enemyImages = new Image[4];
        loadEnemyImages();
    }

    private void loadEnemyImages() {
        for (int i = 1; i <= 4; i++) {
            String fileName = "/md/tower/defense/tdgame/assets/enemies/enemy" + i + ".png";
            enemyImages[i - 1] = new Image(Objects.requireNonNull(getClass().getResource(fileName)).toExternalForm());
        }
    }

    public void update() throws IOException {
        List<Enemy> enemiesCopy = new ArrayList<>(enemies);

        for (Enemy e : enemiesCopy) {
            if (e.isAlive()) {
                updateEnemyMove(e);
            }
        }
    }

    private void updateEnemyMove(Enemy e) throws IOException {

        int newX = (int) (e.getX() + getSpeedAndWidthX(e.getLastDir(), e.getEnemyType()));
        int newY = (int) (e.getY() + getSpeedAndHeightY(e.getLastDir(), e.getEnemyType()));
        if (isPositionOnPath(newX, newY, pathPositions)) {
            e.move(GetSpeed(e.getEnemyType()), e.getLastDir());
        } else if (isAtEnd(e)) {
            e.kill();
            scene.decreaseLife(1);
        } else {
            setNewDirectionAndMove(e);
        }
    }

    private void setNewDirectionAndMove(Enemy e) {
        int dir = e.getLastDir();

        int xCord = (int) (e.getX() / 32);
        int yCord = (int) (e.getY() / 32);

        fixEnemyOffsetTile(e, dir, xCord, yCord);

        if (isAtEnd(e)) {
            return;
        }

        if (dir == LEFT || dir == RIGHT) {
            int newY = (int) (e.getY() + getSpeedAndHeightY(UP, e.getEnemyType()));
            if (isPositionOnPath((int) e.getX(), newY, pathPositions)) {
                e.move(GetSpeed(e.getEnemyType()), UP);
            } else {
                e.move(GetSpeed(e.getEnemyType()), DOWN);
            }
        } else {
            int newX = (int) (e.getX() + getSpeedAndWidthX(RIGHT, e.getEnemyType()));
            if (isPositionOnPath(newX, (int) e.getY(), pathPositions)) {
                e.move(GetSpeed(e.getEnemyType()), RIGHT);
            } else {
                e.move(GetSpeed(e.getEnemyType()), LEFT);
            }
        }
    }

    private void fixEnemyOffsetTile(Enemy e, int dir, int xCord, int yCord) {
        switch (dir) {
            case RIGHT -> {
                if (xCord < 34) {
                    xCord++;
                }
            }
            case DOWN -> {
                if (yCord < 22) {
                    yCord++;
                }
            }
        }

        e.setPos(xCord * 32, yCord * 32);
    }

    private boolean isAtEnd(Enemy e) {
        if ( ((int)e.getX()) == (level.getEndX() * 32)) {
            return ((int) e.getY()) == (level.getEndY() * 32);
        }
        return false;
    }

    private boolean isPositionOnPath(float x, float y, List<Point> pathPositions) {
        if(x>0 && y>0) {
            int tileX = (int) (x / 32);
            int tileY = (int) (y / 32);
            Point enemyTile = new Point(tileX, tileY);
            return pathPositions.contains(enemyTile);
        }else{
            return false;
        }
    }

    private float getSpeedAndHeightY(int lastDir, int enemyType) {
        if (lastDir == UP) {
            return -GetSpeed(enemyType);
        } else if (lastDir == DOWN) {
            return GetSpeed(enemyType) + 32;
        }
        return 0;
    }

    private float getSpeedAndWidthX(int lastDir, int enemyType) {
        if (lastDir == LEFT) {
            return -GetSpeed(enemyType);
        } else if (lastDir == RIGHT) {
            return GetSpeed(enemyType) + 32;
        }
        return 0;
    }

    public void addEnemy(int enemyType) {
        int x = level.getStartX() * 32;
        int y = level.getStartY() * 32;
        switch (enemyType) {
            case SOLDIER -> enemies.add(new Soldier(0, x, y, this));
            case HERO -> enemies.add(new Hero(0, x, y, this));
            case MONKEY -> enemies.add(new Monkey(0, x, y, this));
            case ROBOT -> enemies.add(new Robot(0, x, y, this));
        }

    }

    public void draw(GraphicsContext gc) {
        for (Enemy e : enemies) {
            if (e.isAlive()) {
                drawEnemy(e, gc);
                drawHealthBar(e, gc);
            }
        }
    }

    private void drawHealthBar(Enemy e, GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);
        gc.fillRect((int) e.getX() + 16 - (getNewBarWidth(e) / 2), (int) e.getY() - 10, getNewBarWidth(e), 4);
        gc.strokeRect((int) e.getX() + 16 - (getNewBarWidth(e) / 2), (int) e.getY() - 10, getNewBarWidth(e), 4);
    }

    private int getNewBarWidth(Enemy e) {
        return (int) (HPBarWidth * e.getHealthBarFloat());
    }

    private void drawEnemy(Enemy e, GraphicsContext gc) {
        //gc.drawImage(enemyImages[e.getEnemyType()], (int) e.getX(), (int) e.getY());

        Image enemyImage = enemyImages[e.getEnemyType()];
        double angle = 0;
        switch (e.getLastDir()) {
            case LEFT -> angle = 180;
            case RIGHT -> angle = 0;
            case UP -> angle = 270;
            case DOWN -> angle = 90;
        }
        gc.save(); // Save the current state on the stack
        gc.translate(e.getX() + enemyImage.getWidth() / 2, e.getY() + enemyImage.getHeight() / 2);
        gc.rotate(angle); // Adjust as needed
        gc.drawImage(enemyImage, -enemyImage.getWidth() / 2, -enemyImage.getHeight() / 2);
        gc.restore(); // Restore to the original state
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void spawnEnemy(int nextEnemy) {
        addEnemy(nextEnemy);
    }

    public int getTotalAliveEnemies() {
        int size = 0;
        for (Enemy e : enemies) {
            if (e.isAlive()) {
                size++;
            }
        }
        return size;
    }

    public void rewardPlayer(int enemyType) {
        scene.addCoins(GetKillReward(enemyType));
    }

    public void reset(){
        enemies.clear();
    }
}
