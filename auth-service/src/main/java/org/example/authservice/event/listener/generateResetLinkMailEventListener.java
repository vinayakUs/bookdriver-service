package org.example.authservice.event.listener;

import org.example.authservice.event.OnGenerateResetLinkEvent;
import org.example.authservice.exception.MailSendException;
import org.example.authservice.model.entity.PasswordResetToken;
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
public class generateResetLinkMailEventListener implements ApplicationListener<OnGenerateResetLinkEvent> {

    private final MailService mailService;

    /*
     * Send Reset link when a valid email is entered
     */

    @Override
    @Async
    public void onApplicationEvent(OnGenerateResetLinkEvent event) {
        sendResetLink(event);
    }


    /*
     * Sends Reset Link to the mail address with a password reset link token
     */
    private void sendResetLink(OnGenerateResetLinkEvent event) {
        PasswordResetToken token = event.getPasswordResetToken();
        String to = token.getUser().getEmail();
        String resetPasswordUrl = event.getRedirectUrl().queryParam("token",token.getToken()).toUriString();


        try{
            mailService.sendResetLink(resetPasswordUrl,to);
        }catch(IOException |TemplateException| MessagingException ex){
            log.error(ex.getMessage());
            throw new MailSendException(to,"Email Verification");

        }

     }

}
