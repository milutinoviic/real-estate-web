package com.example.realestate.model;

import com.example.realestate.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String last;

    private String phone;

    private String address;

    private String email;

    private String password;

    private boolean active;

    private Role role;


    public User() {
    }

    public User(Long id, String name, String last, String phone, String address, String email, String password, boolean active, Role role) {
        this.id = id;
        this.name = name;
        this.last = last;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.password = password;
        this.active = active;
        this.role = role;
    }

    public User(String name, String last, String phone, String address, String email, String password, boolean active, Role role) {
        this.name = name;
        this.last = last;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.password = password;
        this.active = active;
        this.role = role;
    }
}
