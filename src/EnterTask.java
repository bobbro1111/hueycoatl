import com.google.inject.Inject;
import config.Domain;
import data.HueyData;
import org.rspeer.game.Keyboard;
import org.rspeer.game.adapter.component.InterfaceComponent;
import org.rspeer.game.adapter.component.inventory.Backpack;
import org.rspeer.game.adapter.component.inventory.Bank;
import org.rspeer.game.adapter.scene.Npc;
import org.rspeer.game.adapter.scene.SceneObject;
import org.rspeer.game.component.*;
import org.rspeer.game.movement.Movement;
import org.rspeer.game.position.Position;
import org.rspeer.game.position.area.Area;
import org.rspeer.game.scene.Npcs;
import org.rspeer.game.scene.Players;
import org.rspeer.game.scene.SceneObjects;
import org.rspeer.game.script.Task;
import org.rspeer.game.script.TaskDescriptor;
import org.rspeer.game.social.Relationships;
import prepare.OrderTask;


@TaskDescriptor(name = "Enters correct area", blocking = true)
public class EnterTask extends Task {
    private static Area startArea = Area.rectangular(1523, 3297, 1533, 3287,0);
    private final Domain domain;
    @Inject
    public EnterTask(Domain domain) {
        this.domain = domain;
    }
    @Override
    public boolean execute() {
        if (Players.self().getPosition().fromInstance().within(HueyData.COMBAT_AREA)) {
            return false;
        }

        if (Dialog.isViewingChatOptions()) {Dialog.process("Let's do it!");} //Initial travel to varlamore may not work?

        InterfaceComponent glitchedBox = Interfaces.getDirect(10616874); //NOT_10747997 NOT_10616866
        if (glitchedBox.isVisible()) { //can prevent interactions with other interfaces
            if (glitchedBox.getText().contains("Enter amount:")) {
                Keyboard.sendText("0", true);
                return true;
            }
        }
        Npc pet = Npcs.query().ids(HueyData.NPC_PET_ID).results().nearest();
        if (pet != null) {
            if (pet.getTarget().equals(Players.self())) {
                handlePet();
                return true;
            }
        }
        if (Inventories.backpack().contains(iq -> iq.ids(HueyData.ITEM_PET_ID).results())) {
            handlePet();
            return true;
        }

        if (!Players.self().getPosition().fromInstance().within(HueyData.START_AREA)) {
            Movement.walkTo(Position.from(1528,3291));
            OrderTask.setTask("Walking to start");
            OrderTask.setSubtask("Walking to start");
            return true;
        }

        SceneObject flag = SceneObjects.query().ids(HueyData.FLAG_1).results().nearest();
        InterfaceComponent hostEntry = Interfaces.getDirect(10616875);
        if (hostEntry.isVisible() && flag!=null) {
            Keyboard.sendText(domain.getConfig().getHostName(), true);
            sleepUntil(() -> (flag==null), 10);
            OrderTask.setSubtask("Entering host name");
            OrderTask.setHostName(domain.getConfig().getHostName());
            return true;
        }

        InterfaceComponent fightOptions = Interfaces.getDirect(219,1);
        if (fightOptions!=null) {
            if (domain.getConfig().shouldHost()) {
                fightOptions.getSubComponent(1).interact("Continue");
            } else {
                fightOptions.getSubComponent(3).interact("Continue");
            }
            OrderTask.setSubtask("Entering instance");
            return true;
        }

        if (domain.getConfig().shouldInstance() && flag!=null) {
            flag.interact("Fight-privately");
            return true;
        }
        SceneObject chain = SceneObjects.query().ids(55401).results().first();
        if (!Players.self().getPosition().fromInstance().within(HueyData.COMBAT_AREA)) {
            chain.interact("Quick-climb");
            OrderTask.setSubtask("Climbing chain");
        }
        return true;
    }

    private static void handlePet() { //Should handle pets in instance or regular
        Backpack backpack = Inventories.backpack();
        OrderTask.setSubtask("Banking pet");
        Npc pet = Npcs.query().ids(HueyData.NPC_PET_ID).results().nearest();
        if (backpack.contains(iq -> iq.ids(HueyData.ITEM_PET_ID).results())) {
            if (!Bank.isOpen()) {
                SceneObjects.query().actions("Bank").results().nearest().interact("Bank");
                return;
            }
            Inventories.bank().depositInventory();
            return;
        }
        if (!pet.isInFieldOfViewOf(Players.self())) {
            InterfaceComponent exitOption = Interfaces.getDirect(219,1,1);
            if (exitOption!=null) {
                exitOption.interact("Continue");
                return;
            }
            SceneObject flag = SceneObjects.query().ids(HueyData.FLAG_2).results().nearest();
            if (flag!=null) {
                flag.interact("Leave");
                return;
            }
        }
        if (!backpack.contains(iq -> iq.ids(HueyData.ITEM_PET_ID).results())) {
            if (backpack.isFull()) {
                backpack.query().ids(385).results().first().interact("Eat");
                return;
            }
            pet.interact("Pick-up");
            return;
        }

    }
    public static Boolean hostOnline(String hostName) {
        return Relationships.friends().names(hostName).results().first().isOnline();
    }
}