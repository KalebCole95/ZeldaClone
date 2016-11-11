package adventuregame.pkg1;

import static adventuregame.pkg1.GameDriver.*;

import java.util.Iterator;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class HitBox {

    public static boolean hitBoxVisibility = false;
    public static int numAttempts = 0, lastDoor;

    public boolean isPassable;
    public String name;
    public int damage;
    public Rectangle shape;

    public HitBox(boolean isPassable, String name, int damage) {
        this.isPassable = isPassable;
        this.name = name;
        this.damage = damage;
    }

    public static void setHitBoxVisibility(boolean change) {
        if (change) {
            hitBoxVisibility = !hitBoxVisibility;
        }
        for (int i = 0; i < areaSet.get(currentArea).hitBoxList.size(); i++) {
            areaSet.get(currentArea).hitBoxList.get(i).shape.setVisible(hitBoxVisibility);
        }
        player.hitBox.setVisible(hitBoxVisibility);
    }

    public void createBackgroundHitBox(int X, int Y) {
        this.shape = new Rectangle(X * PIXEL_PER_TILE + 1, Y * PIXEL_PER_TILE + 1, PIXEL_PER_TILE - 2, PIXEL_PER_TILE - 2);
        this.shape.setFill(Color.TRANSPARENT);
        this.shape.setStroke(Color.RED);
        this.shape.strokeWidthProperty().set(2);
        this.shape.setVisible(hitBoxVisibility);
    }

    public void createSpecialRectangle(double X, double Y, double width, double height) {
        this.shape = new Rectangle(X + 1, Y + 1, width - 2, height - 2);
        this.shape.setFill(Color.TRANSPARENT);
        this.shape.setStroke(Color.CYAN);
        this.shape.strokeWidthProperty().set(2);
        this.shape.setVisible(hitBoxVisibility);
    }

    public static Rectangle createEntityHitBoxRectangle(double X, double Y, double width, double height) {
        Rectangle temp = new Rectangle(X + 1, Y + 1, width - 2, height - 2);
        temp.setFill(Color.TRANSPARENT);
        temp.setStroke(Color.CYAN);
        temp.strokeWidthProperty().set(2);
        temp.setVisible(hitBoxVisibility);
        return temp;
    }

    public static boolean collisionResolver(Shape currentShape){
        for (Iterator<HitBox> IT = areaSet.get(currentArea).hitBoxList.iterator(); IT.hasNext();) {
            HitBox currentHitBox = IT.next();
            if (currentHitBox.shape != currentShape) {
                Shape intersection = Shape.intersect(currentShape, currentHitBox.shape);
                if (intersection.getBoundsInLocal().getWidth() > 0 || intersection.getBoundsInLocal().getHeight() > 0) {
                    boolean isHorizontal;
                    if (intersection.getBoundsInLocal().getWidth() > intersection.getBoundsInLocal().getHeight()) {
                        isHorizontal = true;
                    } else {
                        isHorizontal = false;
                    }
                    player.updateHealth(currentHitBox.damage);
                    switch (currentHitBox.name) {
                        case "door":
                            if (numAttempts - 1 != lastDoor) {
                                paneHandler.doorFade.play();
                                paneHandler.doorFade.jumpTo(Duration.millis(250));
                                double temp;
                                int doorValue = ((DoorHitBox) currentHitBox).correspondingDoor;
                                if (isHorizontal) {
                                    temp = player.hitBox.getTranslateX() - ((DoorHitBox) currentHitBox).width * PIXEL_PER_TILE;
                                } else {
                                    temp = player.hitBox.getTranslateY() - ((DoorHitBox) currentHitBox).height * PIXEL_PER_TILE;
                                }
                                currentArea = ((DoorHitBox) currentHitBox).areaNumber;
                                paneHandler.changePane(areaSet.get(currentArea));
                                for (int i = 0; i < areaSet.get(currentArea).hitBoxList.size(); i++) {
                                    if (areaSet.get(currentArea).hitBoxList.get(i) instanceof DoorHitBox && ((DoorHitBox) areaSet.get(currentArea).hitBoxList.get(i)).correspondingDoor == doorValue) {
                                        if (isHorizontal) {
                                            player.playerPlacer(((DoorHitBox) areaSet.get(currentArea).hitBoxList.get(i)).width * PIXEL_PER_TILE + temp, ((DoorHitBox) areaSet.get(currentArea).hitBoxList.get(i)).height * PIXEL_PER_TILE);
                                        } else {
                                            player.playerPlacer(((DoorHitBox) areaSet.get(currentArea).hitBoxList.get(i)).width * PIXEL_PER_TILE, ((DoorHitBox) areaSet.get(currentArea).hitBoxList.get(i)).height * PIXEL_PER_TILE + temp);
                                        }
                                        lastDoor = numAttempts;
                                        numAttempts++;
                                        return false;
                                    }
                                }
                            }
                            lastDoor = numAttempts;
                            numAttempts++;
                            return false;
                        case "wall":
                            if (!currentHitBox.isPassable) {
                                return true;
                            }
                        case "pickup":
                            break;
                    }
                }
            }
        }
        numAttempts++;
        return false;
    }
}
