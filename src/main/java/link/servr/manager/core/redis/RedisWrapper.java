package link.servr.manager.core.redis;

import link.servr.manager.core.ConfigManager;
import link.servr.manager.core.Core;
import link.servr.manager.core.ServrLink;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisWrapper implements Runnable {

    private ConfigManager cfgMgr = Core.getInstance().configManager;
    private ServrLink servrLink = Core.getInstance().servrLink;

    private JedisPool pool;

    @Override
    public void run() {
        JedisPoolConfig cfg = new JedisPoolConfig();

        cfg.setMaxTotal(cfgMgr.getGeneric("redis.pool.max-connections", 5));
        cfg.setMaxIdle(cfgMgr.getGeneric("redis.pool.max-idle", 5));

        if(cfgMgr.getGeneric("redis.authentication.enable", true)) {
            pool = new JedisPool(
                    cfg,
                    cfgMgr.getGeneric("redis.host", "127.0.0.1"),
                    cfgMgr.getGeneric("redis.port", 6379),
                    cfgMgr.getGeneric("redis.timeout", 120) * 1000,
                    cfgMgr.getGeneric("redis.authentication.password", ""),
                    cfgMgr.getGeneric("redis.ssl", true)
            );
        } else {
            pool = new JedisPool(
                    cfg,
                    cfgMgr.getGeneric("redis.host", "127.0.0.1"),
                    cfgMgr.getGeneric("redis.port", 6379),
                    cfgMgr.getGeneric("redis.timeout", 120) * 1000,
                    cfgMgr.getGeneric("redis.ssl", true)
            );
        }
    }

    public void setWithExpire(String key, Object value, int expire) {
        servrLink.runAsync(() -> {
            try(Jedis jedis = pool.getResource()) {
                jedis.set(key, value.toString());
                jedis.expire(key, expire);
            }
        });
    }
}
