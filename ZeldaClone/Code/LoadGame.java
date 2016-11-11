package adventuregame.pkg1;

import static adventuregame.pkg1.GameDriver.*;
import java.io.File;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class LoadGame {

    public static final String[] imageName = {"Grass_1", "Flower_1", "Rock_1", "Spikes_1", "Wall_1", "Wall_L", "Wall_R", "Wall_C_1", "Wall_C_2", "Wall_2", "Wall_3"};
    private static final Pattern VALID_PATTERN = Pattern.compile("[0-9]+|[A-Z]+");
    public static Image[] imageArray;
    public static int turns = 0;
    public static ArrayList<Image> backgroundImageList = new ArrayList(), spriteImageList = new ArrayList(), playerSpriteImageList = new ArrayList();

    public static void loadBackgroundAssets() {
        imageArray = new Image[imageName.length];
        for (int i = 0; i < imageArray.length; i++) {
            imageArray[i] = new Image("file:BackgroundImages/" + imageName[i] + ".png", PIXEL_PER_TILE, PIXEL_PER_TILE, true, false);
        }
    }

    public static void loadAssets(String folderLocation) {
        try {
            Scanner input = new Scanner(new File(folderLocation + "Assets.txt"));
            String category = "", temp = "";
            while (input.hasNext()) {
                temp = input.next();
                if (temp.equals("Background")) {
                    category = "Background";
                } else if (temp.equals("Sprite")) {
                    category = "Sprite";
                } else {
                    switch (category) {
                        case "Background":
                            backgroundImageList.add(new Image("file:" + folderLocation + "BackgroundImages/" + temp + ".png", PIXEL_PER_TILE, PIXEL_PER_TILE, true, false));
                            break;
                        case "Sprite":
                            spriteImageList.add(new Image("file:" + folderLocation + "SpriteImages/" + temp + ".png", PIXEL_PER_TILE, PIXEL_PER_TILE, true, false));
                            break;
                        case "PlayerSprite":
                            playerSpriteImageList.add(new Image("file:" + folderLocation + "PlayerSpriteImages/" + temp + ".png", PIXEL_PER_TILE, PIXEL_PER_TILE, true, false));
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Caught Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void loadBackground(int maxWidth, int maxHeight, ArrayList<ImageView> background, ArrayList<HitBox> hitBoxList, Pane backgroundPane, Scanner input) {
        for (int height = 0; height < maxHeight; height++) {
            for (int width = 0; width < maxWidth; width++) {
                ArrayList<String> temp = parse(input.next().toUpperCase());
                background.add(new ImageView(backgroundImageList.get(Integer.parseInt(temp.get(0)))));
                background.get(background.size() - 1).relocate(width * PIXEL_PER_TILE, height * PIXEL_PER_TILE);
                backgroundPane.getChildren().add(background.get(background.size() - 1));
                if (temp.size() > 1) {
                    switch (Integer.parseInt(temp.get(1))) {
                        case 1:
                            background.add(new ImageView(backgroundImageList.get(Integer.parseInt(temp.get(2)))));
                            background.get(background.size() - 1).relocate(width * PIXEL_PER_TILE, height * PIXEL_PER_TILE);
                            backgroundPane.getChildren().add(background.get(background.size() - 1));
                        //no Break ! ! !
                        case 2:
                            hitBoxList.add(new HitBox(false, "wall", 0));
                            hitBoxList.get(hitBoxList.size() - 1).createBackgroundHitBox(width, height);
                            break;
                        case 3:
                            hitBoxList.add(new HitBox(true, "wall", 1));
                            hitBoxList.get(hitBoxList.size() - 1).createBackgroundHitBox(width, height);
                            break;
                        case 4:
                            ImageView one = new ImageView(new Image("file:Heart_Pickup.png", 64, 64, true, false));
                            one.relocate(width * PIXEL_PER_TILE, height * PIXEL_PER_TILE);
                            backgroundPane.getChildren().add(one);
                            hitBoxList.add(new HitBox(true, "pickup", -2));
                            hitBoxList.get(hitBoxList.size() - 1).createBackgroundHitBox(width, height);
                            break;
                        case 5:
                            int offset = 2;
                            hitBoxList.add(new DoorHitBox(true, "door", 0, Integer.parseInt(temp.get(2)), Integer.parseInt(temp.get(3)), temp.get(4)));
                            switch (temp.get(4)) {
                                case "U":
                                    hitBoxList.get(hitBoxList.size() - 1).createSpecialRectangle(width * PIXEL_PER_TILE, height * PIXEL_PER_TILE + player.HB_OFFSET_Y, Integer.parseInt(temp.get(5)) * PIXEL_PER_TILE, offset);
                                    break;
                                case "D":
                                    hitBoxList.get(hitBoxList.size() - 1).createSpecialRectangle(width * PIXEL_PER_TILE, ((height + 1) * PIXEL_PER_TILE) - offset, Integer.parseInt(temp.get(5)) * PIXEL_PER_TILE, offset);
                                    break;
                                case "L":
                                    hitBoxList.get(hitBoxList.size() - 1).createSpecialRectangle(width * PIXEL_PER_TILE, height * PIXEL_PER_TILE, offset, Integer.parseInt(temp.get(5)) * PIXEL_PER_TILE);
                                    break;
                                case "R":
                                    hitBoxList.get(hitBoxList.size() - 1).createSpecialRectangle((width + 1) * PIXEL_PER_TILE - offset, height * PIXEL_PER_TILE, offset, Integer.parseInt(temp.get(5)) * PIXEL_PER_TILE);
                                    break;
                                default:
                                    System.out.println("FAILURE: DOOR CREATION");
                                    break;
                            }
                            ((DoorHitBox) hitBoxList.get(hitBoxList.size() - 1)).updatePosition(width, height);
                            break;
                    }
                }
                turns++;
            }
        }
    }

    private static ArrayList<String> parse(String toParse) {
        ArrayList<String> chunks = new ArrayList<String>();
        Matcher matcher = VALID_PATTERN.matcher(toParse);
        while (matcher.find()) {
            chunks.add(matcher.group());
        }
        return chunks;
    }
    
    
    public static void HitBoxResolver(){
        //DO THIS!!!
        //Fix hitboxes to be more helpful
        //temporary all hitboxes then reduce to minimal and store
        //sort hitboxes wall,door,traps
    }
}
