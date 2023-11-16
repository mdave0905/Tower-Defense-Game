package md.tower.defense.tdgame.Objects;

import javafx.geometry.Point2D;

public class Projectile {

    public void reuse(int x, int y, float xSpeed, float ySpeed, int damage, float rotate) {
        pos = new Point2DFloat(x, y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.dmg = damage;
        this.rotation = rotate;
        active = true;
    }

    public static class Point2DFloat{
        private float x,y;

        public Point2DFloat(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }

    private Point2DFloat pos;
    private int id, projectileType, dmg;
    private boolean active = true;

    private float xSpeed, ySpeed, rotation;

    public Projectile(float x, float y, float xSpeed, float ySpeed, int dmg, float rotation, int id, int projectileType) {
        pos = new Point2DFloat(x, y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.dmg = dmg;
        this.rotation = rotation;
        this.id = id;
        this.projectileType = projectileType;
    }

    public int getDamage() {
        return dmg;
    }

    public void move(){
        pos.setX(pos.getX()+xSpeed);
        pos.setY(pos.getY()+ySpeed);
    }

    public Point2DFloat getPos() {
        return pos;
    }

    public void setPos(Point2DFloat pos) {
        this.pos = pos;
    }

    public int getId() {
        return id;
    }

    public int getProjectileType() {
        return projectileType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public float getRotation() {
        return rotation;
    }
}
