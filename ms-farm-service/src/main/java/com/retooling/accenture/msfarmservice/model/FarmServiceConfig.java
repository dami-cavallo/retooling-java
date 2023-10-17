package com.retooling.accenture.msfarmservice.model;

import javax.persistence.*;


public class FarmServiceConfig {
    private double purchasePriceChicken;
    private double purchasePriceEgg;
    private double sellPriceChicken;
    private double sellPriceEgg;


    private int amountDaysToPutEggs;
    private int amountEggsToPut;
    private int chickensDaysToDie;
    private int eggsDaysToBecomeChicken;

    protected FarmServiceConfig(){

    }

    public FarmServiceConfig(FarmServiceConfig farmServiceConfig){
        this.purchasePriceChicken = farmServiceConfig.getPurchasePriceChicken();
        this.purchasePriceEgg = farmServiceConfig.getPurchasePriceEgg();
        this.sellPriceChicken = farmServiceConfig.getSellPriceChicken();
        this.sellPriceEgg = farmServiceConfig.getSellPriceEgg();
        this.amountDaysToPutEggs = farmServiceConfig.getAmountDaysToPutEggs();
        this.amountEggsToPut = farmServiceConfig.getAmountEggsToPut();
        this.chickensDaysToDie = farmServiceConfig.getChickensDaysToDie();
        this.eggsDaysToBecomeChicken = farmServiceConfig.getEggsDaysToBecomeChicken();
    }

    public FarmServiceConfig(double purchasePriceChicken, double purchasePriceEgg, double sellPriceChicken, double sellPriceEgg, int amountDaysToPutEggs, int amountEggsToPut, int chickensDaysToDie, int eggsDaysToBecomeChicken) {
        this.purchasePriceChicken = purchasePriceChicken;
        this.purchasePriceEgg = purchasePriceEgg;
        this.sellPriceChicken = sellPriceChicken;
        this.sellPriceEgg = sellPriceEgg;
        this.amountDaysToPutEggs = amountDaysToPutEggs;
        this.amountEggsToPut = amountEggsToPut;
        this.chickensDaysToDie = chickensDaysToDie;
        this.eggsDaysToBecomeChicken = eggsDaysToBecomeChicken;
    }

    public double getPurchasePriceChicken() {
        return purchasePriceChicken;
    }

    public void setPurchasePriceChicken(double purchasePriceChicken) {
        this.purchasePriceChicken = purchasePriceChicken;
    }

    public double getPurchasePriceEgg() {
        return purchasePriceEgg;
    }

    public void setPurchasePriceEgg(double purchasePriceEgg) {
        this.purchasePriceEgg = purchasePriceEgg;
    }

    public double getSellPriceChicken() {
        return sellPriceChicken;
    }

    public void setSellPriceChicken(double sellPriceChicken) {
        this.sellPriceChicken = sellPriceChicken;
    }

    public double getSellPriceEgg() {
        return sellPriceEgg;
    }

    public void setSellPriceEgg(double sellPriceEgg) {
        this.sellPriceEgg = sellPriceEgg;
    }

    public int getAmountDaysToPutEggs() {
        return amountDaysToPutEggs;
    }

    public void setAmountDaysToPutEggs(int amountDaysToPutEggs) {
        this.amountDaysToPutEggs = amountDaysToPutEggs;
    }

    public int getAmountEggsToPut() {
        return amountEggsToPut;
    }

    public void setAmountEggsToPut(int amountEggsToPut) {
        this.amountEggsToPut = amountEggsToPut;
    }

    public int getChickensDaysToDie() {
        return chickensDaysToDie;
    }

    public void setChickensDaysToDie(int chickensDaysToDie) {
        this.chickensDaysToDie = chickensDaysToDie;
    }

    public int getEggsDaysToBecomeChicken() {
        return eggsDaysToBecomeChicken;
    }

    public void setEggsDaysToBecomeChicken(int eggsDaysToBecomeChicken) {
        this.eggsDaysToBecomeChicken = eggsDaysToBecomeChicken;
    }
}
