package org.example.authservice.event.listener;

import org.example.authservice.event.OnRegenerateEmailVerificationEvent;
import org.example.authservice.exception.MailSendException;
import org.example.authservice.model.entity.EmailVerificationToken;
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

@Component
@Slf4j
@RequiredArgsConstructor
public class onRegenerateEmailVerificationListener implements ApplicationListener<OnRegenerateEmailVerificationEvent> {

    private final MailService mailService;

    @Async
    @Override
    public void onApplicationEvent(OnRegenerateEmailVerificationEvent event) {
        resendEmailVerification(event);
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }

    private void resendEmailVerification(OnRegenerateEmailVerificationEvent event) {

        User user= event.getUser();
        String recipientAddress = user.getEmail();
        EmailVerificationToken emailVerificationToken = event.getEmailVerificationToken();

        String emailConfirmationUrl= event.getRedirectUrl().queryParam("token" , emailVerificationToken.getToken()).toUriString();

        try {
            mailService.sendEmailVerification(emailConfirmationUrl,recipientAddress);
        } catch (IOException |TemplateException |MessagingException e) {
            log.error(e.getMessage());
            throw new MailSendException(recipientAddress,"Email Verification");
        }


    }
}
