package md.tower.defense.tdgame.Objects;

import java.util.ArrayList;

public class Wave {
    private final ArrayList<Integer> enemyList = new ArrayList<>();

    public Wave(int soldiers, int heros, int monkeys, int robots) {
        for (int i = 0; i < soldiers; i++) {
            enemyList.add(0);
        }
        for (int i = 0; i < heros; i++) {
            enemyList.add(1);
        }
        for (int i = 0; i < monkeys; i++) {
            enemyList.add(2);
        }
        for (int i = 0; i < robots; i++) {
            enemyList.add(3);
        }
    }

    public ArrayList<Integer> getEnemyList() {
        return enemyList;
    }
}
