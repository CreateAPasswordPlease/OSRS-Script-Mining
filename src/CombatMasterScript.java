import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;

import java.util.Random;

@ScriptManifest(name = "Combat Master v1.01",
        description = "A Combat master script focused on leveling",
        author = "Blank0001",
        version = 1.0, category = Category.MINING, image = "")

//yellowdog001@gmail.com Matthew666

public class CombatMasterScript extends AbstractScript {
    public void onStart(){
        Logger.log("Welcome to COMBAT MASTER 101 script v1.01");
    }
    private Player player(){
        return Players.getLocal();
    }
    GenericHelper gh = new GenericHelper();
    Random rand = new Random();
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
    private boolean isAccountCombatReady(){
        if(Inventory.contains("Training sword")){
            Inventory.interact("Training sword");
            Sleep.sleep(500,1300);
        }
        if(Inventory.contains("Training shield")){
            Inventory.interact("Training shield");
            Sleep.sleep(500,1300);
        }
        if(Equipment.contains("Training sword") && Equipment.contains("Training shield")){
            return true;
        }
        return false;
    }
    private String setTrainingArea(){
        if(isAccountCombatReady() && Skills.getRealLevel(Skill.ATTACK) <= 15 && Skills.getRealLevel(Skill.DEFENCE) <= 15 && Skills.getRealLevel(Skill.STRENGTH) <= 15){
            return "Chicken";
        }
        if(isAccountCombatReady() && Skills.getRealLevel(Skill.ATTACK) > 15 && Skills.getRealLevel(Skill.DEFENCE) > 15 && Skills.getRealLevel(Skill.STRENGTH) > 15){
            return "Cow";
        }
        return null;
    }
    private boolean goToTrainingArea(String enemy){
        if(enemy.equalsIgnoreCase("Chicken")){
            if(LocationConstants.CHICKENS.distance()<8){
                return true;
            }
            gh.walkToExactTile(LocationConstants.CHICKENS,6);
            if(LocationConstants.CHICKENS.distance()<8){
                return true;
            }
        }
        if(enemy.equalsIgnoreCase("Cow")){
            if(LocationConstants.COWS.distance()<8){
                return true;
            }
            gh.walkToExactTile(LocationConstants.COWS,6);
            if(LocationConstants.COWS.distance()<8){
                return true;
            }
        }
        return false;
    }
    private void trainCombat(String npcName){
        if(gh.searchForNearestNPCWithName(npcName)!=null){
            NPC enemy = gh.searchForNearestNPCWithName(npcName);
            //NPC was found
            if(!enemy.isInCombat() && !player().isInCombat()){
                enemy.interact();
            }
        }
    }


    @Override
    public int onLoop() {

        if(gh.accountIsNew()){
            gh.walkToExactTile(LocationConstants.LUMBRIDGEBANK,3);
            if(LocationConstants.LUMBRIDGEBANK.distance() < 5){
                gh.turnToEntity(GameObjects.closest("Bank booth"));
                Mouse.click(GameObjects.closest("Bank booth"));
                if(!Bank.isOpen()){
                    Bank.open(BankLocation.LUMBRIDGE);
                    Sleep.sleep(1000,1500);
                }
                if(Bank.isOpen()){
                    Sleep.sleep(500,1000);
                    Bank.depositAllItems();
                    Sleep.sleep(400,1200);
                }
                Bank.close();
                if(Bank.isOpen()){
                    Bank.close();
                }
            }
        }
        if(!gh.accountIsNew() && !isAccountCombatReady()){
            //GO GET THE TRAINING SWORD AND SHIELD
            if(LocationConstants.COMBATTUTORS.distance()>9){
                gh.walkToExactTile(LocationConstants.COMBATTUTORS,4);
            }
            gh.turnToEntity(gh.searchForNearestNPCWithName("Melee combat tutor"));
            gh.searchForNearestNPCWithName("Melee combat tutor").interact();
            Sleep.sleep(900,2000);

            while(player().isInteracting(gh.searchForNearestNPCWithName("Melee combat tutor"))){
                if(Dialogues.inDialogue()){
                    if(Dialogues.canContinue()){
                        Dialogues.continueDialogue();
                        Sleep.sleep(900,1500);
                    }
                    if(Dialogues.areOptionsAvailable()){
                        Dialogues.chooseOption(4);
                        Sleep.sleep(900,1500);
                    }
                }
                if(Inventory.contains("Training sword") && Inventory.contains("Training shield")){
                    break;
                }
            }
            if(Dialogues.canContinue()){
                Dialogues.continueDialogue();
            }else{
                Dialogues.chooseOption(4);
                if(Dialogues.canContinue()){
                    Dialogues.continueDialogue();
                }
            }
        }
        if(goToTrainingArea(setTrainingArea())){
            if(!player().isInCombat()){
                trainCombat(setTrainingArea());
            }
            while(player().isInCombat() || player().isMoving()){
                Sleep.sleep(500,1500);
                if(rand.nextInt(100)==98){
                    //Perform a random act
                    //If player is still animating send the mouse off the screen again
                    //If player isn't animating and the tree is not showing send the mouse off the screen again
                    //If the player isn't animating and the tree is showing and inventory isn't full then slap that tree
                    Logger.log("WOULD'VE PERFORMED A RANDOM ACT");
                }
                Logger.log("Still in combat or moving sleeping for 1.5 - 3 seconds");
            }
        }




        return 350;
    }
}