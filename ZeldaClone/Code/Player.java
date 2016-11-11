package adventuregame.pkg1;

import static adventuregame.pkg1.GameDriver.*;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;

public class Player {

    int currentSprite = 0;
    double spriteTimer = 0;
    final double SPRITE_SPEED = .15;
    final double SPEED = 200;
    final double HB_OFFSET_X = 10, HB_OFFSET_Y = 22;

    public long lastHurt;
    public int num = 0;
    public ImageView[] spriteArray = new ImageView[8];
    public ArrayList<ImageView> hearts = new ArrayList();
    public ArrayList<ImageView> maxHearts = new ArrayList();
    public String[] spriteNames = {"Front_View_1.png", "Front_View_2.png", "Back_View_1.png", "Back_View_2.png", "Right_View_1.png", "Right_View_2.png", "Left_View_1.png", "Left_View_2.png"};
    private String primaryDirection, lastDirection;
    private boolean up, down, right, left, moveBackground = true;
    private double vertVelocity, horizVelocity, lastUpdate, currentX, currentY, nextX, nextY;
    public Rectangle hitBox;
    public int health = 10, maxHealth = 10;

    public Player() {
        loadSprites();
        hitBox = HitBox.createEntityHitBoxRectangle(HB_OFFSET_X, HB_OFFSET_Y, PIXEL_PER_TILE - HB_OFFSET_X * 2, PIXEL_PER_TILE - HB_OFFSET_Y);
        primaryDirection = "down";
        up = false;
        down = false;
        right = false;
        left = false;
        horizVelocity = 0;
        vertVelocity = 0;
        lastUpdate = 0;
        setAllInvisible();
        spriteArray[0].setVisible(true);
        lastHurt = 0;
    }

    public void playerUpdater() {
        setVelocity();
        lastDirection = primaryDirection;
        primaryDirection = setPrimaryDirection(primaryDirection);
        if (lastUpdate > 0) {
            movement();
        }
        lastUpdate = timestamp;
    }

