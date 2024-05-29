package com.example.mentalhealthsystem.Database;

import com.example.mentalhealthsystem.constants.Language;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Data
@SuperBuilder
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User {
    public User(){}
    @Id
    String email;

    @Column(name = "name")
    String name;

    @Column(name = "dateOfBirth")
    String dateOfBirth;

    @Column(name = "nationality")
    String nationality;

    @Column(name = "phoneNumber")
    String phoneNumber;

    @Column(name = "address")
    String address;

    @Column(name = "gender")
    String gender;

    @Column(name = "age")
    int age;

    @Column(name = "country")
    String country;

    @Column(name = "city")
    String city;

    @Column(name = "language")
    Set<Language> language;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Assessment> assessments;
}
