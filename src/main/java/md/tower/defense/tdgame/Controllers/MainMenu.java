package md.tower.defense.tdgame.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import md.tower.defense.tdgame.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static md.tower.defense.tdgame.Main.cursor;
import static md.tower.defense.tdgame.Main.gameScene;

public class MainMenu implements Initializable {

    MediaPlayer mediaPlayer;
    public static MediaPlayer bgPlayer;

    @FXML
    void HoverEnter(MouseEvent event) {
        Button b = (Button) event.getSource();
        hoverEntered(b);
    }

    @FXML
    void HoverExit(MouseEvent event) {
        Button b = (Button) event.getSource();
        hoverExited(b);
    }

    @FXML
    void exitApp(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void startGame(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(gameScene);
        Pane root = fxmlLoader.load();
        Scene firstLevel = new Scene(root);
        firstLevel.setCursor(cursor);
        Main.primaryStage.setScene(firstLevel);
    }

    public void hoverEntered(Button b) {
        mediaPlayer.play();
        b.setStyle("-fx-border-radius: 20; -fx-background-color: rgba(255,255,255,1); -fx-border-color: white; -fx-border-width: 2; -fx-background-radius: 20; -fx-text-fill: black;");
    }

    public void hoverExited(Button b) {
        mediaPlayer.stop();
        b.setStyle("-fx-border-radius: 10; -fx-background-color: rgba(100,0,100,0.5); -fx-border-color: white; -fx-border-width: 2; -fx-background-radius: 10; -fx-text-fill: white;");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Media click = new Media(getClass().getResource("/md/tower/defense/tdgame/audio/mainMenuHover.wav").toExternalForm());
        mediaPlayer = new MediaPlayer(click);

        Media bg = new Media(getClass().getResource("/md/tower/defense/tdgame/audio/bgm.mp3").toExternalForm());
        bgPlayer = new MediaPlayer(bg);
        bgPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgPlayer.play();
        bgPlayer.setVolume(0.2);
    }
}
