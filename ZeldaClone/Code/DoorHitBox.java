package adventuregame.pkg1;

public class DoorHitBox extends HitBox {

    public int areaNumber, correspondingDoor,height, width;
    public String side;

    public DoorHitBox(boolean isPassable, String name, int damage, int areaNumber, int correspondingDoor, String side) {
        super(isPassable, name, damage);
        this.areaNumber = areaNumber;
        this.correspondingDoor = correspondingDoor;
        this.side = side;
    }
    
    public void updatePosition(int width, int height){
        this.height = height;
        this.width = width;
    }
}
