package md.tower.defense.tdgame.Controllers;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import md.tower.defense.tdgame.Main;
import md.tower.defense.tdgame.Managers.EnemyManager;
import md.tower.defense.tdgame.Managers.ProjectileManager;
import md.tower.defense.tdgame.Managers.TowerManager;
import md.tower.defense.tdgame.Managers.WaveManager;
import md.tower.defense.tdgame.Objects.Enemy;
import md.tower.defense.tdgame.Objects.Level;
import md.tower.defense.tdgame.Objects.Tower;
import md.tower.defense.tdgame.Helpers.Point;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

import static md.tower.defense.tdgame.Helpers.Constants.Towers.*;
import static md.tower.defense.tdgame.Main.*;

public class GameScene implements Initializable {

    @FXML
    public Label damageStat;
    @FXML
    public Label rangeStat;
    @FXML
    public Label cooldownStat;
    @FXML
    public Label costStat;
    @FXML
    private Button sellBtn;
    @FXML
    private Button upgradeBtn;
    @FXML
    private Button pauseBtn;
    @FXML
    private VBox towerInfo;
    @FXML
    private ImageView mapImageView;
    @FXML
    private Label levelLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label enemyLabel;
    @FXML
    private Label waveLabel;
    @FXML
    private Label coinsLabel;
    @FXML
    private Label livesLabel;
    @FXML
    private Canvas canvas;
    private GraphicsContext gc;
    private DecimalFormat format;
    Tower selectedTower, highlightedTower;
    int mouseX, mouseY;
    private int coins = 750;
    private int lives = 10;
    @FXML
    private Pane root;
    private int levelIndex = 0;
    private final int maxLevelIndex = 2;

    AnimationTimer timer;

    ArrayList<Level> levels = new ArrayList<>();
    MediaPlayer towerPlacedMediaPlayer;
    MediaPlayer towerSelectedMediaPlayer;
    Media towerPlaced = new Media(getClass().getResource("/md/tower/defense/tdgame/audio/placed.wav").toExternalForm());
    Media towerSelected = new Media(getClass().getResource("/md/tower/defense/tdgame/audio/select.wav").toExternalForm());
    private final WaveManager waveManager = new WaveManager(GameScene.this);
    private boolean placedAudioPlaying;

    {
        for (int i = 0; i < maxLevelIndex + 1; i++) {
            Level l = new Level("/md/tower/defense/tdgame/assets/levels/level" + (i + 1) + ".tmx", (i + 1));
            levels.add(l);
        }
        waveManager.setWaves(levels.get(levelIndex).getWaves());
    }

    private final TowerManager towerManager = new TowerManager(GameScene.this);
    private final ProjectileManager projectileManager = new ProjectileManager(GameScene.this);
    private EnemyManager enemyManager = new EnemyManager(GameScene.this, levels.get(levelIndex));
    private int goldTick = 0;
    private boolean gamePaused = false;

    @FXML
    void pauseGame(ActionEvent event) {
        if (!gamePaused) {
            gamePaused = true;
            pauseBtn.setText("Resume");
        } else {
            gamePaused = false;
            pauseBtn.setText("Pause");
        }
    }

    @FXML
    void goToMenu(ActionEvent event) throws IOException {
        MainMenu.bgPlayer.stop();
        resetEverything();
        FXMLLoader fxmlLoader = new FXMLLoader(mainMenu);
        Pane root = fxmlLoader.load();
        Scene mainMenu = new Scene(root);
        mainMenu.setCursor(cursor);
        Main.primaryStage.setScene(mainMenu);
    }

    @FXML
    void sellTower(ActionEvent event) {
        if (highlightedTower != null) {
            towerManager.removeTower(highlightedTower);
            coins += getSellAmount(highlightedTower);
            highlightedTower = null;
            towerInfo.setVisible(false);
            sellBtn.setVisible(false);
            upgradeBtn.setVisible(false);
        }
    }

