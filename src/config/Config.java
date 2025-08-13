package config;

import com.google.inject.Singleton;
import data.Food;
import data.Prayer;
import org.rspeer.game.script.model.ConfigModel;
import org.rspeer.game.script.model.ui.schema.checkbox.CheckBoxComponent;
import org.rspeer.game.script.model.ui.schema.selector.SelectorComponent;
import org.rspeer.game.script.model.ui.schema.text.TextFieldComponent;
import org.rspeer.game.script.model.ui.schema.text.TextInputType;

@Singleton
public class Config extends ConfigModel {

    @SelectorComponent(name = "Food", key = "food", type = Food.class)
    private Food food;

    @SelectorComponent(name = "Prayer", key = "prayer", type = Prayer.class)
    private Prayer prayer;

    @CheckBoxComponent(name = "Should Instance", key = "instance")
    private boolean instance;

    @CheckBoxComponent(name = "Should host", key = "host")
    private boolean shouldHost;

    @TextFieldComponent(name = "Hostname", key = "host_name", inputType = TextInputType.ANY)
    private String hostName;

    public Food getFood() {
        return food;
    }

    public Prayer getPrayer() {
        return prayer;
    }

    public boolean shouldInstance() {
        return instance;
    }

    public boolean shouldHost() {
        return shouldHost;
    }

    public String getHostName() {
        return hostName;
    }
}