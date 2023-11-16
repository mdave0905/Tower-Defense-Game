package md.tower.defense.tdgame.Objects;

import md.tower.defense.tdgame.Managers.EnemyManager;

import static md.tower.defense.tdgame.Helpers.Constants.Enemies.SOLDIER;

public class Soldier extends Enemy{
    public Soldier(int id, float x, float y, EnemyManager enemyManager) {
        super(id, SOLDIER, x, y, enemyManager);
    }
}