package adventuregame.pkg1;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import static javafx.application.Application.launch;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

public class GameDriver extends Application {

    public static String GAME_VERSION;
    public static final String FOLDER = "Assets/";

    public static final int WINDOW_WIDTH_TILE = 15, WINDOW_HEIGHT_TILE = 10, PIXEL_PER_TILE = 64;
    public static final double WINDOW_WIDTH = WINDOW_WIDTH_TILE * PIXEL_PER_TILE, WINDOW_HEIGHT = WINDOW_HEIGHT_TILE * PIXEL_PER_TILE;
    public final AnimationTimer timer;

    public static ArrayList<Area> areaSet = new ArrayList();
    public static int currentArea = 0;

    public static PaneController paneHandler;
    public static Player player;
    public static long timestamp = 0;

    public GameDriver() {
        loadGameVersion();
        LoadGame.loadAssets(FOLDER);
        paneHandler = new PaneController();
        player = new Player();
        areaSet.add(new Area(FOLDER + "BackgroundTextFiles/BackGround_1"));
        areaSet.add(new Area(FOLDER + "BackgroundTextFiles/BackGround_2"));
        this.timer = new AnimationTimer() {
            @Override
            public void handle(long time) {
                System.out.println(timestamp - time);
                timestamp = time;
                player.playerUpdater();
            }
        };
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void loadGameVersion() {
        try {
            Scanner input = new Scanner(new File(FOLDER + "VersionController" + ".txt"));
            input.next();
            GAME_VERSION = input.next() + " " + input.next();
        } catch (Exception e) {
            System.err.println("Caught Error: " + e.getMessage());
            System.out.println(LoadGame.turns);
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void start(Stage mainStage) {
        paneHandler.changePane(areaSet.get(currentArea));
        player.playerPlacer(PIXEL_PER_TILE, PIXEL_PER_TILE);

        paneHandler.loadHud(player);
        mainStage.setTitle(GAME_VERSION);
        mainStage.setScene(paneHandler.scene);
        mainStage.show();
        timer.start();
        player.playerEvents(paneHandler.scene);
    }
}
//Inacuracy in timer causes hitbox lag motion towards object!!!!