    public void playerEvents(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.W) {
                up = true;
            } else if (event.getCode() == KeyCode.S) {
                down = true;
            } else if (event.getCode() == KeyCode.A) {
                left = true;
            } else if (event.getCode() == KeyCode.D) {
                right = true;
            } else if (event.getCode() == KeyCode.I) {
                HitBox.setHitBoxVisibility(true);
            }
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.W) {
                up = false;
            } else if (event.getCode() == KeyCode.S) {
                down = false;
            } else if (event.getCode() == KeyCode.A) {
                left = false;
            } else if (event.getCode() == KeyCode.D) {
                right = false;
            }
        });
    }

    public void loadSprites() {
        for (int i = 0; i < spriteArray.length; i++) {
            spriteArray[i] = new ImageView(new Image("file:" + GameDriver.FOLDER + "PlayerSprites/" + spriteNames[i], PIXEL_PER_TILE, PIXEL_PER_TILE, true, false));
        }
        for (int i = 0; i < health; i++) {
            if (i % 2 == 0) {
                hearts.add(new ImageView(new Image("file:" + GameDriver.FOLDER + "PlayerSprites/Heart_Half.png", 32, 32, true, false)));
            } else {
                hearts.add(new ImageView(new Image("file:" + GameDriver.FOLDER + "PlayerSprites/Heart_Full.png", 32, 32, true, false)));
                maxHearts.add(new ImageView(new Image("file:" + GameDriver.FOLDER + "PlayerSprites/Heart_Empty.png", 32, 32, true, false)));
            }
            hearts.get(i).relocate(WINDOW_WIDTH + (i / 2 * 34) - (maxHealth / 2 * 34), 5);
        }
        for (int i = 0; i < maxHearts.size(); i++) {
            maxHearts.get(i).relocate(WINDOW_WIDTH + (i * 34) - (maxHealth / 2 * 34), 5);
        }
    }

    public void setVelocity() {
        if (right && left) {
            horizVelocity = 0;
        } else if (right) {
            horizVelocity = SPEED;
        } else if (left) {
            horizVelocity = -SPEED;
        } else {
            horizVelocity = 0;
        }
        if (up && down) {
            vertVelocity = 0;
        } else if (up) {
            vertVelocity = -SPEED;
        } else if (down) {
            vertVelocity = SPEED;
        } else {
            vertVelocity = 0;
        }
    }

    public String setPrimaryDirection(String currentDirection) {
        if (up && !down && ((!left && !right) || (left && right))) {
            return "up";
        }
        if (down && !up && ((!left && !right) || (left && right))) {
            return "down";
        }
        if (left && !right && ((!up && !down) || (up && down))) {
            return "left";
        }
        if (right && !left && ((!up && !down) || (up && down))) {
            return "right";
        }
        if ((up && !down && left && !right) && (!currentDirection.equals("up") && !currentDirection.equals("left"))) {
            return "up";
        }
        if ((down && !up && left && !right) && (!currentDirection.equals("down") && !currentDirection.equals("left"))) {
            return "left";
        }
        if ((up && !down && !left && right) && (!currentDirection.equals("up") && !currentDirection.equals("right"))) {
            return "right";
        }
        if ((down && !up && !left && right) && (!currentDirection.equals("down") && !currentDirection.equals("right"))) {
            return "down";
        }
        return currentDirection;
    }

    public void movement() {
        animation();
        final double elapsedSeconds = (timestamp - lastUpdate) / 1_000_000_000.0;
        final double deltaX = elapsedSeconds * horizVelocity;
        final double deltaY = elapsedSeconds * vertVelocity;
        final double oldPaneX = paneHandler.hitBoxPane.getTranslateX();
        double newPaneX = oldPaneX - deltaX;
        final double oldPaneY = paneHandler.hitBoxPane.getTranslateY();
        double newPaneY = oldPaneY - deltaY;
        final double oldHitBoxX = hitBox.getTranslateX();
        double newHitBoxX = oldHitBoxX + deltaX;
        final double oldHitBoxY = hitBox.getTranslateY();
        double newHitBoxY = oldHitBoxY + deltaY;
        final double oldSpriteX = spriteArray[0].getTranslateX();
        double newSpriteX = oldSpriteX + deltaX;
        final double oldSpriteY = spriteArray[0].getTranslateY();
        double newSpriteY = oldSpriteY + deltaY;

        if (newHitBoxX <= 0) {
            newHitBoxX = 0;
            newSpriteX = 0;
        } else if (newHitBoxX >= areaSet.get(currentArea).maxWidth) {
            newHitBoxX = areaSet.get(currentArea).maxWidth;
            newSpriteX = WINDOW_WIDTH;
        }
        if (newHitBoxX <= (WINDOW_WIDTH - PIXEL_PER_TILE) / 2) {
            newPaneX = 0;
            newSpriteX = newHitBoxX;
        } else if (newHitBoxX >= areaSet.get(currentArea).maxWidth - (WINDOW_WIDTH + PIXEL_PER_TILE) / 2) {
            newPaneX = -(areaSet.get(currentArea).maxWidth - WINDOW_WIDTH);
            newSpriteX = newHitBoxX - (areaSet.get(currentArea).maxWidth - WINDOW_WIDTH);
        } else {
            newSpriteX = oldSpriteX;
        }
        hitBox.setTranslateX(newHitBoxX);
        if (HitBox.collisionResolver(hitBox)) {
            if (newPaneX != oldPaneX) {
                paneHandler.hitBoxPane.setTranslateX(oldPaneX);
                areaSet.get(currentArea).backgroundPane.setTranslateX(oldPaneX);
            }
            hitBox.setTranslateX(oldHitBoxX);
        } else {
            if (newPaneX != oldPaneX) {
                paneHandler.hitBoxPane.setTranslateX(newPaneX);
                areaSet.get(currentArea).backgroundPane.setTranslateX(newPaneX);
            }
            moveAll("x", newSpriteX);
        }

        if (newHitBoxY <= 0) {
            newHitBoxY = 0;
            newSpriteY = 0;
        } else if (newHitBoxY >= areaSet.get(currentArea).maxHeight) {
            newHitBoxY = areaSet.get(currentArea).maxHeight;
            newSpriteY = WINDOW_HEIGHT;
        }
        if (newHitBoxY <= (WINDOW_HEIGHT - PIXEL_PER_TILE) / 2) {
            newPaneY = 0;
            newSpriteY = newHitBoxY;
        } else if (newHitBoxY >= areaSet.get(currentArea).maxHeight - (WINDOW_HEIGHT + PIXEL_PER_TILE) / 2) {
            newPaneY = -(areaSet.get(currentArea).maxHeight - WINDOW_HEIGHT);
            newSpriteY = newHitBoxY - (areaSet.get(currentArea).maxHeight - WINDOW_HEIGHT);
        } else {
            newSpriteY = oldSpriteY;
        }
        hitBox.setTranslateY(newHitBoxY);
        if (HitBox.collisionResolver(hitBox)) {
            if (newPaneY != oldPaneY) {
                paneHandler.hitBoxPane.setTranslateY(oldPaneY);
                areaSet.get(currentArea).backgroundPane.setTranslateY(oldPaneY);
            }
            hitBox.setTranslateY(oldHitBoxY);
        } else {
            if (newPaneY != oldPaneY) {
                paneHandler.hitBoxPane.setTranslateY(newPaneY);
                areaSet.get(currentArea).backgroundPane.setTranslateY(newPaneY);
            }

            moveAll("y", newSpriteY);
        }
        
    }

    public void animation() {
        if (primaryDirection.equals("up")) {
            if (!lastDirection.equals("up")) {
                setAllInvisible();
                spriteArray[2].setVisible(true);
                spriteTimer = SPRITE_SPEED;
            }
            if (up && !down) {
                spriteTimer = spriteTimer + ((timestamp - lastUpdate) / 1_000_000_000.0);
                if (spriteTimer > SPRITE_SPEED) {
                    if (currentSprite == 2) {
                        setAllInvisible();
                        currentSprite = 3;
                        spriteArray[3].setVisible(true);
                    } else {
                        setAllInvisible();
                        currentSprite = 2;
                        spriteArray[2].setVisible(true);
                    }
                    spriteTimer = 0;
                }
            }
        }
        if (primaryDirection.equals("down")) {
            if (!lastDirection.equals("down")) {
                setAllInvisible();
                spriteArray[0].setVisible(true);
                spriteTimer = SPRITE_SPEED;
            }
            if (down && !up) {
                spriteTimer = spriteTimer + ((timestamp - lastUpdate) / 1_000_000_000.0);
                if (spriteTimer > SPRITE_SPEED) {
                    if (currentSprite == 0) {
                        setAllInvisible();
                        currentSprite = 1;
                        spriteArray[1].setVisible(true);
                    } else {
                        setAllInvisible();
                        currentSprite = 0;
                        spriteArray[0].setVisible(true);
                    }
                    spriteTimer = 0;
                }
            }
        }
        if (primaryDirection.equals("right")) {
            if (!lastDirection.equals("right")) {
                setAllInvisible();
                spriteArray[4].setVisible(true);
                spriteTimer = SPRITE_SPEED;
            }
            if (right && !left) {
                spriteTimer = spriteTimer + ((timestamp - lastUpdate) / 1_000_000_000.0);
                if (spriteTimer > SPRITE_SPEED) {
                    if (currentSprite == 4) {
                        setAllInvisible();
                        currentSprite = 5;
                        spriteArray[5].setVisible(true);
                    } else {
                        setAllInvisible();
                        currentSprite = 4;
                        spriteArray[4].setVisible(true);
                    }
                    spriteTimer = 0;
                }
            } else {
                setAllInvisible();
                currentSprite = 4;
                spriteArray[4].setVisible(true);
            }
        }
        if (primaryDirection.equals("left")) {
            if (!lastDirection.equals("left")) {
                setAllInvisible();
                spriteArray[6].setVisible(true);
                spriteTimer = SPRITE_SPEED;
            }
            if (left && !right) {
                spriteTimer = spriteTimer + ((timestamp - lastUpdate) / 1_000_000_000.0);
                if (spriteTimer > SPRITE_SPEED) {
                    if (currentSprite == 6) {
                        setAllInvisible();
                        currentSprite = 7;
                        spriteArray[7].setVisible(true);
                    } else {
                        setAllInvisible();
                        currentSprite = 6;
                        spriteArray[6].setVisible(true);
                    }
                    spriteTimer = 0;
                }
            } else {
                setAllInvisible();
                currentSprite = 6;
                spriteArray[6].setVisible(true);
            }
        }
    }

    public void updateHealth(int damage) {
        if (damage == 0) {
            return;
        } else {
            if (((timestamp - lastHurt) / 1_000_000_000.0) > 1 && damage > 0) {
                lastHurt = timestamp;
                health = Math.max(health - damage, 0);
                updateHearts(health);
            } else if (damage < 0) {
                health = Math.min(health - damage, maxHealth);
                updateHearts(health);
            }
        }
    }

    public void moveAll(String direction, double distance) {
        for (int i = 0; i < spriteArray.length; i++) {
            if (direction.equals("x")) {
                spriteArray[i].setTranslateX(distance);
            }
            if (direction.equals("y")) {
                spriteArray[i].setTranslateY(distance);
            }
        }
    }

    public void updateHearts(int health) {
        for (int i = 0; i < hearts.size(); i++) {
            if (i >= health) {
                hearts.get(i).setVisible(false);
            } else {
                hearts.get(i).setVisible(true);
            }
        }
    }

    public void setAllInvisible() {
        for (int i = 0; i < spriteArray.length; i++) {
            spriteArray[i].setVisible(false);
        }
    }

    public void playerPlacer(double X, double Y) {
        if (X <= 0) {
            X = 0;
        } else if (X >= areaSet.get(currentArea).maxWidth - PIXEL_PER_TILE) {
            X = areaSet.get(currentArea).maxWidth - PIXEL_PER_TILE;
        }
        if (Y <=0) {
            Y = 0;
        } else if (Y >= areaSet.get(currentArea).maxHeight - PIXEL_PER_TILE) {
            Y = areaSet.get(currentArea).maxHeight - PIXEL_PER_TILE;
        }
        hitBox.setTranslateX(X);
        hitBox.setTranslateY(Y);
        if (X <= (WINDOW_WIDTH - PIXEL_PER_TILE) / 2) {
            paneHandler.hitBoxPane.setTranslateX(0);
            areaSet.get(currentArea).backgroundPane.setTranslateX(0);
            player.moveAll("x", X);
        } else if (X >= areaSet.get(currentArea).maxWidth - (WINDOW_WIDTH + PIXEL_PER_TILE) / 2) {
            paneHandler.hitBoxPane.setTranslateX(-(areaSet.get(currentArea).maxWidth - WINDOW_WIDTH));
            areaSet.get(currentArea).backgroundPane.setTranslateX(-(areaSet.get(currentArea).maxWidth - WINDOW_WIDTH));
            player.moveAll("x", X - (areaSet.get(currentArea).maxWidth - WINDOW_WIDTH));
        } else {
            paneHandler.hitBoxPane.setTranslateX(-(X - (WINDOW_WIDTH - PIXEL_PER_TILE) / 2));
            areaSet.get(currentArea).backgroundPane.setTranslateX(-(X - (WINDOW_WIDTH - PIXEL_PER_TILE) / 2));
            player.moveAll("x", (WINDOW_WIDTH - PIXEL_PER_TILE) / 2);
        }
        if (Y <= (WINDOW_HEIGHT - PIXEL_PER_TILE) / 2) {
            paneHandler.hitBoxPane.setTranslateY(0);
            areaSet.get(currentArea).backgroundPane.setTranslateY(0);
            player.moveAll("y", Y);
        } else if (Y >= areaSet.get(currentArea).maxHeight - (WINDOW_HEIGHT + PIXEL_PER_TILE) / 2) {
            paneHandler.hitBoxPane.setTranslateY(-(areaSet.get(currentArea).maxHeight - WINDOW_HEIGHT));
            areaSet.get(currentArea).backgroundPane.setTranslateY(-(areaSet.get(currentArea).maxHeight - WINDOW_HEIGHT));
            player.moveAll("y", Y - (areaSet.get(currentArea).maxHeight - WINDOW_HEIGHT));
        } else {
            paneHandler.hitBoxPane.setTranslateY(-(Y - (WINDOW_HEIGHT - PIXEL_PER_TILE) / 2));
            areaSet.get(currentArea).backgroundPane.setTranslateY(-(Y - (WINDOW_HEIGHT - PIXEL_PER_TILE) / 2));
            player.moveAll("y", (WINDOW_HEIGHT - PIXEL_PER_TILE) / 2);
        }
    }
}
