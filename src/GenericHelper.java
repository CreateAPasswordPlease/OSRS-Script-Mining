import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.input.Camera;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.*;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.*;
import org.dreambot.api.wrappers.interactive.util.Direction;


public class GenericHelper {
    private int mouseSpeed;
    private int howManyLevels;
    private int runThreshold;

    public GenericHelper(){
        this.mouseSpeed = 80;
        this.howManyLevels = 5;
        this.runThreshold = 15;
    }

    public void setMouseSpeed(int mouseSpeed) {this.mouseSpeed = mouseSpeed;}
    public int getMouseSpeed(){return this.mouseSpeed;}
    public void setHowManyLevels(int howManyLevels){this.howManyLevels = howManyLevels;}
    public int getHowManyLevels(){return this.howManyLevels;}


    public boolean cameraCheck(int x, int y, int z, int pitch,int zoom){
        if(Camera.getPitch()<pitch){
            Logger.log("Failed camera check CURRENT PITCH: "+Camera.getPitch());
            return false;
        }
        if(Camera.getZoom()>zoom){
            Logger.log("Failed camera check CURRENT ZOOM: "+Camera.getZoom());
            return false;
        }
        return true;
    }
    public void autoSetCamera(){
        Logger.log("Setting Zoom and Pitch");
        Camera.setZoom(789);
        Camera.mouseRotateToPitch(269);
    }
    public void clickOnVisibleTile(Tile t){
        if(Players.getLocal().distance(t)<6){
            Mouse.move(t);
            Mouse.click();
            Sleep.sleep(900,2000);
            if(!Players.getLocal().getTile().equals(t)){
                Mouse.move();
                Mouse.click();
            }
        }

    }
    public void walkToRandomTileInArea(Area area){

        Tile randomTile = area.getRandomTile();
        Walking.setRunThreshold(this.runThreshold);

        while(!Players.getLocal().getTile().equals(randomTile)){
            if(randomTile.distance()>5 && Walking.shouldWalk()){
                Walking.walk(randomTile);
                if(!cameraCheck(0,0,0,250,355)){
                    autoSetCamera();
                }

//                if(Walking.canWalk(randomTile)){
//                    Camera.rotateToTile(randomTile);
//                    Mouse.move(randomTile);
//                    Mouse.click();
//                }
            }
        }
    }
    public void turnCameraDirectionPlayerIsFacing(Player player){

        if(player.getFacingDirection().equals(Direction.NORTH)){
            //Handle turning camera forward same direction as currently facing NORTH
            Logger.log("Turning camera to direction player is facing: "+player.getFacingDirection());
            Camera.rotateToTile(new Tile(player.getX(),player.getY()+2,player.getZ()));
            Sleep.sleepUntil(()->player.getFacingDirection().equals(Direction.NORTH),700,1200);
        }
        if(player.getFacingDirection().equals(Direction.SOUTH)){
            //Handle turning camera forward same direction as currently facing SOUTH
            Logger.log("Turning camera to direction player is facing: "+player.getFacingDirection());
            Camera.rotateToTile(new Tile(player.getX(),player.getY()-2,player.getZ()));
            Sleep.sleepUntil(()->player.getFacingDirection().equals(Direction.SOUTH),700,1200);
        }
        if(player.getFacingDirection().equals(Direction.EAST)){
            //Handle turning camera forward same direction as currently facing EAST
            Logger.log("Turning camera to direction player is facing: "+player.getFacingDirection());
            Camera.rotateToTile(new Tile(player.getX()+2,player.getY(),player.getZ()));
            Sleep.sleepUntil(()->player.getFacingDirection().equals(Direction.EAST),700,1200);
        }
        if(player.getFacingDirection().equals(Direction.WEST)){
            //Handle turning camera forward same direction as currently facing WEST
            Logger.log("Turning camera to direction player is facing: "+player.getFacingDirection());
            Camera.rotateToTile(new Tile(player.getX()-2,player.getY(),player.getZ()));
            Sleep.sleepUntil(()->player.getFacingDirection().equals(Direction.WEST),700,1200);
        }
    }
    public NPC searchForNearestNPCWithName(String name){
        return NPCs.closest(new Filter<NPC>() {
            @Override
            public boolean match(NPC npc) {
                return npc != null && npc.getName().equalsIgnoreCase(name) && npc.getActions().length > 0;
            }
        });
    }
    public GameObject searchForNearestGameObjectWithName(String name, Tile tileObjectIsLocatedOn){
        return GameObjects.closest(new Filter<GameObject>() {
            @Override
            public boolean match(GameObject gameObject1) {
                return gameObject1 != null && gameObject1.getName().equals(name) && gameObject1.getActions().length > 0 && gameObject1.getTile().equals(tileObjectIsLocatedOn);
            }
        });
    }
    public void turnToEntity(Entity entity){
        if(!entity.isOnScreen()){ //If Entity is not on the Screen turn the camera to face it
            Logger.log("Turning camera to face: "+entity.getName()+" Located at position: "+entity.getCenterPoint());
            Camera.rotateToEntity(entity);
        }
    }

    public void walkToClosestBank(){
        Logger.log("Walking to closest Bank: "+Bank.getClosestBankLocation());
        Tile bankTile = Bank.getClosestBankLocation().getArea(20).getRandomTile();
        if(bankTile.distance()>5 && Walking.shouldWalk()){
            Walking.walk(bankTile);
        }
        if(Walking.isRunEnabled()){
            Sleep.sleep(1000,2000);
        }else{
            Sleep.sleep(2500,3600);
        }
    }
    public void walkToLocation(Area location){
        Tile randomTileFromLocation = location.getCenter();
//        turnCameraDirectionPlayerIsFacing(Players.getLocal());
        if(randomTileFromLocation.distance()>5 && Walking.shouldWalk()){
            Walking.walk(randomTileFromLocation);
        }
        if(Walking.isRunEnabled()){
//            Sleep.sleep(1000,2500);
        }else{
//            Sleep.sleep(2500,3500);
        }
    }
}
