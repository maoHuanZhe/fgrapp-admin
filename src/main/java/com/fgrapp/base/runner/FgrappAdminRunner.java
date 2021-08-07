package com.fgrapp.base.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
    @Value("${fgr.version}")
    private String version;

    @Override
    public void run(String... args) throws UnknownHostException {
        String ip = InetAddress.getLocalHost().getHostAddress();
        System.out.println("########  ######   ########     ###    ########  ########\n" +
                           "##       ##    ##  ##     ##   ## ##   ##     ## ##     ##\n" +
                           "##       ##        ##     ##  ##   ##  ##     ## ##     ##\n" +
                           "######   ##   #### ########  ##     ## ########  ########\n" +
                           "##       ##    ##  ##   ##   ######### ##        ##\n" +
                           "##       ##    ##  ##    ##  ##     ## ##        ##\n" +
                           "##        ######   ##     ## ##     ## ##        ##");
        System.out.println("App running at:\n" +
                "- Local:   http://localhost:"+port+"/doc.html (v"+version+")\n" +
                "- Network: http://"+ip+":"+port+"/doc.html (v"+version+")\n" +
                "- GitHub：https://github.com/dromara/sa-token");
    }
}
