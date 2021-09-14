package com.fgrapp.base.runner;

import com.fgrapp.base.config.redis.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * runner
 *
 * @author fan guang rui
 * @date 2021年08月07日 21:24
 */
@Slf4j
@Component
@Order
public class FgrappAdminRunner implements CommandLineRunner{

    @Value("${server.port}")
    private int port;
    @Value("${fgr.swagger.version}")
    private String version;
    @Value("${fgr.ip}")
    private String ip;
    @Value("${fgr.gtihub}")
    private String gtihub;
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private String redisPort;
    @Value("${spring.redis.database}")
    private String redisDatabase;
    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) {
        redisCache.deleteObject("testKey");
        log.info("Redis:"+redisHost+":"+redisPort+"("+redisDatabase+")-"+"连接成功");
        System.out.println("########  ######   ########     ###    ########  ########\n" +
                           "##       ##    ##  ##     ##   ## ##   ##     ## ##     ##\n" +
                           "##       ##        ##     ##  ##   ##  ##     ## ##     ##\n" +
                           "######   ##   #### ########  ##     ## ########  ########\n" +
                           "##       ##    ##  ##   ##   ######### ##        ##\n" +
                           "##       ##    ##  ##    ##  ##     ## ##        ##\n" +
                           "##        ######   ##     ## ##     ## ##        ##");
        System.out.println("App running at:\n" +
                "- Local:   http://localhost:"+port+"/doc.html (v"+version+")\n" +
                "- Network: https://"+ip+":"+port+"/doc.html (v"+version+")\n" +
                "- GitHub： "+gtihub);
    }
}
