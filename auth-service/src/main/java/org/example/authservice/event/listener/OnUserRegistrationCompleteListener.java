package org.example.authservice.event.listener;

import org.example.authservice.event.OnUserRegistrationCompleteEvent;
import org.example.authservice.exception.MailSendException;
import org.example.authservice.model.entity.User;
import org.example.authservice.service.EmailVerificationTokenService;
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
@RequiredArgsConstructor
@Slf4j
public class OnUserRegistrationCompleteListener implements ApplicationListener<OnUserRegistrationCompleteEvent> {

    private final EmailVerificationTokenService emailVerificationTokenService;

    // @Lazy
    private final MailService mailService;

    @Override
    @Async
    public void onApplicationEvent(OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent) {
        sendEmailVerification(onUserRegistrationCompleteEvent);
    }

    /**
     * Send email verification to the user and persist the token in the database.
     */
    private void sendEmailVerification(OnUserRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = emailVerificationTokenService.generateNewToken();
        emailVerificationTokenService.createVerificationToken(user, token);
        String recipientAddress = user.getEmail();
        String emailConfirmationUrl = event.getUriBuilder().queryParam("token", token)
                .toUriString();

        try {
            mailService.sendEmailVerification(emailConfirmationUrl, recipientAddress);

        } catch (IOException | TemplateException | MessagingException e) {
            log.error(e.getMessage());
            throw new MailSendException(recipientAddress, "Email Verification");
        }

    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
