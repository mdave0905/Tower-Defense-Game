package md.tower.defense.tdgame.Managers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Rotate;
import md.tower.defense.tdgame.Helpers.Constants;
import md.tower.defense.tdgame.Controllers.GameScene;
import md.tower.defense.tdgame.Objects.Enemy;
import md.tower.defense.tdgame.Objects.Projectile;
import md.tower.defense.tdgame.Objects.Tower;

import java.util.ArrayList;
import java.util.Objects;

import static md.tower.defense.tdgame.Helpers.Constants.Projectiles.*;
import static md.tower.defense.tdgame.Helpers.Constants.Towers.*;

public class ProjectileManager {
    private GameScene scene;
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private final Image[] proImages = new Image[4];
    private final Image[] explosionImages = new Image[8];
    private int projectileId = 0;
    private ArrayList<Explosion> explosions = new ArrayList<>();
    private Projectile.Point2DFloat explosionPos;
    MediaPlayer explosionMediaPlayer;
    MediaPlayer cannonMediaPlayer;
    MediaPlayer twinGunMediaPlayer;
    MediaPlayer tankMediaPlayer;
    Media explosionSound = new Media(getClass().getResource("/md/tower/defense/tdgame/audio/explosion.wav").toExternalForm());
    Media cannonSound = new Media(getClass().getResource("/md/tower/defense/tdgame/audio/cannon.wav").toExternalForm());
    Media twinGunSound = new Media(getClass().getResource("/md/tower/defense/tdgame/audio/twinGun.wav").toExternalForm());
    Media tankSound = new Media(getClass().getResource("/md/tower/defense/tdgame/audio/tank.wav").toExternalForm());

