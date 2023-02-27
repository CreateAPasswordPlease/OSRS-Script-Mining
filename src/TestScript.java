import org.dreambot.api.Client;
import org.dreambot.api.data.GameState;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.input.Mouse.*;
import org.dreambot.api.input.mouse.destination.AbstractMouseDestination;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.input.Camera;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.randoms.*;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Player;
import sun.font.Script;

import java.util.Random;

@ScriptManifest(name = "Mining Script v1.03",
        description = "A simple F2P Mining script focused on leveling",
        author = "Blank0001",
        version = 1.0, category = Category.MINING, image = "")


public class TestScript extends AbstractScript {
    public void onStart(){
        Logger.log("Welcome to Mining 101 script v1.03");
    }

    GenericHelper gh = new GenericHelper();
    Player localPlayer = Players.getLocal();
    Random rand = new Random();
    int upperbound = 100;
    int lastTinMined = 99;
    
    //final GameObject doorToCombatArea = GameObjects.closest(9718);

    private Player player(){
        return Players.getLocal();
    }

    private void turnCameraDirectionPlayerIsFacing(Player player){

        if(player.getFacingDirection().equals(Direction.NORTH)){
            //Handle turning camera forward same direction as currently facing NORTH
            Logger.log("Turning camera to direction player is facing: "+player.getFacingDirection());
            Camera.rotateToTile(new Tile(player.getX(),player.getY()+2,player.getZ()));
            Sleep.sleepUntil(()->player().getFacingDirection().equals(Direction.NORTH),700,1200);
        }
        if(player.getFacingDirection().equals(Direction.SOUTH)){
            //Handle turning camera forward same direction as currently facing SOUTH
            Logger.log("Turning camera to direction player is facing: "+player.getFacingDirection());
            Camera.rotateToTile(new Tile(player.getX(),player.getY()-2,player.getZ()));
            Sleep.sleepUntil(()->player().getFacingDirection().equals(Direction.SOUTH),700,1200);
        }
        if(player.getFacingDirection().equals(Direction.EAST)){
            //Handle turning camera forward same direction as currently facing EAST
            Logger.log("Turning camera to direction player is facing: "+player.getFacingDirection());
            Camera.rotateToTile(new Tile(player.getX()+2,player.getY(),player.getZ()));
            Sleep.sleepUntil(()->player().getFacingDirection().equals(Direction.EAST),700,1200);
        }
        if(player.getFacingDirection().equals(Direction.WEST)){
            //Handle turning camera forward same direction as currently facing WEST
            Logger.log("Turning camera to direction player is facing: "+player.getFacingDirection());
            Camera.rotateToTile(new Tile(player.getX()-2,player.getY(),player.getZ()));
            Sleep.sleepUntil(()->player().getFacingDirection().equals(Direction.WEST),700,1200);
        }
    }

    private GameObject searchForNearestGameObjectWithName(String name, Tile tileObjectIsLocatedOn){
        return GameObjects.closest(new Filter<GameObject>() {
            @Override
            public boolean match(GameObject gameObject1) {
                return gameObject1 != null && gameObject1.getName().equals(name) && gameObject1.getActions().length > 0 && gameObject1.getTile().equals(tileObjectIsLocatedOn);
            }
        });
    }

