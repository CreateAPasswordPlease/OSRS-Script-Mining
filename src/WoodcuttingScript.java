import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Player;

import java.awt.*;
import java.util.Random;

@ScriptManifest(name = "Woodcutting Script v1.01",
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
            Inventory.dropAll();
            Sleep.sleep(500,3000);
        }
        return Inventory.isEmpty();
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


        if(Inventory.isFull()){
            dropLogs();
        }
        if(!player().isAnimating() && !player().isInCombat()){
            if(Dialogues.canContinue()){
                Dialogues.continueDialogue();
            }
            gh.turnToEntity(GameObjects.closest(getTree()));
            Mouse.click(GameObjects.closest(getTree()));
            Sleep.sleep(650,2350);
        }
        if(player().isAnimating()){
            if(getTree().equals("tree")){
                Sleep.sleep(400,900);
            }else{
                Mouse.moveOutsideScreen();
                Sleep.sleep(20000,40000);
                Logger.log("Going AFK for 20 - 50 seconds");
            }
        }
        if(Inventory.isFull()){
            dropLogs();
        }

        return 350;
    }

}