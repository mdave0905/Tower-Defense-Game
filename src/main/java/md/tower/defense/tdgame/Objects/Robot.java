package md.tower.defense.tdgame.Objects;

import md.tower.defense.tdgame.Managers.EnemyManager;

import static md.tower.defense.tdgame.Helpers.Constants.Enemies.ROBOT;

public class Robot extends Enemy{
    public Robot(int id, float x, float y, EnemyManager enemyManager) {
        super(id, ROBOT, x, y,enemyManager);
    }
}
