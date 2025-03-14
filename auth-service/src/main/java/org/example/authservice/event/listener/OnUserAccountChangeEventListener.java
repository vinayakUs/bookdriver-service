package org.example.authservice.event.listener;

import org.example.authservice.event.OnUserAccountChangeEvent;
import org.example.authservice.exception.MailSendException;
import org.example.authservice.model.entity.User;
import org.example.authservice.service.MailService;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class OnUserAccountChangeEventListener implements ApplicationListener<OnUserAccountChangeEvent> {

    private final MailService mailService;

    /**
     * As soon as a registration event is complete, invoke the email verification
     * asynchronously in an another thread pool
     */
    @Override
    @Async
    public void onApplicationEvent(OnUserAccountChangeEvent event) {
        sendAccountChangeEmail(event);
    }
    /**
     * Send email verification to the user.
     */
    private void sendAccountChangeEmail(OnUserAccountChangeEvent event) {
        User user = event.getUser();
        String to = user.getEmail();
        String action = event.getAction();
        String actionStatus= event.getActionStatus();
        try {
            mailService.sendAccountChangeEmail(action, actionStatus, to);
        }catch (IOException| TemplateException| MessagingException ex){
            log.error(String.valueOf(ex));
            throw new MailSendException("recipientAddress","Account Change Mail");

        }

    }
}
