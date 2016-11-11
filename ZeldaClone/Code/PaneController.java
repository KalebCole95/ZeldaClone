package adventuregame.pkg1;

import static adventuregame.pkg1.GameDriver.*;
import static adventuregame.pkg1.Area.*;
import java.util.ArrayList;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class PaneController {

    Pane mainPane, hitBoxPane, hudPane;
    FadeTransition doorFade = new FadeTransition();
    Rectangle doorFadeRectangle;
    Scene scene;

    public PaneController() {
        mainPane = new Pane();
        hitBoxPane = new Pane();
        hudPane = new Pane();
        scene = new Scene(mainPane, WINDOW_WIDTH, WINDOW_HEIGHT, Color.WHITE);
        mainPane.getChildren().addAll(hitBoxPane, hudPane);

        doorFadeRectangle = new Rectangle(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        doorFade.setNode(doorFadeRectangle);
        doorFade.setDuration(new Duration(2000));
        doorFade.setFromValue(1.0);
        doorFade.setToValue(0);
        doorFade.setCycleCount(1);
        doorFade.play();
    }

    public void loadHud(Player player) {
        for (int i = 0; i < player.maxHearts.size(); i++) {
            hudPane.getChildren().add((player.maxHearts).get(i));
        }
        for (int i = 0; i < player.hearts.size(); i++) {
            hudPane.getChildren().add((player.hearts).get(i));
        }
        hudPane.getChildren().add(doorFadeRectangle);
    }

    public void changePane(Area area) {
        mainPane.getChildren().clear();
        hitBoxPane.getChildren().clear();
        for (int i = 0; i < area.hitBoxList.size(); i++) {
            hitBoxPane.getChildren().add(area.hitBoxList.get(i).shape);
        }
        hitBoxPane.getChildren().add(player.hitBox);
        for (int i = 0; i < player.spriteArray.length; i++) {
            area.entityPane.getChildren().add(player.spriteArray[i]);
        }
        HitBox.setHitBoxVisibility(false);
        mainPane.getChildren().addAll(area.backgroundPane, area.entityPane, hitBoxPane, hudPane);
    }

    public void panePlacer(double X, double Y, Area area) {

    }
}
