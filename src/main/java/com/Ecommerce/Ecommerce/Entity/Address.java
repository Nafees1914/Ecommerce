package com.Ecommerce.Ecommerce.Entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Address {


    @Id
    @SequenceGenerator(name="address_sequence",sequenceName = "address_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "address_sequence")

    private Long id ;
    private String city;
    private String state;
    private String country;
    private String addressLine;
    private String zipCode;
    private String label;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", addressLine='" + addressLine + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", label='" + label + '\'' +
                ", user=" + user +
                '}';
    }
}