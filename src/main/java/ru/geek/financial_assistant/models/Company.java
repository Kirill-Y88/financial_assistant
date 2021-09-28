package ru.geek.financial_assistant.models;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "em")
    private Long em;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @OneToMany(mappedBy = "company")
    List<Index> indices;


    public Company(){

    }

    public Company(Long id, Long em, String name, String code) {
        this.id = id;
        this.em = em;
        this.name = name;
        this.code = code;
    }
}