    private NPC searchForNearestNPCWithName(String name){
        return NPCs.closest(new Filter<NPC>() {
            @Override
            public boolean match(NPC npc) {
                return npc != null && npc.getName().equalsIgnoreCase(name) && npc.getActions().length > 0;
            }
        });
    }
    private void turnToEntity(Entity entity){
        if(!entity.isOnScreen()){ //If Entity is not on the Screen turn the camera to face it
            Logger.log("Turning camera to face: "+entity.getName()+" Located at position: "+entity.getCenterPoint());
            Camera.rotateToEntity(entity);
        }
    }
    private void mineOre(Tile oreLocation, Tile oreToMineLocation1,Tile oreToMineLocation2){
        if(oreLocation.distance()>4 && !localPlayer.isAnimating() && oreLocation.distance()<10){
            Logger.log( "Distance to correct tile: "+oreLocation.distance());
            if(oreLocation.distance()>5 && !localPlayer.isMoving()){Walking.clickTileOnMinimap(oreLocation);}
            gh.clickOnVisibleTile(oreLocation);
            Sleep.sleep(1000,2500);
        }else if(localPlayer.distance(oreLocation)>13){
            //Must walk back to the mining site
            Logger.log("Running back to mining site");
            gh.walkToLocation(LocationConstants.F2PMININGAREA1);
        }

        GameObject[] tinOre1 = GameObjects.getObjectsOnTile(oreToMineLocation1);
        GameObject[] tinOre2 = GameObjects.getObjectsOnTile(oreToMineLocation2);

        if(Walking.getRunEnergy()> rand.nextInt(20)+25 && !Walking.isRunEnabled()){Walking.toggleRun(); Sleep.sleep(500,1000);}

        if(!localPlayer.isAnimating() && oreLocation.distance()<5
                || tinOre1[0].getModelColors() == null && lastTinMined == 1 && oreLocation.distance()<5
                || tinOre2[0].getModelColors() == null && lastTinMined == 2 && oreLocation.distance()<5){
//            Logger.log("Local Player Animation status: "+localPlayer.isAnimating()+" Current Animation: "+ localPlayer.getAnimation());
//            Logger.log("Inventory Count: "+Inventory.getEmptySlots());
//            if(Inventory.isFull()){dropOre();}
            Logger.log("Mining Ore...");

            if(tinOre1[0].getModelColors() != null && lastTinMined != 1 || tinOre2[0].getModelColors() == null && lastTinMined == 2 && tinOre1[0].getModelColors() != null){
                Mouse.click(tinOre1[0]);
                //Add randomization in the number of times clicked
                if(rand.nextInt(upperbound)>69){
                    //Will perform at least a second click
                    if(rand.nextInt(upperbound)>73){
                        //Move mouse before clicking second time
                        Mouse.move(tinOre1[0].getTile());
                    }
                    Mouse.click(tinOre1[0]);
                    if(rand.nextInt(upperbound)>70){
                        //Clicks three times
                        if(rand.nextInt(upperbound)>73){
                            //Move mouse before clicking third time
                            Mouse.move(tinOre1[0].getTile());
                        }
                        Mouse.click(tinOre1[0]);
                    }else if(rand.nextInt(upperbound)>90){
                        //Clicks four times
                        if(rand.nextInt(upperbound)>73){
                            //Move mouse before clicking third time
                            Mouse.move(tinOre1[0].getTile());
                        }
                        Mouse.click(tinOre1[0]);
                    }
                }

                Sleep.sleep(500,1500);
                tinOre1 = GameObjects.getObjectsOnTile(oreToMineLocation1);
                if(localPlayer.getAnimation() == -1 && tinOre1[0].getModelColors() != null){
                    //Check if missclicked
                    Logger.log("Miss-clicked trying to click on Ore again");
                    Mouse.move(tinOre1[0].getTile());
                    Mouse.click(tinOre1[0]);
                    Sleep.sleep(300,600);

                    if(localPlayer.getAnimation() == -1 && tinOre1[0].getModelColors() != null){
                        //Check if missclicked
                        Logger.log("Miss-clicked trying to click on Ore again");
                        Mouse.move(tinOre1[0].getTile());
                        Mouse.click(tinOre1[0]);
                        Sleep.sleep(300,600);
                    }
                }
                if(rand.nextInt(upperbound)>37){
                    while(localPlayer.isMoving()){Sleep.sleep(300,900);}
                    Mouse.move(tinOre2[0].getTile());
                }else{
                    //Perform a random act
                }
                lastTinMined = 1;

            }else if(tinOre2[0].getModelColors() != null && lastTinMined != 2 || tinOre1[0].getModelColors() == null && lastTinMined == 1 && tinOre2[0].getModelColors() != null){
                Mouse.click(tinOre2[0]);
                //Add randomization in the number of times clicked
                if(rand.nextInt(upperbound)>69){
                    //Will perform at least a second click
                    if(rand.nextInt(upperbound)>73){
                        //Move mouse before clicking second time
                        Mouse.move(tinOre2[0].getTile());
                    }
                    Mouse.click(tinOre2[0]);
                    if(rand.nextInt(upperbound)>70){
                        //Clicks three times
                        if(rand.nextInt(upperbound)>73){
                            //Move mouse before clicking third time
                            Mouse.move(tinOre2[0].getTile());
                        }
                        Mouse.click(tinOre2[0]);
                    }else if(rand.nextInt(upperbound)>90){
                        //Clicks four times
                        if(rand.nextInt(upperbound)>73){
                            //Move mouse before clicking fourth time
                            Mouse.move(tinOre2[0].getTile());
                        }
                        Mouse.click(tinOre2[0]);
                    }
                }

                Sleep.sleep(500,1500);
                tinOre2 = GameObjects.getObjectsOnTile(oreToMineLocation2);
                if(localPlayer.getAnimation() == -1 && tinOre2[0].getModelColors() != null){
                    //Check if missclicked
                    Logger.log("Miss-clicked trying to click on Ore again");
                    Mouse.move(tinOre2[0].getTile());
                    Mouse.click(tinOre2[0]);
                    Sleep.sleep(300,600);
                    if(localPlayer.getAnimation() == -1 && tinOre2[0].getModelColors() != null){
                        //Check if missclicked
                        Logger.log("Miss-clicked trying to click on Ore again");
                        Mouse.move(tinOre2[0].getTile());
                        Mouse.click(tinOre2[0]);
                        Sleep.sleep(300,600);
                    }
                }
                if(rand.nextInt(upperbound)>34){
                    while(localPlayer.isMoving()){Sleep.sleep(300,900);}
                    Mouse.move(tinOre1[0].getTile());
                }else{
                    //Perform a random act
                }
                lastTinMined = 2;

            }
        }

    }
    private boolean dropOre(){
        if(Inventory.isFull()){
//            while(BankLocation.getNearest().getTile().distance()>5 && Walking.shouldWalk()){
//                Walking.walk(BankLocation.getNearest());
//            }
//            if(BankLocation.getNearest().getTile().distance()<8){
//                Bank.depositAllItems();
//            }
            Logger.log("Dropping Inventory");
            Inventory.dropAll();
            Sleep.sleep(500,5000);
//            gh.walkToRandomTileInArea(LocationConstants.F2PMININGAREA1);
        }
        return Inventory.isEmpty();
    }


    @Override
    public int onLoop() {

        if(Client.isLoggedIn()){

            if(!gh.cameraCheck(0,0,0,235,850)){gh.autoSetCamera();}
            if(!Inventory.isFull()){mineOre(LocationConstants.F2PMININGIRON1,new Tile(3286,3369,0),new Tile(3285,3368,0));}
            if(Inventory.isFull()){
//            dropOre();
                gh.walkToClosestBank();
                Bank.open();
                if(Bank.isOpen()){
                    Sleep.sleep(600,2000);
                    Bank.depositAllItems();
                    Sleep.sleep(800,2000);
                    Bank.close();
                }
            }
        }
//        ScriptManager.getScriptManager().isRunning() &&
//        if(new RandomSolver(RandomEvent.BREAK).isEnabled()){}
        return 350;
    }

}
