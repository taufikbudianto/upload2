/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service.scheduler;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import prosia.basarnas.service.ws.cilent.RegistrasiPlb;

/**
 *
 * @author Ismail
 */
@Component
@ConditionalOnProperty("sar.scheduler.enable")
public class SyncScheduler {

    @Autowired
    private RegistrasiPlb registrasiPlb;

    @Scheduled(cron = "${sar.scheduler.time}")
    private void schedulerWsClient() throws IOException {
        registrasiPlb.saveRegistartionPlb();
    }
}
