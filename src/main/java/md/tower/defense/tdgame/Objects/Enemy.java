package md.tower.defense.tdgame.Objects;

import javafx.scene.shape.Rectangle;
import md.tower.defense.tdgame.Managers.EnemyManager;

import static md.tower.defense.tdgame.Helpers.Constants.Direction.*;
import static md.tower.defense.tdgame.Helpers.Constants.Enemies.*;

public abstract class Enemy {
    protected int health;
    protected int maxHealth;
    protected int id;
    protected int enemyType;
    protected float x, y;
    protected Rectangle bounds;
    protected int lastDir;
    protected boolean alive = true;
    protected EnemyManager enemyManager;

    public Enemy(int id, int enemyType, float x, float y, EnemyManager enemyManager) {
        this.id = id;
        this.enemyType = enemyType;
        this.x = x;
        this.enemyManager = enemyManager;
        this.y = y;
        bounds = new Rectangle((int) x, (int) y, 32, 32);
        lastDir = RIGHT;
        setStartHealth();
    }

    private void setStartHealth() {
        health = GetStartHealth(enemyType);
        maxHealth = health;
    }

    public void hurt(int damage){
        this.health -= damage;
        if(health<=0){
            alive = false;
            enemyManager.rewardPlayer(enemyType);
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void move(float speed, int dir) {
        lastDir = dir;
        switch (dir) {
            case LEFT -> this.x -= speed;
            case UP -> this.y -= speed;
            case RIGHT -> this.x += speed;
            case DOWN -> this.y += speed;
        }

        updateHitBox();
    }

    private void updateHitBox() {
        bounds.setX(x);
        bounds.setY(y);
    }

    public void setPos(int x, int y) {
        //don't use this one for move, this is for position offset fix
        this.x = x;
        this.y = y;
    }

    public float getHealthBarFloat(){
        return health/(float)maxHealth;
    }

    public int getLastDir() {
        return lastDir;
    }

    public int getHealth() {
        return health;
    }

    public int getId() {
        return id;
    }

    public int getEnemyType() {
        return enemyType;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void kill() {
        alive = false;
        health = 0;
    }
}
