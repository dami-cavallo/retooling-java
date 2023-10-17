package com.retooling.accenture.msspringsecurity.model;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity(name="users")
public class User {

    protected User(){

    }
    public User(Integer id, String firstName, String lastName, String email, Role role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "FIRST_NAME")
    @NotBlank(message = "el nombre no puede ser vacío")
    private String firstName;

    @Column(name = "LAST_NAME")
    @NotBlank(message = "el apellido no puede ser vacío")
    private String lastName;

    @Column(name = "EMAIL")
    @Email(message = "El mail ingresado no es valido")
    private String email;


    @Column(name = "USER_PASS")
    @Size(min=8,message = "La password tiene que tener al menos 8 caracteres")
    private String userPass;

    @ManyToOne
    @JoinColumn(name = "FK_ROLE_ID")
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
