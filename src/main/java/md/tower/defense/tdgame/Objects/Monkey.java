package md.tower.defense.tdgame.Objects;

import md.tower.defense.tdgame.Managers.EnemyManager;

import static md.tower.defense.tdgame.Helpers.Constants.Enemies.MONKEY;

public class Monkey extends Enemy{
    public Monkey(int id, float x, float y, EnemyManager enemyManager) {
        super(id, MONKEY, x, y, enemyManager);
    }
}
