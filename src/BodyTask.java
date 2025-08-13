import data.HueyData;
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
import org.rspeer.game.scene.SceneObjects;
import org.rspeer.game.script.Task;
import org.rspeer.game.script.TaskDescriptor;
import prepare.OrderTask;


@TaskDescriptor(name = "Smacking big boy's tails")
public class BodyTask extends Task {
    private static int[] foodIds = {333, 385, 391};
    private static int[] prayerIds = {143, 141, 139, 2434, 3024, 3026, 3028, 3030};
    @Override
    public boolean execute() {
        Npc body = Npcs.query().nameContains("Hueycoatl body").results().nearest();
        if (body==null) {
            return false;
        }
        OrderTask.setTask("Body phase");

        SceneObject glowingSymbols = SceneObjects.query().ids(HueyData.GLOWING_SYMBOL).results().first();
        EffectObject flash = EffectObjects.query().ids(HueyData.FLASH).results().first();
        if (glowingSymbols != null && flash == null) {
            DodgeTask.dodgeSymbols();
            return true;
        }

        if (body != null) {
            body.interact("Attack");
            return true;
        }

        return true;
    }
}