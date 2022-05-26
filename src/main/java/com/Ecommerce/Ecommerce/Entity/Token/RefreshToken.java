package com.Ecommerce.Ecommerce.Entity.Token;

import com.Ecommerce.Ecommerce.Entity.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Data
public class RefreshToken {

    @Id
    @SequenceGenerator(name="refresh_token_sequence",sequenceName = "refresh_token_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "refresh_token_sequence")
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime expireAt;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}
