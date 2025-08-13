import org.rspeer.game.VarComposite;
import org.rspeer.game.Vars;
import org.rspeer.game.adapter.component.inventory.Backpack;
import org.rspeer.game.adapter.scene.Npc;
import org.rspeer.game.component.Inventories;
import org.rspeer.game.component.Item;
import org.rspeer.game.movement.Movement;
import org.rspeer.game.position.Position;
import org.rspeer.game.position.area.Area;
import org.rspeer.game.scene.Npcs;
import org.rspeer.game.script.Task;
import org.rspeer.game.script.TaskDescriptor;


@TaskDescriptor(name = "Enters correct area", blocking = true)
public class DeathTask extends Task {
    private static Area huey = Area.rectangular(1523,3295,1531,3289,0);
    private static String[] equipIds = {
            "Blood","glory","Dual macuahuitl","combat",
            "cloak","blessing","dragon","warrior",
            "monk","cudgel"
    };
    @Override
    public boolean execute() {
        if (Vars.get(VarComposite.GRAVESTONE_TIMER_VALUE)==0) {return false;}
        Backpack backpack = Inventories.backpack();
        if (backpack.contains(iq -> iq.nameContains(equipIds).results())) {
            for (String item : equipIds) {
                Item temp = backpack.query().nameContains(item).results().first();
                if (temp==null) {
                    break;
                }
                if (temp.containsAction("Wear")) {
                    temp.interact("Wear");
                }
                if (temp.containsAction("Wield")) {
                    temp.interact("Wield");
                }
            }
        }
        Npc grave = Npcs.query().nameContains("Grave").within(huey).results().nearest();
        if (grave!=null) {
            grave.interact("Loot");
            return true;
        } else {
            Movement.walkTo(new Position(1528,3291));
        }
        return true;
    }
}