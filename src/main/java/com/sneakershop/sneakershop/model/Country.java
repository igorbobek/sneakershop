package com.sneakershop.sneakershop.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "country")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "country",cascade = CascadeType.ALL)
    private Set<Sneaker> sneakers = new HashSet<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Sneaker> getSneakers() {
        return sneakers;
    }

    public void setSneakers(Set<Sneaker> sneakers) {
        this.sneakers = sneakers;
    }
}
