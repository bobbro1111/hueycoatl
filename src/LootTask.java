import org.rspeer.commons.logging.Log;
import org.rspeer.game.adapter.scene.Pickable;
import org.rspeer.game.component.Inventories;
import org.rspeer.game.scene.Pickables;
import org.rspeer.game.script.Task;
import org.rspeer.game.script.TaskDescriptor;
import prepare.OrderTask;

@TaskDescriptor(name = "Loots")
public class LootTask extends Task {
    public final int[] LOOT_IDS =
            {30085,30066,30070,1433,
                    1334,1124,560,557,564,
                    561,5298,5299,5302,30088,
                    5296,5304,5295,30068,9380,
                    574,384,28924,29379,537,
                    2,450,824,226,30105
            };
    @Override
    public boolean execute() {
        Pickable item = Pickables.query().ids(LOOT_IDS).results().nearest();
        if (item==null) {
            return false;
        }
        OrderTask.setTask("Looting");
        if (Inventories.backpack().isFull()) { //Munch shark if no space
            OrderTask.setTask("Inv full, making space");
            return Inventories.backpack().query().actions("Eat").results().first().interact("Eat");
        }
        item.interact("Take");
        return true;
    }
}