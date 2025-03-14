package org.example.authservice.event;

import org.example.authservice.model.entity.EmailVerificationToken;
import org.example.authservice.model.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Clock;

public class OnRegenerateEmailVerificationEvent extends ApplicationEvent {

    @Getter
    private transient UriComponentsBuilder redirectUrl;

    @Getter
    private  EmailVerificationToken emailVerificationToken;

    public OnRegenerateEmailVerificationEvent(Object source, EmailVerificationToken emailVerificationToken,UriComponentsBuilder redirectUrl ) {
        super(source);
        this.emailVerificationToken = emailVerificationToken;
        this.redirectUrl = redirectUrl;

    }



    public OnRegenerateEmailVerificationEvent(Object source, Clock clock) {
        super(source, clock);
    }

    public User getUser(){return emailVerificationToken.getUser();}

}
