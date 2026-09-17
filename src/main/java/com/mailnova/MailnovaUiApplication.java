package com.mailnova;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme("mailnova")
@PWA(name = "MailNova", shortName = "MailNova")
public class MailnovaUiApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(MailnovaUiApplication.class, args);
    }

}
