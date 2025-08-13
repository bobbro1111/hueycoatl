package config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.rspeer.event.Service;


@Singleton
public class Domain implements Service {

    private final Config config;

    @Inject
    public Domain(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }
}