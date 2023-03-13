import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.ViewportTools;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.input.Camera;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.Player;

import java.awt.*;
import java.awt.event.PaintEvent;
import java.util.Random;

@ScriptManifest(name = "Woodcutting Script v1.2",
        description = "A simple F2P Woodcutting script focused on leveling",
        author = "Blank0001",
        version = 1.0, category = Category.MINING, image = "")

//yellowdog001@gmail.com Matthew666

public class WoodcuttingScript extends AbstractScript {
    public void onStart(){
        Logger.log("Welcome to Woodcutting 101 script v1.2");
        startTime = System.currentTimeMillis(); //save current number of system millis elapsed (long value)

    }
    @Override
    public void onPaint(Graphics2D g) {
        String experienceGainedText = "Elapsed Time: "+getElapsedTimeAsString();
        String logsCut = "Yew logs cut: "+cutLogs();
        String moneyMade = "Money made: "+moneyMadeSoFar();
        String moneyMadePerHour = "Money made hourly: "+ moneyMadePerHour();
        // Now we'll draw the text on the canvas at (5, 35). (0, 0) is the top left of the canvas.
        g.drawString(experienceGainedText, 5, 35);
        g.drawString(logsCut, 5, 55);
        g.drawString(moneyMade, 5, 75);
        g.drawString(moneyMadePerHour, 5, 95);
    }
    private Player player(){
        return Players.getLocal();
    }
    GenericHelper gh = new GenericHelper();
    Player localPlayer = Players.getLocal();
    Random rand = new Random();
    private long startTime = 0;
    private int startingExperience = Skills.getExperience(Skill.WOODCUTTING);

    private boolean failSafe(int timeUntilFailSafeInMinutes){
        final int minutes = (int)((getElapsedTime()));
        int timeUntilQuit = timeUntilFailSafeInMinutes * 60000;
        if(minutes > timeUntilQuit){
            Logger.log("Script has been on for more than "+ timeUntilFailSafeInMinutes +" minutes straight stopping....");
            return true;
        }
        return false;
    }
    private String cutLogs(){
        int numberOfYewsCut = (Skills.getExperience(Skill.WOODCUTTING) - startingExperience)/175;
        double result = Math.round(numberOfYewsCut);
        return String.valueOf(result);
    }
    private String moneyMadeSoFar(){
        return String.valueOf(Double.valueOf(cutLogs())*278);
    }
    private String moneyMadePerHour(){
        double longAsDouble = (double)(getElapsedTime()/1000);
        double money = Math.round(((Double.parseDouble(cutLogs())*278)/longAsDouble)*60)*60;
        return String.valueOf(money);
    }
    private String getElapsedTimeAsString() {
        return makeTimeString(getElapsedTime()); //make a formatted string from a long value
    }
    private long getElapsedTime() {
        return System.currentTimeMillis() - startTime; //return elapsed millis since start of script
    }
    private String makeTimeString(long ms) {
        final int seconds = (int)(ms / 1000) % 60;
        final int minutes = (int)((ms / (1000 * 60)) % 60);
        final int hours = (int)((ms / (1000 * 60 * 60)) % 24);
        final int days = (int)((ms / (1000 * 60 * 60 * 24)) % 7);
        final int weeks = (int)(ms / (1000 * 60 * 60 * 24 * 7));
        if (weeks > 0) {
            return String.format("%02dw %03dd %02dh %02dm %02ds", weeks, days, hours, minutes, seconds);
        }
        if (weeks == 0 && days > 0) {
            return String.format("%03dd %02dh %02dm %02ds", days, hours, minutes, seconds);
        }
        if (weeks == 0 && days == 0 && hours > 0) {
            return String.format("%02dh %02dm %02ds", hours, minutes, seconds);
        }
        if (weeks == 0 && days == 0 && hours == 0 && minutes > 0) {
            return String.format("%02dm %02ds", minutes, seconds);
        }
        if (weeks == 0 && days == 0 && hours == 0 && minutes == 0) {
            return String.format("%02ds", seconds);
        }
        if (weeks == 0 && days == 0 && hours == 0 && minutes == 0 && seconds == 0) {
            return String.format("%04dms", ms);
        }
        return "00";
    }
    private boolean dropLogs(){
        if(Inventory.isFull()){
            Logger.log("Dropping Inventory");
            if(getTree().equalsIgnoreCase("tree")){
                Inventory.dropAll("Logs");
                if(Inventory.contains("Logs")){
                    //Second check
                    Inventory.dropAll("Logs");
                }
            }
            if(getTree().equalsIgnoreCase("oak")){
                Inventory.dropAll("Oak logs");
                if(Inventory.contains("Oak logs")){
                    //Second check
                    Inventory.dropAll("Oak logs");
                }
            }
            if(getTree().equalsIgnoreCase("willow")){
                Inventory.dropAll("Willow logs");
                if(Inventory.contains("Willow logs")){
                    //Second check
                    Inventory.dropAll("Willow logs");
                }
            }
            if(getTree().equalsIgnoreCase("Yew")){
                ScriptManager.getScriptManager().stop();
            }
            Sleep.sleep(500,3000);
        }
        return Inventory.isEmpty();
    }
    private boolean bankLogs(){
        gh.walkToExactTile(LocationConstants.LUMBRIDGEBANK,1);
        if(LocationConstants.LUMBRIDGEBANK.distance() < 5){
            gh.turnToEntity(GameObjects.closest("Bank booth"));
            Mouse.click(GameObjects.closest("Bank booth"));

            if(!Bank.isOpen()){
                Bank.open(BankLocation.LUMBRIDGE);
                Sleep.sleep(400,900);
            }
            if(Bank.isOpen()){
                Bank.depositAll("Yew logs");
                Sleep.sleep(400,1200);
            }
            Bank.close();
            if(Bank.isOpen()){
                Bank.close();
            }
        }
        Sleep.sleep(400,900);
        return Inventory.contains("Yew logs");
    }
    private String getTree(){
        if(Skills.getRealLevel(Skill.WOODCUTTING) < 15 ){
            return "tree";
        }
        if(Skills.getRealLevel(Skill.WOODCUTTING) < 30){
            return "Oak";
        }
        if(Skills.getRealLevel(Skill.WOODCUTTING) < 60){
            return "Willow";
        }
        else{
            return "Yew";
        }
    }
    private boolean playerHasAxe(String nameOfAxe){
        return Inventory.contains(nameOfAxe) || Equipment.contains(nameOfAxe);
    }

