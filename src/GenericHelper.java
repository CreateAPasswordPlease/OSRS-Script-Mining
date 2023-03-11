import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.input.Camera;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.*;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.*;
import org.dreambot.api.wrappers.interactive.util.Direction;

import java.awt.*;
import java.util.Random;


public class GenericHelper {
    private int mouseSpeed;
    private int howManyLevels;
    private int runThreshold;
    private Random rand = new Random();

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
            Camera.keyboardRotateToTile(new Tile(player.getX(),player.getY()+2,player.getZ()));
            Sleep.sleepUntil(()->player.getFacingDirection().equals(Direction.NORTH),700,1200);
        }
        if(player.getFacingDirection().equals(Direction.SOUTH)){
            //Handle turning camera forward same direction as currently facing SOUTH
            Logger.log("Turning camera to direction player is facing: "+player.getFacingDirection());
            Camera.keyboardRotateToTile(new Tile(player.getX(),player.getY()-2,player.getZ()));
            Sleep.sleepUntil(()->player.getFacingDirection().equals(Direction.SOUTH),700,1200);
        }
        if(player.getFacingDirection().equals(Direction.EAST)){
            //Handle turning camera forward same direction as currently facing EAST
            Logger.log("Turning camera to direction player is facing: "+player.getFacingDirection());
            Camera.keyboardRotateToTile(new Tile(player.getX()+2,player.getY(),player.getZ()));
            Sleep.sleepUntil(()->player.getFacingDirection().equals(Direction.EAST),700,1200);
        }
        if(player.getFacingDirection().equals(Direction.WEST)){
            //Handle turning camera forward same direction as currently facing WEST
            Logger.log("Turning camera to direction player is facing: "+player.getFacingDirection());
            Camera.keyboardRotateToTile(new Tile(player.getX()-2,player.getY(),player.getZ()));
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
            if(rand.nextInt(100)>75){
                Camera.keyboardRotateToEntity(entity);
            }else{
                Camera.rotateToEntity(entity);
            }

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
        if(rand.nextInt(100)>70){
            turnCameraDirectionPlayerIsFacing(Players.getLocal());
        }
        if(randomTileFromLocation.distance()>5 && Walking.shouldWalk()){
            Walking.walk(randomTileFromLocation);
        }
        if(Walking.isRunEnabled()){
//            Sleep.sleep(1000,2500);
        }else{
//            Sleep.sleep(2500,3500);
        }
    }
    public void walkToExactTile(Tile tile, int AreaRadius){
        Tile randomTileFromAreaOfTile = tile.getArea(AreaRadius).getRandomTile();
        if(randomTileFromAreaOfTile.distance()>5 && Walking.shouldWalk()){
            if(rand.nextInt(100)>90){
                Logger.log("Turning forward....");
                turnCameraDirectionPlayerIsFacing(Players.getLocal());
            }
            if(Walking.canWalk(randomTileFromAreaOfTile)){
                Logger.log("Tile Selected: "+randomTileFromAreaOfTile);
                Walking.walk(randomTileFromAreaOfTile);
                if(rand.nextInt(100)>50){
                    //Perform a random mouse movement
                }
            }else{
                Logger.log("Can't reach tile: "+randomTileFromAreaOfTile);
            }
        }
        if(Walking.isRunEnabled()){
//            Sleep.sleep(300,900);
        }else{
//            Sleep.sleep(300,1300);
        }
    }
    public boolean accountIsNew(){
        //Logic to check if account is brand new
        if(Inventory.contains("Bronze axe")
                && Inventory.contains("Bronze pickaxe")
                && Inventory.contains("Tinderbox")
                && Inventory.contains("Small fishing net") && Inventory.contains("Shrimps")
                && Inventory.contains("Bronze dagger")
                && Inventory.contains("Bread"))
        {
            return true;
        }
        return false;
    }

    //Random acts
    public void antiRandomRandomlyCheckNearbyGroundItems(){
        //Check a ground item nearby within 1 - 10 distance
        //right click examine rarely
        //right click, move mouse away, right click again, move mouse away
        //Pick item up and then drop it
        //Pick item up hover for 1 - 5 seconds then drop it with right click or shift click
    }
    public void antiRandomCheckNpcInCombatWith(){
        //If player is in combat with the NPC provided perform a random action of checking it out
        //Examining it
        //Right-clicking it once and moving mouse away
        //Right-clicking it twice
        //Clicking it again
        //Hovering it for 1-3 seconds then moving mouse away
    }
    public void antiRandomCheckingNearbyPlayers(){
        //Logic to right click nearby players
    }
    public void customClick(){
        //Customized click with random clicks
    }
    public void skillCheck(Skill skillToCheck){
        if(!Tabs.isOpen(Tab.SKILLS)){
            Tabs.open(Tab.SKILLS);
            Sleep.sleep(700,1400);
        }else{
            Skills.hoverSkill(skillToCheck);
            if(Players.getLocal().isInCombat() || Players.getLocal().isAnimating()){
                Sleep.sleep(1700,3300);
            }else{
                Sleep.sleep(500,1000);
            }
        }
        //In the end switch back to the inventory
        Tabs.open(Tab.INVENTORY);
    }
    public void performRandomMouseMovement(boolean trueForBigMovement){
        int getCurrentMouseX = Mouse.getX();
        int getCurrentMouseY = Mouse.getY();
        if(trueForBigMovement){
            Logger.log("Performed random act large mouse movement");

            int randomX1 = rand.nextInt(150);
            if(rand.nextInt(100)>49){
                randomX1 = randomX1*-1;
            }
            int randomX2 = rand.nextInt(100);
            if(rand.nextInt(100)>50){
                randomX2 = randomX2*-1;
            }
            int randomX3 = rand.nextInt(50);
            if(rand.nextInt(100)>=50){
                randomX3 = randomX3*-1;
            }

            int randomY1 = rand.nextInt(150);
            if(rand.nextInt(100)>=50){
                randomY1 = randomY1*-1;
            }

            int randomY2 = rand.nextInt(100);
            if(rand.nextInt(100)>=49){
                randomY2 = randomY2*-1;
            }
            int randomY3 = rand.nextInt(50);
            if(rand.nextInt(100)>=49){
                randomY3 = randomY3*-1;
            }

            Point p;
            if(getCurrentMouseX > -1 && getCurrentMouseY > -1){
                p = new Point(randomX1 + randomX2 + randomX3, getCurrentMouseY + randomY1 + randomY2 + randomY3);
            }else{
                p = new Point(randomX1 + randomX2 + randomX3, randomY1 + randomY2 + randomY3);
            }
            Mouse.move(p);
        }else{
            Logger.log("Performed random act small mouse movement");

            Mouse.move();
        }
    }

}
