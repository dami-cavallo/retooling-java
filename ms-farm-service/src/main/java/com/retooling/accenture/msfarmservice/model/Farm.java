package com.retooling.accenture.msfarmservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity(name = "farms")
public class Farm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "FARM_NAME")
    private String name;
    @Column(name = "CAPACITY")
    private int capacity;
    @Column(name = "MONEY")
    private double money;
    @ManyToOne
    @JoinColumn(name = "FK_FARMER")
    private Farmer farmer;
    @Column(name = "CANT_CHICKENS")
    private int cantChickens;
    @Column(name = "CANT_EGGS")
    private int cantEggs;

    @OneToMany(mappedBy = "farm")
    @JsonManagedReference
    private List<Chicken> chickens;

    @OneToMany(mappedBy = "farm")
    @JsonManagedReference
    private List<Egg> eggs;
    @Column(name = "CAPACIDAD_DISPONIBLE")
    private int capacidadDisponible;



    protected Farm(){

    }
    public Farm(String name, int capacity, double money, Farmer farmer, int cantChickens, int cantEggs) {
        this.name = name;
        this.capacity = capacity;
        this.money = money;
        this.farmer = farmer;
        this.cantChickens = cantChickens;
        this.cantEggs = cantEggs;
        this.capacidadDisponible = this.capacity - (this.cantChickens + this.cantEggs);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    public int getCantChickens() {
        return cantChickens;
    }

    public void setCantChickens(int cantChickens) {
        this.cantChickens = cantChickens;
    }

    public int getCantEggs() {
        return cantEggs;
    }

    public void setCantEggs(int cantEggs) {
        this.cantEggs = cantEggs;
    }


    public List<Chicken> getChickens() {
        return chickens;
    }

    public void setChickens(List<Chicken> chickens) {
        this.chickens = chickens;
    }

    public List<Egg> getEggs() {
        return eggs;
    }

    public void setEggs(List<Egg> eggs) {
        this.eggs = eggs;
    }

    public int getCapacidadDisponible() {
        return capacidadDisponible;
    }

    public void setCapacidadDisponible() {
        this.capacidadDisponible = this.capacity - (this.cantChickens + this.cantEggs);
    }
}
