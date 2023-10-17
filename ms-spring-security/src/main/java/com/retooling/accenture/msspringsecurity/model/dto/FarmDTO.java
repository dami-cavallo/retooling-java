package com.retooling.accenture.msspringsecurity.model.dto;

public class FarmDTO {

    private String name;
    private int capacity;
    private int money;

    protected FarmDTO(){
        
    }

    public FarmDTO(String name, int capacity, int money) {
        this.name = name;
        this.capacity = capacity;
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
