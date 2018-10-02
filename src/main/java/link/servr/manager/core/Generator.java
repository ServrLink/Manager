package link.servr.manager.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {

    private ConfigManager cfg = Core.getInstance().configManager;

    private char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();

    private String key;

    public Generator() {
        key = generateVerificationKey(32);
    }

    public void register(UUID uuid) {
        Core.getInstance().redis.setWithExpire(key, uuid, 600);
    }

    public URL getURL() {
        try {
            return new URL(cfg.getGeneric("base-url", "https://go.servr.link/") + "?vkey=" + key);
        } catch(MalformedURLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String generateVerificationKey(int length) {
        StringBuilder str = new StringBuilder();

        for(int i = 0; i < length; i++) {
            str.append(chars[ThreadLocalRandom.current().nextInt(chars.length)]);
        }

        return str.toString();
    }
}
