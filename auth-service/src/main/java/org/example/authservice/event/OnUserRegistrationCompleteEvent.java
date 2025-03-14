package org.example.authservice.event;

import org.example.authservice.model.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
public class OnUserRegistrationCompleteEvent extends ApplicationEvent {

    private User user;
    private transient UriComponentsBuilder uriBuilder;


    public OnUserRegistrationCompleteEvent(User user, UriComponentsBuilder uriBuilder) {
        super(user);
        this.user = user;
        this.uriBuilder = uriBuilder;
    }

    public UriComponentsBuilder getUriBuilder() {
        return uriBuilder;
    }

}