    @Override
    public int onLoop() {


        if(failSafe(137)){
            ScriptManager.getScriptManager().stop();
        }
        if(getTree().equalsIgnoreCase("yew")){
            if(LocationConstants.LUMBRIDGEYEW.distance()> 7 && !Inventory.isFull()){
                gh.walkToExactTile(LocationConstants.LUMBRIDGEYEW.getTile(),2);
            }
            if(Inventory.isFull()){
                bankLogs();
            }
            if(!player().isAnimating() && !player().isInCombat() && GameObjects.closest(getTree()) != null && !Inventory.isFull() && LocationConstants.LUMBRIDGEYEW.distance()< 8){
                if(Dialogues.canContinue()){
                    Dialogues.continueDialogue();
                }
                try{
                    if(Camera.getZoom()>750){
                        Camera.setZoom(rand.nextInt(150)+575);
                    }
                    //MAKE CODE HERE FOR DEALING WITH THE VIEW OF THE YEW TREE
                    //Make sure its actually properly in view when trying to cut it
                    //
                    //
                    //
                    Sleep.sleep(400,2000);
                    if(GameObjects.closest(getTree()).getTile().equals(new Tile(3249,3201,0))){
                        gh.turnToEntity(GameObjects.closest(getTree()));
                        GameObjects.closest(getTree()).interact();
                    }
//                Mouse.click(GameObjects.closest(getTree()));
                    if(!Tab.INVENTORY.isOpen()){
                        Tabs.open(Tab.INVENTORY);
                    }
                    if(rand.nextInt(100)>90){
                        //Perform a random act
                        //If player is still animating send the mouse off the screen again
                        //If player isn't animating and the tree is not showing send the mouse off the screen again
                        //If the player isn't animating and the tree is showing and inventory isn't full then slap that tree
                        if(rand.nextInt(100)>49){
                            gh.performRandomMouseMovement(true);
                        }else{
                            gh.performRandomMouseMovement(false);
                        }
                    }
                }catch (Exception e){
                    Logger.log("Cannot find tree or cannot turn to it");
                    Sleep.sleep(5000,10000);
                }
                Sleep.sleep(650,3000);
            }
            if(player().isAnimating() && !Inventory.isFull()){
                if(getTree().equals("tree")){
                    Sleep.sleep(400,900);
                }else{
                    Mouse.moveOutsideScreen();
                    Sleep.sleep(20000,40000);
                    Logger.log("Going AFK for 20 - 50 seconds");
                }
            }
            if(Inventory.isFull()){
                bankLogs();
            }
        }
        if(getTree().equalsIgnoreCase("willow")){
            //Logic for handling willow
            if(LocationConstants.LUMBRIDGEWILLOW.distance()> 7 && !Inventory.isFull()){
                gh.walkToExactTile(LocationConstants.LUMBRIDGEWILLOW.getTile(),2);
            }
            if(Inventory.isFull()){
                dropLogs();
            }
            if(!player().isAnimating() && !player().isInCombat() && GameObjects.closest(getTree()) != null && !Inventory.isFull() && LocationConstants.LUMBRIDGEWILLOW.distance()< 8){
                if(Dialogues.canContinue()){
                    Dialogues.continueDialogue();
                }
                try{
                    if(Camera.getZoom()>750){
                        Camera.setZoom(rand.nextInt(150)+575);
                    }
                    //MAKE CODE HERE FOR DEALING WITH THE VIEW OF THE YEW TREE
                    //Make sure its actually properly in view when trying to cut it
                    //
                    //
                    //
                    Sleep.sleep(400,2000);
                    if(GameObjects.closest(getTree()).getTile().equals(new Tile(3233,3244,0)) || GameObjects.closest(getTree()).getTile().equals(new Tile(3235,3237,0))){
                        gh.turnToEntity(GameObjects.closest(getTree()));
                        GameObjects.closest(getTree()).interact();
                    }
//                Mouse.click(GameObjects.closest(getTree()));
                    if(!Tab.INVENTORY.isOpen()){
                        Tabs.open(Tab.INVENTORY);
                    }
                    if(rand.nextInt(100)>90){
                        //Perform a random act
                        //If player is still animating send the mouse off the screen again
                        //If player isn't animating and the tree is not showing send the mouse off the screen again
                        //If the player isn't animating and the tree is showing and inventory isn't full then slap that tree
                        if(rand.nextInt(100)>49){
                            gh.performRandomMouseMovement(true);
                        }else{
                            gh.performRandomMouseMovement(false);
                        }
                    }
                }catch (Exception e){
                    Logger.log("Cannot find tree or cannot turn to it");
                    Sleep.sleep(5000,10000);
                }
                Sleep.sleep(650,3000);
            }
            if(player().isAnimating() && !Inventory.isFull()){
                if(getTree().equals("tree")){
                    Sleep.sleep(400,900);
                }
                if(getTree().equalsIgnoreCase("willow")){
                    Mouse.moveOutsideScreen();
                    Sleep.sleep(15000,35000);
                    Logger.log("Going AFK for 15 - 35 seconds");
                }
                if(getTree().equalsIgnoreCase("oak")){
                    Mouse.moveOutsideScreen();
                    Sleep.sleep(15000,35000);
                    Logger.log("Going AFK for 15 - 35 seconds");
                }else{
                    Mouse.moveOutsideScreen();
                    Sleep.sleep(20000,40000);
                    Logger.log("Going AFK for 20 - 40 seconds");
                }
            }
            if(Inventory.isFull()){
                dropLogs();
            }
        }
        if(getTree().equalsIgnoreCase("oak")){
            //Logic for oak
            if(LocationConstants.LUMBRIDGEOAK.distance()> 7 && !Inventory.isFull()){
                gh.walkToExactTile(LocationConstants.LUMBRIDGEOAK.getTile(),3);
            }
            if(Inventory.isFull()){
                dropLogs();
            }
            if(!player().isAnimating() && !player().isInCombat() && GameObjects.closest(getTree()) != null && !Inventory.isFull() && LocationConstants.LUMBRIDGEOAK.distance()< 8){
                if(Dialogues.canContinue()){
                    Dialogues.continueDialogue();
                }
                try{
                    if(Camera.getZoom()>750){
                        Camera.setZoom(rand.nextInt(150)+575);
                    }
                    //MAKE CODE HERE FOR DEALING WITH THE VIEW OF THE YEW TREE
                    //Make sure its actually properly in view when trying to cut it
                    //
                    //
                    //
                    Sleep.sleep(400,2000);
                    if(GameObjects.closest(getTree()).getTile().equals(new Tile(3204,3239,0)) || GameObjects.closest(getTree()).getTile().equals(new Tile(3203,3246,0))){
                        gh.turnToEntity(GameObjects.closest(getTree()));
                        GameObjects.closest(getTree()).interact();
                    }
//                Mouse.click(GameObjects.closest(getTree()));
                    if(!Tab.INVENTORY.isOpen()){
                        Tabs.open(Tab.INVENTORY);
                    }
                    if(rand.nextInt(100)>90){
                        //Perform a random act
                        //If player is still animating send the mouse off the screen again
                        //If player isn't animating and the tree is not showing send the mouse off the screen again
                        //If the player isn't animating and the tree is showing and inventory isn't full then slap that tree
                        if(rand.nextInt(100)>49){
                            gh.performRandomMouseMovement(true);
                        }else{
                            gh.performRandomMouseMovement(false);
                        }
                    }
                }catch (Exception e){
                    Logger.log("Cannot find tree or cannot turn to it");
                    Sleep.sleep(5000,10000);
                }
                Sleep.sleep(650,3000);
            }
            if(player().isAnimating() && !Inventory.isFull()){
                if(getTree().equals("tree")){
                    Sleep.sleep(400,900);
                }
                if(getTree().equalsIgnoreCase("willow")){
                    Mouse.moveOutsideScreen();
                    Sleep.sleep(15000,35000);
                    Logger.log("Going AFK for 15 - 35 seconds");
                }
                if(getTree().equalsIgnoreCase("oak")){
                    Mouse.moveOutsideScreen();
                    Sleep.sleep(15000,35000);
                    Logger.log("Going AFK for 15 - 35 seconds");
                }else{
                    Mouse.moveOutsideScreen();
                    Sleep.sleep(20000,40000);
                    Logger.log("Going AFK for 20 - 40 seconds");
                }
            }
            if(Inventory.isFull()){
                dropLogs();
            }
        }
        if(getTree().equalsIgnoreCase("tree")){
            if(!playerHasAxe("Bronze axe")){
                ScriptManager.getScriptManager().stop();
            }
            //Logic for tree
            if(LocationConstants.LUMBRIDGETREES.distance()> 13 && !Inventory.isFull()){
                gh.walkToExactTile(LocationConstants.LUMBRIDGETREES.getTile(),4);
            }
            if(Inventory.isFull()){
                dropLogs();
            }
            if(!player().isAnimating() && !player().isInCombat() && GameObjects.closest(getTree()) != null && !Inventory.isFull() && LocationConstants.LUMBRIDGETREES.distance()< 13){
                if(Dialogues.canContinue()){
                    Dialogues.continueDialogue();
                }
                try{
                    if(Camera.getZoom()>750){
                        Camera.setZoom(rand.nextInt(150)+575);
                    }
                    //MAKE CODE HERE FOR DEALING WITH THE VIEW OF THE YEW TREE
                    //Make sure its actually properly in view when trying to cut it
                    //
                    //
                    //
                    Sleep.sleep(400,2000);
                    if(GameObjects.closest(getTree()).getTile().equals(new Tile(3233,3244,0)) || GameObjects.closest(getTree()).getTile().equals(new Tile(3235,3237,0))){
                        gh.turnToEntity(GameObjects.closest(getTree()));
                        GameObjects.closest(getTree()).interact();
                    }
//                Mouse.click(GameObjects.closest(getTree()));
                    if(!Tab.INVENTORY.isOpen()){
                        Tabs.open(Tab.INVENTORY);
                    }
                    if(rand.nextInt(100)>90){
                        //Perform a random act
                        //If player is still animating send the mouse off the screen again
                        //If player isn't animating and the tree is not showing send the mouse off the screen again
                        //If the player isn't animating and the tree is showing and inventory isn't full then slap that tree
                        if(rand.nextInt(100)>49){
                            gh.performRandomMouseMovement(true);
                        }else{
                            gh.performRandomMouseMovement(false);
                        }
                    }
                }catch (Exception e){
                    Logger.log("Cannot find tree or cannot turn to it");
                    Sleep.sleep(5000,10000);
                }
                Sleep.sleep(650,3000);
            }
            if(player().isAnimating() && !Inventory.isFull()){
                if(getTree().equals("tree")){
                    Sleep.sleep(400,900);
                }
                if(getTree().equalsIgnoreCase("willow")){
                    Mouse.moveOutsideScreen();
                    Sleep.sleep(15000,35000);
                    Logger.log("Going AFK for 15 - 35 seconds");
                }
                if(getTree().equalsIgnoreCase("oak")){
                    Mouse.moveOutsideScreen();
                    Sleep.sleep(15000,35000);
                    Logger.log("Going AFK for 15 - 35 seconds");
                }else{
                    Mouse.moveOutsideScreen();
                    Sleep.sleep(20000,40000);
                    Logger.log("Going AFK for 20 - 40 seconds");
                }
            }
            if(Inventory.isFull()){
                dropLogs();
            }
        }


        return rand.nextInt(50)+50;
    }
}