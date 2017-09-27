package ru.worm.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.IOException;

public class MainClient {
    private static AbstractXmlApplicationContext context;
    private static final String SPRING_CONFIGURATION_XML = "classpath*:spring_configuration.xml";
    private static Logger log;

    public static void main(String[] args) throws IOException {
        log = LoggerFactory.getLogger(MainClient.class);
        try {
            context = new FileSystemXmlApplicationContext(SPRING_CONFIGURATION_XML);
        } catch (BeansException e) {
            log.error("error on spring context initialization", e);
            System.exit(1);
        }
        try {
			Object starter = context.getBean("clientStarter");
			starter.getClass().getDeclaredMethod("start").invoke(starter);
		} catch (BeansException e) {
			log.debug("no starter bean defined");
		} catch (Exception e) {
			log.error("starter failed", e);
			System.exit(1);
		}
    }
}
