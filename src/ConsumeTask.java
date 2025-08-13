import data.HueyData;
import org.rspeer.commons.logging.Log;
import org.rspeer.game.adapter.scene.EffectObject;
import org.rspeer.game.adapter.scene.Npc;
import org.rspeer.game.adapter.scene.SceneObject;
import org.rspeer.game.component.Inventories;
import org.rspeer.game.component.Item;
import org.rspeer.game.component.tdi.Prayers;
import org.rspeer.game.component.tdi.Skill;
import org.rspeer.game.component.tdi.Skills;
import org.rspeer.game.effect.Health;
import org.rspeer.game.scene.EffectObjects;
import org.rspeer.game.scene.Npcs;
import org.rspeer.game.scene.Players;
import org.rspeer.game.scene.SceneObjects;
import org.rspeer.game.script.Task;
import org.rspeer.game.script.TaskDescriptor;
import prepare.OrderTask;


@TaskDescriptor(name = "Smacking THE big boy")
public class ConsumeTask extends Task {
    private final int[] FOOD_IDS = {333, 385, 391};
    private final int[] PRAYER_IDS = {143, 141, 139, 2434, 3024, 3026, 3028, 3030};
    @Override
    public boolean execute() {
        // Eat food when low
        if (Health.getCurrent() < Health.getLevel() * .5) {
            Item food = Inventories.backpack().query().ids(FOOD_IDS).results().first();
            if (food != null) {
                food.interact("Eat");
                OrderTask.setSubtask("Eating");
                return true;
            }
        }

        // Handle prayer restoration if prayer is low
        if (Prayers.getPercent() < 70) {
            Item potion = Inventories.backpack().query().ids(PRAYER_IDS).results().first();
            if (potion != null) {
                potion.interact("Drink");
                OrderTask.setSubtask("Drinking prayer potion");
                return true;
            }
        }

        SceneObject glowingSymbols = SceneObjects.query().ids(55209).results().first();
        EffectObject flash = EffectObjects.query().ids(3001).results().first();
        if (glowingSymbols != null && flash == null) {
            DodgeTask.dodgeSymbols();
            return true;
        }

        if (Skills.getCurrentLevel(Skill.STRENGTH) < Skills.getLevel(Skill.STRENGTH) * 1.1) {
            Item potion = Inventories.backpack().query().nameContains("combat").results().first();
            if (potion != null) {
                potion.interact("Drink");
                OrderTask.setSubtask("Drinking combat potion");
                return true;
            }
        }

        return true;
    }
}