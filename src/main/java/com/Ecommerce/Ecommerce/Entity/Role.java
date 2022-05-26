package com.Ecommerce.Ecommerce.Entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@RequiredArgsConstructor
public class Role {

    @Id
    @SequenceGenerator(name="role_sequence",sequenceName = "role_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "role_sequence")
    private Long id;
    private String authority;

}
