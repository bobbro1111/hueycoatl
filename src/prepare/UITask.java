package prepare;

import com.google.inject.Inject;
import org.rspeer.game.script.Task;
import org.rspeer.game.script.TaskDescriptor;
import config.Config;

@TaskDescriptor(
        name = "Waiting on user config",
        blocking = true
)
public class UITask extends Task {

    private final Config config;

    @Inject
    public UITask(Config config) {
        this.config = config;
    }

    @Override
    public boolean execute() {
        return !config.isBound();
    }
}