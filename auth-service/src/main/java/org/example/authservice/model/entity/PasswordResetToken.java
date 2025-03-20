package org.example.authservice.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import java.time.Instant;

@Entity(name = "PASSWORD_RESET_TOKEN")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class PasswordResetToken {

    @Id
    @Column(name = "TOKEN_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TOKEN_NAME")
    @NaturalId
    private String token;

    @Column(name = "CREATED_DATE")
    private Instant CreatedDate;

    @Column(name = "EXPIRTY_DATE")
    private Instant expiryDate;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean active;

    @Column(name = "IS_CLAIMED", nullable = false)
    private Boolean claimed;
    
    @OneToOne(targetEntity = User.class,fetch = FetchType.EAGER)
    @JoinColumn(nullable = false,name = "USER_ID")
    private User user;
}
