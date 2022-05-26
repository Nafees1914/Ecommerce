package com.Ecommerce.Ecommerce.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@Table(name = "customer")
public class Customer extends User
{
    private String contact;

}
