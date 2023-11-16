module md.tower.defense.tdgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.xml;


    opens md.tower.defense.tdgame to javafx.fxml;
    exports md.tower.defense.tdgame;
    exports md.tower.defense.tdgame.Objects;
    opens md.tower.defense.tdgame.Objects to javafx.fxml;
    exports md.tower.defense.tdgame.Managers;
    opens md.tower.defense.tdgame.Managers to javafx.fxml;
    exports md.tower.defense.tdgame.Controllers;
    opens md.tower.defense.tdgame.Controllers to javafx.fxml;
    exports md.tower.defense.tdgame.Helpers;
    opens md.tower.defense.tdgame.Helpers to javafx.fxml;
}