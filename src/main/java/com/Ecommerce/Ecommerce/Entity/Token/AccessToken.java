package com.Ecommerce.Ecommerce.Entity.Token;

import com.Ecommerce.Ecommerce.Entity.User;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class AccessToken
{
    @Id
    @SequenceGenerator(name = "access_token_sequence", sequenceName = "access_token_sequence",initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "access_token_sequence")
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;


    @OneToOne(fetch =FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
