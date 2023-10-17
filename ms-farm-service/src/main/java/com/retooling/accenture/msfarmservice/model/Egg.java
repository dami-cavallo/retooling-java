package com.retooling.accenture.msfarmservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity(name = "eggs")
public class Egg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "FK_FARM")
    @JsonBackReference
    private Farm farm;

    @Column(name = "DAYS_TO_BECOME_CHICKEN")
    private int daysToBecomeChicken;

    protected Egg(){

    }

    public Egg(int daysToBecomeChicken) {
        this.daysToBecomeChicken = daysToBecomeChicken;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Farm getFarm() {
        return farm;
    }

    public void setFarm(Farm farm) {
        this.farm = farm;
    }

    public int getDaysToBecomeChicken() {
        return daysToBecomeChicken;
    }

    public void setDaysToBecomeChicken(int daysToBecomeChicken) {
        this.daysToBecomeChicken = daysToBecomeChicken;
    }
}
