package md.tower.defense.tdgame.Objects;

import md.tower.defense.tdgame.Managers.EnemyManager;

import static md.tower.defense.tdgame.Helpers.Constants.Enemies.HERO;

public class Hero extends Enemy{
    public Hero(int id, float x, float y, EnemyManager enemyManager) {
        super(id, HERO, x, y, enemyManager);
    }
}
