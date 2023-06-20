package com.tyd.user.module.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "tblPassword")
public class Password implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passwordId;

    @Column(name = "password")
    private String encryptedPassword;

    @Column(name = "passwordCreationDT")
    private LocalDate passwordCreationDT;

}
