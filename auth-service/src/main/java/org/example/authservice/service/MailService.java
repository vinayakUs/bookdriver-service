package org.example.authservice.service;

import org.example.authservice.model.Mail;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor

public class MailService {

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Value("${app.velocity.templates.location}")
    private String basePackagePath;

    @Value("${app.token.password.reset.duration}")
    private Long resetTokenExpiration;

    private final JavaMailSender mailSender;

    private final Configuration templateConfiguration;

    public void sendEmailVerification(String emailConfirmationUrl, String to)
            throws IOException, TemplateException, MessagingException {
        Mail mail = new Mail();
        mail.setSubject("Email Verification");
        mail.setTo(to);
        mail.setFrom(mailFrom);
        mail.getModel().put("userName", to);
        mail.getModel().put("userEmailTokenVerificationLink", emailConfirmationUrl);
        templateConfiguration.setClassForTemplateLoading(getClass(), basePackagePath);
        Template template = templateConfiguration.getTemplate("email-verification.ftl");
        String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
        mail.setContent(mailContent);
        send(mail);

    }

    /**
     * Sends a simple mail as a MIME Multipart message
     */
    private void send(Mail mail) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.setTo(mail.getTo());
        helper.setText(mail.getContent(), true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());
        mailSender.send(message);
    }
    /**
     * Setting the mail parameters.Send the reset link to the respective user's mail
     */
    public void sendResetLink(String resetPasswordUrl, String to)
            throws IOException, TemplateException, MessagingException {

        String expirationInMinutesString = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(resetTokenExpiration));
        Mail mail = new Mail();
        mail.setSubject("Password Reset Link");
        mail.setTo(to);
        mail.setFrom(mailFrom);

        mail.getModel().put("userName", to);
        mail.getModel().put("userResetPasswordLink", resetPasswordUrl);
        mail.getModel().put("expirationTime", expirationInMinutesString);

        templateConfiguration.setClassForTemplateLoading(getClass(), basePackagePath);
        Template template = templateConfiguration.getTemplate("reset-link.ftl");

        String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
        mail.setContent(mailContent);
        send(mail);

    }
    /**
     * Send an email to the user indicating an account change event with the correct
     * status
     */
    public void sendAccountChangeEmail(String action, String actionStatus, String to) throws IOException, TemplateException, MessagingException {
        Mail mail = new Mail();
        mail.setSubject("Account Status Change");
        mail.setTo(to);
        mail.setFrom(mailFrom);
        mail.getModel().put("userName", to);
        mail.getModel().put("action", action);
        mail.getModel().put("actionStatus", actionStatus);
        templateConfiguration.setClassForTemplateLoading(getClass(), basePackagePath);
        Template template = templateConfiguration.getTemplate("account-activity-change.ftl");
        String mailCont = FreeMarkerTemplateUtils.processTemplateIntoString(template,mail.getModel());
        mail.setContent(mailCont);
        send(mail);
    }
}