    public ProjectileManager(GameScene scene) {
        this.scene = scene;
        loadProjectileImages();
        explosionMediaPlayer = new MediaPlayer(explosionSound);
        explosionMediaPlayer.setCycleCount(1);
        explosionMediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                explosionMediaPlayer.stop();
            }
        });
        twinGunMediaPlayer = new MediaPlayer(twinGunSound);
        twinGunMediaPlayer.setVolume(0.5);
        twinGunMediaPlayer.setCycleCount(1);
        twinGunMediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                twinGunMediaPlayer.stop();
            }
        });
        cannonMediaPlayer = new MediaPlayer(cannonSound);
        cannonMediaPlayer.setCycleCount(1);
        cannonMediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                cannonMediaPlayer.stop();
            }
        });
        tankMediaPlayer = new MediaPlayer(tankSound);
        tankMediaPlayer.setCycleCount(1);
        tankMediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                tankMediaPlayer.stop();
            }
        });
    }

    private void loadProjectileImages() {
        for (int i = 1; i <= 4; i++) {
            String fileName = "/md/tower/defense/tdgame/assets/projectiles/projectile" + i + ".png";
            proImages[i - 1] = new Image(Objects.requireNonNull(getClass().getResource(fileName)).toExternalForm());
        }
        loadExplosionImages();
    }

    private void loadExplosionImages() {
        for (int i = 1; i <= 8; i++) {
            String fileName = "/md/tower/defense/tdgame/assets/explosions/explosion" + i + ".png";
            explosionImages[i - 1] = new Image((Objects.requireNonNull(getClass().getResource(fileName))).toExternalForm());
        }
    }

    public void newProjectile(Tower t, Enemy e) {
        int type = getProjectileType(t);

        int xDist = (int) (t.getX() - e.getX());
        int yDist = (int) (t.getY() - e.getY());
        int totDist = Math.abs(xDist) + Math.abs(yDist);

        float xPer = (float) Math.abs(xDist) / totDist;

        float xSpeed = xPer * Constants.Projectiles.GetSpeed(type);
        float ySpeed = Constants.Projectiles.GetSpeed(type) - xSpeed;

        if (t.getX() > e.getX())
            xSpeed *= -1;
        if (t.getY() > e.getY())
            ySpeed *= -1;

        float rotate = 0;
        if (type == MISSILE || type == DOUBLE_FIRE_BALL) {
            float arcValue = (float) Math.atan(yDist / (float) xDist);
            rotate = (float) Math.toDegrees(arcValue) - 90;

            if (xDist < 0) { //check if tower is on left of enemy
                rotate += 180;
            }
        }
        for(Projectile p: projectiles){
            if(!p.isActive()){
                if (p.getProjectileType() == type){
                    p.reuse(t.getX() + 16, t.getY() + 16, xSpeed, ySpeed, t.getDamage(), rotate);
                    return;
                }
            }
        }
        projectiles.add(new Projectile(t.getX() + 16, t.getY() + 16, xSpeed, ySpeed, t.getDamage(), rotate, ++projectileId, type));
    }

    private int getProjectileType(Tower t) {
        return switch (t.getTowerType()) {
            case CANNON -> CANNON_BALL;
            case ROCKET -> MISSILE;
            case TANK -> FIRE_BALL;
            case TWIN_GUN -> DOUBLE_FIRE_BALL;
            default -> 0;
        };
    }

    public void update() {
        for (Projectile p : projectiles) {
            if (p.isActive()) {
                p.move();
                if (isProjectileHittingEnemy(p)) {
                    p.setActive(false);
                    if (p.getProjectileType() == MISSILE) {
                        explosionMediaPlayer.play();
                        explosions.add(new Explosion(p.getPos()));
                        explodeOnEnemies(p);
                    }
                    if(p.getProjectileType() == DOUBLE_FIRE_BALL){
                        twinGunMediaPlayer.play();
                    }
                    if(p.getProjectileType() == CANNON_BALL){
                        cannonMediaPlayer.play();
                    }
                    if(p.getProjectileType() == FIRE_BALL){
                        tankMediaPlayer.play();
                    }
                }else if(isProjectileOutsideBounds(p)){
                    p.setActive(false);
                }
            }
        }

        for (Explosion e : explosions) {
            if (e.getIndex() < 8) {
                e.update();
            }
        }
    }

    private boolean isProjectileOutsideBounds(Projectile p) {
        if(p.getPos().getX() >= 0){
            if(p.getPos().getX() <= 1088){
                if(p.getPos().getY() >= 0){
                    if(p.getPos().getY() <= 720){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void explodeOnEnemies(Projectile p) {
        for (Enemy e : scene.getEnemyManager().getEnemies()) {
            if (e.isAlive()) {
                float radius = 40f;
                float xDist = Math.abs(p.getPos().getX() - e.getX());
                float yDist = Math.abs(p.getPos().getY() - e.getY());
                float realDist = (float) Math.hypot(xDist, yDist);

                if (realDist <= radius) {
                    e.hurt(p.getDamage()/2);
                }
            }
        }
    }

    private boolean isProjectileHittingEnemy(Projectile p) {
        for (Enemy e : scene.getEnemyManager().getEnemies()) {
            if (e.isAlive()) {
                if (e.getBounds().contains(p.getPos().getX(), p.getPos().getY())) {
                    e.hurt(p.getDamage());
                    return true;
                }
            }
        }
        return false;
    }

    private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    private void drawRotatedImage(GraphicsContext gc, Image image, double angle, double tlpx, double tlpy) {
        gc.save(); // saves the current state on stack, including the current transform
        rotate(gc, angle, tlpx + image.getWidth() / 2, tlpy + image.getHeight() / 2);
        gc.drawImage(image, tlpx, tlpy);
        gc.restore(); // back to original state (before rotation)
    }

    public void draw(GraphicsContext gc) {

        for (Projectile p : projectiles) {
            if (p.isActive()) {
                if (p.getProjectileType() == MISSILE || p.getProjectileType() == DOUBLE_FIRE_BALL) {
                    drawRotatedImage(gc, proImages[p.getProjectileType()], p.getRotation(), (int) p.getPos().getX() - 16, (int) p.getPos().getY() - 16);
                } else {
                    gc.drawImage(proImages[p.getProjectileType()], (int) p.getPos().getX() - 16, (int) p.getPos().getY() - 16);
                }

            }
        }

        drawExplosions(gc);

    }

    private void drawExplosions(GraphicsContext gc) {
        for (Explosion e : explosions) {
            if (e.getIndex() < 8) {
                gc.drawImage(explosionImages[e.getIndex()], (int) e.getPos().getX() - 16, (int) e.getPos().getY() - 16);
            }
        }
    }

    public static class Explosion {
        private Projectile.Point2DFloat pos;
        private int explosionTick = 0, explosionIndex = 0;

        public Explosion(Projectile.Point2DFloat pos) {
            this.pos = pos;
        }

        public void update() {
            explosionTick++;
            if (explosionTick >= 4) {
                explosionTick = 0;
                explosionIndex++;
            }
        }

        public Projectile.Point2DFloat getPos() {
            return pos;
        }

        public int getIndex() {
            return explosionIndex;
        }
    }

    public void reset(){
        projectiles.clear();
        explosions.clear();
        projectileId = 0;
    }
}
