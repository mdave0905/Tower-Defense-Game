package md.tower.defense.tdgame.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import md.tower.defense.tdgame.Main;
import java.io.IOException;

import static md.tower.defense.tdgame.Main.*;

public class GameOver {

    @FXML
    private Label title;
    private GameScene scene;

    @FXML
    public void goToMenu(ActionEvent actionEvent) throws IOException {
        MainMenu.bgPlayer.stop();
        scene.resetEverything();
        FXMLLoader fxmlLoader = new FXMLLoader(mainMenu);
        Pane root = fxmlLoader.load();
        Scene mainMenu = new Scene(root);
        mainMenu.setCursor(cursor);
        Main.primaryStage.setScene(mainMenu);
    }

    @FXML
    public void restartLevel(ActionEvent actionEvent) throws IOException {
        scene.resetEverything();
        FXMLLoader fxmlLoader = new FXMLLoader(gameScene);
        Pane root = fxmlLoader.load();
        Scene mainMenu = new Scene(root);
        mainMenu.setCursor(cursor);
        Main.primaryStage.setScene(mainMenu);
    }

    public void setGameScene(GameScene scene) {
        this.scene = scene;
    }

    public void isGameCompleted(boolean gameCompleted) {
        if (gameCompleted) {
            title.setText("You Won!");
            title.setTextFill(Color.GREEN);
        } else {
            title.setText("Game Over :(");
            title.setTextFill(Color.RED);
        }
    }
}
