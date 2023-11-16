package md.tower.defense.tdgame.Helpers;

public class Constants {
    public static class Direction {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class Projectiles {
        public static final int CANNON_BALL = 0;
        public static final int MISSILE = 1;
        public static final int FIRE_BALL = 2;
        public static final int DOUBLE_FIRE_BALL = 3;

        public static float GetSpeed(int type) {
            switch (type) {
                case CANNON_BALL -> {
                    return 7.5f;
                }
                case MISSILE -> {
                    return 15f;
                }
                case FIRE_BALL -> {
                    return 12.5f;
                }
                case DOUBLE_FIRE_BALL -> {
                    return 10f;
                }
            }
            return 0;
        }
    }

    public static class Enemies {
        public static final int SOLDIER = 0;
        public static final int HERO = 1;
        public static final int MONKEY = 2;
        public static final int ROBOT = 3;

        public static float GetSpeed(int enemyType) {
            switch (enemyType) {
                case SOLDIER -> {
                    return 0.5f;
                }
                case HERO -> {
                    return 0.6f;
                }
                case MONKEY -> {
                    return 0.85f;
                }
                case ROBOT -> {
                    return 1f;
                }
            }
            return 0;
        }

        public static int GetStartHealth(int enemyType) {
            switch (enemyType) {
                case SOLDIER -> {
                    return 400;
                }
                case HERO -> {
                    return 800;
                }
                case MONKEY -> {
                    return 300;
                }
                case ROBOT -> {
                    return 1200;
                }
            }
            return 0;
        }

        public static int GetKillReward(int enemyType) {
            switch (enemyType) {
                case SOLDIER -> {
                    return 15;
                }
                case HERO -> {
                    return 20;
                }
                case MONKEY -> {
                    return 10;
                }
                case ROBOT -> {
                    return 25;
                }
            }
            return 0;
        }
    }

    public static class Towers {
        public static final int CANNON = 0;
        public static final int TANK = 1;
        public static final int TWIN_GUN = 2;
        public static final int ROCKET = 3;
        public static int MAX_CANNON_UPGRADE = 1;
        public static int MAX_TANK_UPGRADE = 1;
        public static int MAX_TWIN_GUN_UPGRADE = 1;
        public static int MAX_ROCKET_UPGRADE = 1;

        public static int GetDamage(int towerType) {
            switch (towerType) {
                case CANNON -> {
                    return 150 + (20 * (MAX_CANNON_UPGRADE - 1));
                }
                case TANK -> {
                    return 250 + (30 * (MAX_TANK_UPGRADE - 1));
                }
                case TWIN_GUN -> {
                    return 50 + (10 * (MAX_TWIN_GUN_UPGRADE - 1));
                }
                case ROCKET -> {
                    return 400 + (40 * (MAX_ROCKET_UPGRADE - 1));
                }
            }
            return 0;
        }

        public static int GetCost(int towerType) {
            switch (towerType) {
                case CANNON -> {
                    return 200;
                }
                case TANK -> {
                    return 300;
                }
                case TWIN_GUN -> {
                    return 150;
                }
                case ROCKET -> {
                    return 400;
                }
            }
            return 0;
        }

        public static int GetRange(int towerType) {
            switch (towerType) {
                case CANNON -> {
                    return 150 + (10 * (MAX_CANNON_UPGRADE - 1));
                }
                case TANK -> {
                    return 100 + (30 * (MAX_TANK_UPGRADE - 1));
                }
                case TWIN_GUN -> {
                    return 200 + (5 * (MAX_TWIN_GUN_UPGRADE - 1));
                }
                case ROCKET -> {
                    return 250 + (10 * (MAX_ROCKET_UPGRADE - 1));
                }
            }
            return 0;
        }

        public static float GetCoolDown(int towerType) {
            switch (towerType) {
                case CANNON -> {
                    return (float) ((60 * 2) - ((60 * 0.5) * (MAX_CANNON_UPGRADE - 1)));
                }
                case TANK -> {
                    return (float) ((60 * 3) - ((60 * 0.75) * (MAX_TANK_UPGRADE - 1)));
                }
                case TWIN_GUN -> {
                    return (float) ((60 * 1) - ((60 * 0.1) * (MAX_TWIN_GUN_UPGRADE - 1)));
                }
                case ROCKET -> {
                    return (60 * 4) - ((60 * 1) * (MAX_ROCKET_UPGRADE - 1));
                }
            }
            return 0;
        }
    }
}
