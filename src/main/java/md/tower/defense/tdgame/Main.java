package md.tower.defense.tdgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Main extends Application {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static Stage primaryStage;
    public static Cursor cursor = new ImageCursor(new Image(Main.class.getResource("/md/tower/defense/tdgame/assets/misc/cursor.png").toExternalForm()));
    public static URL mainMenu = Main.class.getResource("/md/tower/defense/tdgame/scenes/main_menu.fxml");
    public static URL gameScene = Main.class.getResource("/md/tower/defense/tdgame/scenes/game_screen.fxml");
    public static URL gameOver = Main.class.getResource("/md/tower/defense/tdgame/scenes/game_over.fxml");
    private void setup() throws IOException {
        primaryStage = new Stage();
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Tower Defense Game");

        FXMLLoader fxmlLoader = new FXMLLoader(mainMenu);
        Pane root = fxmlLoader.load();
        Scene launchScene = new Scene(root);
        launchScene.setCursor(cursor);
        primaryStage.setScene(launchScene);
    }

    @Override
    public void start(Stage stage) throws IOException {
        setup();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