    @FXML
    void upgradeTower(ActionEvent event) {
        if (highlightedTower != null) {
            if (highlightedTower.getUpgradeLevel() < 3 && coins >= getUpgradeAmount(highlightedTower)) {
                if (highlightedTower.getUpgradeLevel() == 2) {
                    upgradeBtn.setVisible(false);
                    levelLabel.setText("MAX LEVEL");
                } else {
                    levelLabel.setText("Level " + (highlightedTower.getUpgradeLevel() + 1));
                }

                towerManager.upgradeTower(highlightedTower);
                damageStat.setText("💥 Damage: " + highlightedTower.getDamage());
                rangeStat.setText("🎯 Range: " + highlightedTower.getRange());
                cooldownStat.setText("❄ Cooldown: " + highlightedTower.getCoolDown() / 60 + "s");
                costStat.setText("💵 Cost: " + GetCost(highlightedTower.getTowerType()));
                sellBtn.setText("Sell (" + getSellAmount(highlightedTower) + ")");
                coins -= getUpgradeAmount(highlightedTower);
            }
        }
    }

    @FXML
    void towerClicked(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            ImageView i = (ImageView) event.getSource();
            if (coins >= GetCost(getTowerType(i))) {
                selectedTower = new Tower((int) event.getX(), (int) event.getY(), 0, getTowerType(i));
            }
        }
    }

    @FXML
    void checkBalance(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
            ImageView i = (ImageView) event.getSource();
            int towerCost = GetCost(getTowerType(i));
            if (towerCost > coins) {
                i.setBlendMode(BlendMode.GREEN);
            }

            towerInfo.setLayoutY(80 * (getTowerType(i) + 1));
            damageStat.setText("💥 Damage: " + GetDamage(getTowerType(i)));
            rangeStat.setText("🎯 Range: " + GetRange(getTowerType(i)));
            cooldownStat.setText("❄ Cooldown: " + GetCoolDown(getTowerType(i)) / 60 + "s");
            costStat.setText("💵 Cost: " + GetCost(getTowerType(i)));
            towerInfo.setVisible(true);
        }
    }

    @FXML
    void removeCheck(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
            ImageView i = (ImageView) event.getSource();
            i.setBlendMode(BlendMode.SRC_OVER);
            towerInfo.setVisible(false);
            towerInfo.setLayoutY(120);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = canvas.getGraphicsContext2D();
        canvas.setMouseTransparent(true);
        towerPlacedMediaPlayer = new MediaPlayer(towerPlaced);
        towerSelectedMediaPlayer = new MediaPlayer(towerSelected);
        towerPlacedMediaPlayer.setCycleCount(1);
        towerSelectedMediaPlayer.setCycleCount(1);
        towerPlacedMediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                towerPlacedMediaPlayer.stop();
            }
        });
        towerSelectedMediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                towerSelectedMediaPlayer.stop();
            }
        });
        mapImageView.setImage(new Image(Objects.requireNonNull(getClass().getResource("/md/tower/defense/tdgame/assets/levels/level" + (levelIndex + 1) + ".png")).toExternalForm()));
        root.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int mouseEventX = (int) mouseEvent.getX();
                int mouseEventY = (int) mouseEvent.getY();
                mouseX = ((mouseEventX / 32) * 32);
                mouseY = ((mouseEventY / 32) * 32);
            }
        });

        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getX() < 1088) {
                    if (selectedTower != null) {
                        if (isTileValid(mouseX, mouseY, levels.get(levelIndex).getPathPositions())) {
                            if (getTowerAt(mouseX, mouseY) == null) {
                                towerPlacedMediaPlayer.play();
                                placedAudioPlaying = true;
                                towerManager.addTower(selectedTower, mouseX, mouseY);
                                removeGold(selectedTower.getTowerType());
                                selectedTower = null;
                            }
                        }
                    }
                    if (getTowerAt(mouseX, mouseY) != null) {
                        if(!placedAudioPlaying) {
                            towerSelectedMediaPlayer.play();
                        }
                        highlightedTower = getTowerAt(mouseX, mouseY);
                        sellBtn.setText("Sell (" + getSellAmount(highlightedTower) + ")");
                        sellBtn.setVisible(true);
                        levelLabel.setVisible(true);
                        levelLabel.setText("Level " + highlightedTower.getUpgradeLevel());
                        if (highlightedTower.getUpgradeLevel() == 3) {
                            upgradeBtn.setVisible(false);
                            levelLabel.setText("MAX LEVEL");
                        } else {
                            upgradeBtn.setVisible(true);
                            upgradeBtn.setText("Upgrade (" + getUpgradeAmount(highlightedTower) + ")");
                        }
                        towerInfo.setLayoutY(80 * (highlightedTower.getTowerType() + 1));
                        damageStat.setText("💥 Damage: " + highlightedTower.getDamage());
                        rangeStat.setText("🎯 Range: " + highlightedTower.getRange());
                        cooldownStat.setText("❄ Cooldown: " + highlightedTower.getCoolDown() / 60 + "s");
                        costStat.setText("💵 Cost: " + GetCost(highlightedTower.getTowerType()));
                        towerInfo.setVisible(true);
                    } else {
                        levelLabel.setVisible(false);
                        highlightedTower = null;
                        towerInfo.setVisible(false);
                        sellBtn.setVisible(false);
                        upgradeBtn.setVisible(false);
                    }
                    placedAudioPlaying = false;
                }
            }
        });
        format = new DecimalFormat("0.0");
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    selectedTower = null;
                }
            }
        });
        gameLoop();
    }

    public void decreaseLife(int i) throws IOException {
        lives -= i;
        if (lives <= 0) {
            goToGameOver(false);
        }
    }

    private void removeGold(int towerType) {
        coins -= GetCost(towerType);
    }

    private Tower getTowerAt(int mouseX, int mouseY) {
        return towerManager.getTowerAt(mouseX, mouseY);
    }

    private int getSellAmount(Tower highlightedTower) {
        int upgradeCost = (highlightedTower.getUpgradeLevel() - 1) * (getUpgradeAmount(highlightedTower) / 2);
        return (3 * GetCost(highlightedTower.getTowerType()) / 5) + upgradeCost;
    }

    private int getUpgradeAmount(Tower highlightedTower) {
        return (int) (GetCost(highlightedTower.getTowerType()) / 1.5);
    }

    private boolean isTileValid(int mouseX, int mouseY, List<Point> pathPositions) {
        int tileX = (mouseX / 32);
        int tileY = (mouseY / 32);

        // Create a Point object for the current tile
        Point towerTile = new Point(tileX, tileY);
        // Check if the enemy's tile is in the pathPositions list
        return !pathPositions.contains(towerTile);
    }

    private int getTowerType(ImageView view) {
        return switch (view.getId()) {
            case "tower1" -> CANNON;
            case "tower2" -> TANK;
            case "tower3" -> TWIN_GUN;
            case "tower4" -> ROCKET;
            default -> 0;
        };
    }

    private void gameLoop() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                try {
                    update();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                render(gc);
            }
        };
        timer.start();
    }

    public void render(GraphicsContext gc) {
        gc.clearRect(0, 0, 1088, 704);
        enemyManager.draw(gc);
        towerManager.draw(gc);
        drawSelectedTower(gc);
        drawHighlight(gc);
        drawHighlightedTowerRange(gc);
        projectileManager.draw(gc);
        drawWaveInfos();
        drawCoinAmount();
        drawLivesAmount();
    }

    private void drawLivesAmount() {
        livesLabel.setText("Lives: " + lives);
    }

    private void drawCoinAmount() {
        coinsLabel.setText("Cash: " + coins);
    }

    private void drawWaveInfos() {
        drawWaveTimeInfo();
        drawEnemiesLeftInfo();
        drawWavesLeftInfo();
    }

    private void drawWavesLeftInfo() {
        int current = waveManager.getWaveIndex() + 1;
        int size = waveManager.getWaves().size();
        waveLabel.setText("Wave " + current + " / " + size);
    }

    private void drawEnemiesLeftInfo() {
        int remaining = enemyManager.getTotalAliveEnemies();
        enemyLabel.setText("Enemies Left: " + remaining);
    }

    private void drawWaveTimeInfo() {
        if (waveManager.isWaveTimerStarted()) {
            float timeLeft = waveManager.getTimeLeft();
            String formattedText = format.format(timeLeft);
            timeLabel.setText("New wave in: " + formattedText);
        } else {
            timeLabel.setText("");
        }
    }

    private void drawHighlightedTowerRange(GraphicsContext gc) {
        if (highlightedTower != null) {
            gc.setStroke(Color.WHITE);
            gc.strokeOval(highlightedTower.getX() - highlightedTower.getRange() + 16,
                    highlightedTower.getY() - highlightedTower.getRange() + 16,
                    highlightedTower.getRange() * 2,
                    highlightedTower.getRange() * 2);
        }
    }

    private void drawHighlight(GraphicsContext gc) {
        if (selectedTower != null) {
            gc.setStroke(Color.WHITE);
            gc.strokeOval(mouseX - selectedTower.getRange() + 16,
                    mouseY - selectedTower.getRange() + 16,
                    selectedTower.getRange() * 2,
                    selectedTower.getRange() * 2);
            if (isTileValid(mouseX, mouseY, levels.get(levelIndex).getPathPositions())) {
                if (getTowerAt(mouseX, mouseY) == null) {
                    gc.setStroke(Color.BLACK);
                } else {
                    gc.setStroke(Color.RED);
                }
            } else {
                gc.setStroke(Color.RED);
            }
            gc.strokeRect(mouseX, mouseY, 32, 32);
        }
    }

    public void update() throws IOException {
        if (!gamePaused) {
            waveManager.update();

            goldTick++;
            if (goldTick % (60 * 1) == 0) {
                addCoins(1);
            }

            if (isAllEnemiesDead()) {
                if (wavesRemaining()) {
                    waveManager.startWaveTimer();
                    if (isWaveTimerOver()) {
                        waveManager.increaseWaveIndex();
                        enemyManager.getEnemies().clear();
                        waveManager.resetEnemyIndex();
                    }
                } else {
                    goToNextLevel();
                }
            }

            if (isTimeForNewEnemy()) {
                spawnEnemy();
            }
            enemyManager.update();
            towerManager.update();
            projectileManager.update();
        }
    }

    private void goToNextLevel() throws IOException {
        if (levelIndex < maxLevelIndex) {
            towerManager.updateMaxTowerLevels();
            levelIndex++;
            mapImageView.setImage(new Image(Objects.requireNonNull(getClass().getResource("/md/tower/defense/tdgame/assets/levels/level" + (levelIndex + 1) + ".png")).toExternalForm()));
            enemyManager = new EnemyManager(GameScene.this, levels.get(levelIndex));
            resetEverything();
            waveManager.setWaves(levels.get(levelIndex).getWaves());
        } else {
            goToGameOver(true);
        }
    }

    private void goToGameOver(boolean gameCompleted) throws IOException {
        timer.stop();
        FXMLLoader fxmlLoader = new FXMLLoader(gameOver);
        Pane root = fxmlLoader.load();
        GameOver gameOverController = fxmlLoader.getController();
        gameOverController.setGameScene(this);
        gameOverController.isGameCompleted(gameCompleted);
        Scene gameOver = new Scene(root);
        gameOver.setCursor(cursor);
        Main.primaryStage.setScene(gameOver);
    }

    private boolean isWaveTimerOver() {
        return waveManager.isWaveTimerOver();
    }

    private boolean wavesRemaining() {
        return waveManager.isThereWaveRemaining();
    }

    private boolean isAllEnemiesDead() {
        if (waveManager.enemiesRemainingInWave()) {
            return false;
        }
        for (Enemy e : enemyManager.getEnemies()) {
            if (e.isAlive()) {
                return false;
            }
        }
        return true;
    }

    private void spawnEnemy() {
        enemyManager.spawnEnemy(waveManager.getNextEnemy());
    }

    private boolean isTimeForNewEnemy() {
        if (waveManager.isTimeForNewEnemy()) {
            return waveManager.enemiesRemainingInWave();
        }
        return false;
    }

    public void shootEnemy(Tower t, Enemy e) {
        projectileManager.newProjectile(t, e);
    }

    private void drawSelectedTower(GraphicsContext gc) {
        if (selectedTower != null) {
            gc.drawImage(towerManager.getTowerImages()[selectedTower.getTowerType()], mouseX, mouseY);
        }
    }

    public void addCoins(int coins) {
        this.coins += coins;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public void resetEverything() {
        lives = 10;
        coins = 750;
        selectedTower = null;
        highlightedTower = null;
        goldTick = 0;
        gamePaused = false;

        enemyManager.reset();
        towerManager.reset();
        projectileManager.reset();
        waveManager.reset();
    }
}
