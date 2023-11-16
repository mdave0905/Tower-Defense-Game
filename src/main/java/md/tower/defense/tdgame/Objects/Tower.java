package md.tower.defense.tdgame.Objects;

import static md.tower.defense.tdgame.Helpers.Constants.Towers.*;

public class Tower {

    private int x, y, id, towerType, cdTick, damage, range;
    private Enemy target;
    private float coolDown;
    private int upgradeLevel;

    public Tower(int x, int y, int id, int towerType) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.towerType = towerType;
        switch (towerType) {
            case CANNON -> upgradeLevel = MAX_CANNON_UPGRADE;
            case TANK -> upgradeLevel = MAX_TANK_UPGRADE;
            case TWIN_GUN -> upgradeLevel = MAX_TWIN_GUN_UPGRADE;
            case ROCKET -> upgradeLevel = MAX_ROCKET_UPGRADE;
        }
        setDefaultDamage();
        setDefaultRange();
        setDefaultCoolDown();
    }

    public void upgradeTower(){
        upgradeLevel++;
        switch (towerType) {
            case CANNON -> {
                damage = GetDamage(towerType)+(20*(upgradeLevel-1));
                range = GetRange(towerType)+(10*(upgradeLevel-1));
                coolDown = (float) (GetCoolDown(towerType)-((60*0.5)*(upgradeLevel-1)));
            }

            case TANK -> {
                damage = GetDamage(towerType)+(30*(upgradeLevel-1));
                range = GetRange(towerType)+(30*(upgradeLevel-1));
                coolDown = (float) (GetCoolDown(towerType)-((60*0.75)*(upgradeLevel-1)));
            }

            case TWIN_GUN -> {
                damage = GetDamage(towerType)+(10*(upgradeLevel-1));
                range = GetRange(towerType)+(5*(upgradeLevel-1));
                coolDown = (float) (GetCoolDown(towerType)-((60*0.1)*(upgradeLevel-1)));
            }

            case ROCKET -> {
                damage = GetDamage(towerType)+(40*(upgradeLevel-1));
                range = GetRange(towerType)+(10*(upgradeLevel-1));
                coolDown = (GetCoolDown(towerType)-((60*1)*(upgradeLevel-1)));
            }
        }
    }

    public int getUpgradeLevel() {
        return upgradeLevel;
    }

    public Enemy getTarget() {
        return target;
    }

    public void setTarget(Enemy e) {
        this.target = e;
    }

    public void update(){
        cdTick++;
    }

    private void setDefaultCoolDown() {
        coolDown = GetCoolDown(towerType);
    }

    private void setDefaultRange() {
        range = GetRange(towerType);
    }

    private void setDefaultDamage() {
        damage = GetDamage(towerType);
    }

    public int getDamage() {
        return damage;
    }

    public float getRange() {
        return range;
    }

    public float getCoolDown() {
        return coolDown;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTowerType() {
        return towerType;
    }

    public void setTowerType(int towerType) {
        this.towerType = towerType;
    }

    public boolean isCoolDownOver() {
        return cdTick>=coolDown;
    }

    public void resetCoolDown() {
        cdTick = 0;
    }
}
