import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
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
import sun.rmi.runtime.Log;

import java.util.Random;

@ScriptManifest(name = "Fishing Script v1.01",
        description = "A simple F2P Fishing script focused on leveling",
        author = "Blank0001",
        version = 1.0, category = Category.MINING, image = "")

//yellowdog001@gmail.com Matthew666

public class FishingScript extends AbstractScript {
    public void onStart(){
        Logger.log("Welcome to Fishing 101 script v1.01");
    }
    private Player player(){
        return Players.getLocal();
    }
    GenericHelper gh = new GenericHelper();
    Random rand = new Random();
    private boolean accountIsNew = true;

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
        gh.walkToExactTile(LocationConstants.LUMBRIDGEBANK,3);
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

        Logger.log("MOUSE POINT: "+Mouse.getPosition());
        gh.performRandomMouseMovement(true);
        Sleep.sleep(2000);
        if(rand.nextInt(100)>97){
            //Perform a random act
            //If player is still animating send the mouse off the screen again
            //If player isn't animating and the tree is not showing send the mouse off the screen again
            //If the player isn't animating and the tree is showing and inventory isn't full then slap that tree
        }
//        if(accountIsNew && gh.accountIsNew()){
//            gh.walkToExactTile(LocationConstants.LUMBRIDGEBANK,3);
//            if(LocationConstants.LUMBRIDGEBANK.distance() < 5){
//                gh.turnToEntity(GameObjects.closest("Bank booth"));
//                Mouse.click(GameObjects.closest("Bank booth"));
//                if(!Bank.isOpen()){
//                    Bank.open(BankLocation.LUMBRIDGE);
//                    Sleep.sleep(1000,1500);
//                }
//                if(Bank.isOpen()){
//                    Sleep.sleep(500,1000);
//                    Bank.depositAllItems();
//                    Sleep.sleep(400,1200);
//                }
//                Bank.close();
//                if(Bank.isOpen()){
//                    Bank.close();
//                }
//            }
//            if(Inventory.isEmpty()){
//                //Inv is empty we mustve been successful
//                accountIsNew = false;
//            }
//        }


        return 350;
    }
}