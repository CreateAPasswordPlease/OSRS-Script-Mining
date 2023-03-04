import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Player;
import sun.font.Script;

import java.awt.*;
import java.util.Random;

@ScriptManifest(name = "Woodcutting Script v1.09",
        description = "A simple F2P Woodcutting script focused on leveling",
        author = "Blank0001",
        version = 1.0, category = Category.MINING, image = "")

//yellowdog001@gmail.com Matthew666

public class WoodcuttingScript extends AbstractScript {
    public void onStart(){
        Logger.log("Welcome to Mining 101 script v1.03");
    }
    private Player player(){
        return Players.getLocal();
    }
    GenericHelper gh = new GenericHelper();
    Player localPlayer = Players.getLocal();
    Random rand = new Random();
    int upperbound = 100;
    int lastTinMined = 99;

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
        gh.walkToExactTile(LocationConstants.LUMBRIDGEBANK);
        if(LocationConstants.LUMBRIDGEBANK.distance() < 5){
            gh.turnToEntity(GameObjects.closest("Bank booth"));
            Mouse.click(GameObjects.closest("Bank booth"));

            if(!Bank.isOpen()){
                Bank.open(BankLocation.LUMBRIDGE);
                Sleep.sleep(400,900);
            }
            if(Bank.isOpen()){
                Bank.depositAll("Yew logs");
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

    @Override
    public int onLoop() {


        if(rand.nextInt(100)>97){
            //Perform a random act
            //If player is still animating send the mouse off the screen again
            //If player isn't animating and the tree is not showing send the mouse off the screen again
            //If the player isn't animating and the tree is showing and inventory isn't full then slap that tree
        }
        if(LocationConstants.LUMBRIDGEYEW.distance()> 7 && !Inventory.isFull()){
            gh.walkToExactTile(LocationConstants.LUMBRIDGEYEW.getTile());
        }
        if(Inventory.isFull()){
            bankLogs();
        }
        if(!player().isAnimating() && !player().isInCombat() && GameObjects.closest(getTree()) != null && !Inventory.isFull()){
            if(Dialogues.canContinue()){
                Dialogues.continueDialogue();
            }
            try{
                gh.turnToEntity(GameObjects.closest(getTree()));
                Mouse.click(GameObjects.closest(getTree()));
                if(!Tab.INVENTORY.isOpen()){
                    Tabs.open(Tab.INVENTORY);
                }
            }catch (Exception e){
                Logger.log("Cannot find tree or cannot turn to it");
                Sleep.sleep(5000,10000);
            }
            Sleep.sleep(650,2350);
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

        return 350;
    }
}