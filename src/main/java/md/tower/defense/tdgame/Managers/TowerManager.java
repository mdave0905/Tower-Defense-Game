package md.tower.defense.tdgame.Managers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import md.tower.defense.tdgame.Controllers.GameScene;
import md.tower.defense.tdgame.Objects.Enemy;
import md.tower.defense.tdgame.Objects.Tower;

import java.util.ArrayList;
import java.util.Objects;

import static md.tower.defense.tdgame.Helpers.Constants.Towers.*;

public class TowerManager {

    private final GameScene scene;
    private final Image[] towerImages = new Image[4];
    private ArrayList<Tower> towers = new ArrayList<>();
    int towerAmount = 0;

    public TowerManager(GameScene scene) {
        this.scene = scene;
        loadTowerImages();
    }

    public Image[] getTowerImages() {
        return towerImages;
    }

    public void update() {
        for (Tower t : towers) {
            t.update();
            attackEnemyIfClose(t);
            if(t.getTarget()!=null && !isEnemyInRange(t,t.getTarget())){
                t.setTarget(null);
            }
        }
    }

    private void attackEnemyIfClose(Tower t) {
        for (Enemy e : scene.getEnemyManager().getEnemies()) {
            if (e.isAlive()) {
                if (isEnemyInRange(t, e)) {
                    if (t.isCoolDownOver()) {
                        scene.shootEnemy(t, e);
                        t.setTarget(e);
                        t.resetCoolDown();
                    }
                }
            }
        }
    }

    private boolean isEnemyInRange(Tower t, Enemy e) {

        int range = getHypoDistance(t.getX(), t.getY(), e.getX(), e.getY());

        return range < t.getRange();
    }

    private int getHypoDistance(float x1, float y1, float x2, float y2) {
        float xDiff = Math.abs(x1 - x2);
        float yDiff = Math.abs(y1 - y2);

        return (int) Math.hypot(xDiff, yDiff);
    }

    private void loadTowerImages() {
        for (int i = 1; i <= 4; i++) {
            String fileName = "/md/tower/defense/tdgame/assets/towers/tower" + i + ".png";
            towerImages[i - 1] = new Image(Objects.requireNonNull(getClass().getResource(fileName)).toExternalForm());
        }
    }

    public void draw(GraphicsContext g) {
        for (Tower t : towers) {
            float rotation = calculateTowerRotation(t);
            drawTower(t, g, rotation);
        }

    }

    private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    private float calculateTowerRotation(Tower t) {
        if (t.getTarget() != null) {
            float angle = (float) Math.toDegrees(Math.atan2(t.getTarget().getY() - t.getY(), t.getTarget().getX() - t.getX()));
            return angle - 270; // Adjusting for the initial rotation of the tower
        }
        return 0; // No target, no rotation
    }


    private void drawRotatedImage(GraphicsContext gc, Image image, double angle, double tlpx, double tlpy) {
        gc.save(); // saves the current state on stack, including the current transform
        rotate(gc, angle, tlpx + image.getWidth() / 2, tlpy + image.getHeight() / 2);
        gc.drawImage(image, tlpx, tlpy);
        gc.restore(); // back to original state (before rotation)
    }

    private void drawTower(Tower t, GraphicsContext gc, float rotation) {
        drawRotatedImage(gc, towerImages[t.getTowerType()],rotation,t.getX(),t.getY());
    }


    public void addTower(Tower tower, int x, int y) {
        towers.add(new Tower(x, y, ++towerAmount, tower.getTowerType()));
    }

    public Tower getTowerAt(int mouseX, int mouseY) {
        for (Tower t : towers) {
            if (t.getX() == mouseX)
                if (t.getY() == mouseY)
                    return t;
        }
        return null;
    }

    public void removeTower(Tower highlightedTower) {
        for(int i = 0; i< towers.size(); i++){
            if(towers.get(i).getId()== highlightedTower.getId()){
                towers.remove(i);
            }
        }
    }

    public void upgradeTower(Tower highlightedTower) {
        for (Tower t: towers) {
            if(t.getId() == highlightedTower.getId()){
                t.upgradeTower();
            }
        }
    }

    public void updateMaxTowerLevels(){
        int maxCannonLevel = 1;
        int maxTankLevel = 1;
        int maxTwinGunLevel = 1;
        int maxRocketLevel = 1;
        for(Tower t: towers){
            switch (t.getTowerType()) {
                case CANNON -> {
                    if(t.getUpgradeLevel()>maxCannonLevel){
                        maxCannonLevel = t.getUpgradeLevel();
                    }
                }
                case TANK -> {
                    if(t.getUpgradeLevel()>maxTankLevel){
                        maxTankLevel = t.getUpgradeLevel();
                    }
                }
                case TWIN_GUN -> {
                    if(t.getUpgradeLevel()>maxTwinGunLevel){
                        maxTwinGunLevel = t.getUpgradeLevel();
                    }
                }
                case ROCKET -> {
                    if(t.getUpgradeLevel()>maxRocketLevel){
                        maxRocketLevel = t.getUpgradeLevel();
                    }
                }
            }
        }

        MAX_CANNON_UPGRADE = maxCannonLevel;
        MAX_TANK_UPGRADE = maxTankLevel;
        MAX_TWIN_GUN_UPGRADE = maxTwinGunLevel;
        MAX_ROCKET_UPGRADE = maxRocketLevel;
    }

    public void reset(){
        towers.clear();
        towerAmount = 0;
    }
}
