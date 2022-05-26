package com.Ecommerce.Ecommerce.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "seller")
@PrimaryKeyJoinColumn(name = "user_id")
public class Seller extends User {


    private String gstNumber;
    private String companyContact;
    private String companyName;
}
