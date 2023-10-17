package com.retooling.accenture.msfarmservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity(name = "chickens")
public class Chicken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "FK_FARM")
    @JsonBackReference
    private Farm farm;

    @Column(name = "REMAINING_DAYS_TO_DIE")
    private int remainingDaysToDie;
    @Column(name = "REMAINING_DAYS_TO_PUT_EGGS")
    private int remainingDaysToPutEggs;

    @Column(name = "AMOUNT_EGGS_TO_PUT")
    private int amountEggsToPut;

    protected Chicken(){

    }


    public Chicken(int remainingDaysToDie, int remainingDaysToPutEggs, int amountEggsToPut) {
        this.remainingDaysToDie = remainingDaysToDie;
        this.remainingDaysToPutEggs = remainingDaysToPutEggs;
        this.amountEggsToPut = amountEggsToPut;
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

    public int getRemainingDaysToDie() {
        return remainingDaysToDie;
    }

    public void setRemainingDaysToDie(int remainingDaysToDie) {
        this.remainingDaysToDie = remainingDaysToDie;
    }

    public int getRemainingDaysToPutEggs() {
        return remainingDaysToPutEggs;
    }

    public void setRemainingDaysToPutEggs(int remainingDaysToPutEggs) {
        this.remainingDaysToPutEggs = remainingDaysToPutEggs;
    }

    public int getAmountEggsToPut() {
        return amountEggsToPut;
    }

    public void setAmountEggsToPut(int amountEggsToPut) {
        this.amountEggsToPut = amountEggsToPut;
    }
}
