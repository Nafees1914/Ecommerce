package com.Ecommerce.Ecommerce.Entity.Token;

import com.Ecommerce.Ecommerce.Entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class PasswordResetToken
{

    @Id
    @SequenceGenerator(name="password_reset_token_sequence",sequenceName = "password_reset_token_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "password_reset_token_sequence")
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expireAt;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;


}
