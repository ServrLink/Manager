package link.servr.manager.core;

import link.servr.manager.core.redis.Redis;

public class Core implements Runnable {

    private static Core core;

    private static void setInstance(Core instance) {
        core = instance;
    }

    public static Core getInstance() {
        return core;
    }

    public ConfigManager configManager;
    public Redis redis;
    public ServrLink servrLink;

    public Core(ServrLink servrLink) {
        Core.setInstance(this);

        this.servrLink = servrLink;
        this.configManager = new ConfigManager("config.yml", servrLink.getDirectory());
        this.redis = new Redis();
    }

    @Override
    public void run() {
        configManager.setup();
    }
}
