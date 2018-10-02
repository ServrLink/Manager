package link.servr.manager.core;

import link.servr.manager.core.redis.RedisWrapper;

public class Core implements Runnable {

    private static Core core;

    public static Core getInstance() {
        return core;
    }

    public ConfigManager configManager;
    public RedisWrapper redis;
    public ServrLink servrLink;

    public Core(ServrLink servrLink) {
        core = this;

        this.servrLink = servrLink;
        this.configManager = new ConfigManager("config.yml", servrLink.getDirectory());
        this.redis = new RedisWrapper();
    }

    @Override
    public void run() {
        configManager.setup();
        redis.run();
    }
}
