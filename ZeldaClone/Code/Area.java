package adventuregame.pkg1;

import static adventuregame.pkg1.GameDriver.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Area {

    public String name;
    public int tileWidth, tileHeight;
    public double maxWidth, maxHeight, minWidth, minHeight;
    public ArrayList<ImageView> background;
    public Pane backgroundPane, entityPane;
    public ArrayList<HitBox> hitBoxList;

    public Area(String name) {
        this.name = name;
        try {
            Scanner input = new Scanner(new File(this.name + ".txt"));
            tileWidth = input.nextInt();
            maxWidth = tileWidth * PIXEL_PER_TILE;
            tileHeight = input.nextInt();
            maxHeight = tileHeight * PIXEL_PER_TILE;
            background = new ArrayList();
            backgroundPane = new Pane();
            entityPane = new Pane();
            hitBoxList = new ArrayList();
            LoadGame.loadBackground(tileWidth, tileHeight, background, hitBoxList, backgroundPane, input);
        } catch (Exception e) {
            System.err.println("Caught Error: " + e.getMessage());
            System.out.println(LoadGame.turns);
            e.printStackTrace();
            System.exit(1);
        }
    }
}
