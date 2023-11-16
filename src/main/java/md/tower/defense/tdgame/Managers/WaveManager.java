package md.tower.defense.tdgame.Managers;

import md.tower.defense.tdgame.Controllers.GameScene;
import md.tower.defense.tdgame.Objects.Wave;

import java.util.ArrayList;

public class WaveManager {

    private GameScene scene;
    private ArrayList<Wave> waves = new ArrayList<>();

    private int enemySpawnTickLimit = 60 * 1;
    private int enemySpawnTick = enemySpawnTickLimit;
    private int enemyIndex, waveIndex;
    private int waveTickLimit = 5 * 60;
    private int waveTick = 0;
    private boolean waveStartTimer;
    private boolean waveTickTimeOver;

    public WaveManager(GameScene scene) {
        this.scene = scene;
        //createWaves();
    }

    public void update() {
        if (enemySpawnTick < enemySpawnTickLimit) {
            enemySpawnTick++;
        }

        if (waveStartTimer) {
            waveTick++;
            if (waveTick >= waveTickLimit) {
                waveTickTimeOver = true;
            }
        }
    }

    public int getNextEnemy() {
        enemySpawnTick = 0;
        return waves.get(waveIndex).getEnemyList().get(enemyIndex++);
    }

    public void setWaves(ArrayList<Wave> waves) {
        this.waves = waves;
    }

    public ArrayList<Wave> getWaves() {
        return waves;
    }

    public boolean isTimeForNewEnemy() {
        return enemySpawnTick >= enemySpawnTickLimit;
    }

    public boolean enemiesRemainingInWave() {
        if (!waves.isEmpty() && waveIndex < waves.size()) {
            return enemyIndex < waves.get(waveIndex).getEnemyList().size();
        }
        return false;
    }

    public boolean isThereWaveRemaining() {
        return waveIndex + 1 < waves.size();
    }

    public void startWaveTimer() {
        waveStartTimer = true;
    }

    public boolean isWaveTimerOver() {
        return waveTickTimeOver;
    }

    public void increaseWaveIndex() {
        waveIndex++;
        waveTick = 0;
        waveTickTimeOver = false;
        waveStartTimer = false;
    }

    public void resetEnemyIndex() {
        enemyIndex = 0;
    }

    public int getWaveIndex() {
        return waveIndex;
    }

    public float getTimeLeft() {
        float ticksLeft = waveTickLimit - waveTick;
        return ticksLeft / 60.0f;
    }

    public boolean isWaveTimerStarted() {
        return waveStartTimer;
    }

    public void reset() {
        waves.clear();
        enemyIndex = 0;
        waveIndex = 0;
        waveStartTimer = false;
        waveTickTimeOver = false;
        waveTick = 0;
        enemySpawnTick = enemySpawnTickLimit;
    }
}
