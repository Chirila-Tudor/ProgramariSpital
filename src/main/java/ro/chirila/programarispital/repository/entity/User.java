package ro.chirila.programarispital.repository.entity;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Table(name = "person")
public class User {

    //region Constructors
    public User(Long id, String username, String password, Boolean hasPassword) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.hasPassword = hasPassword;
    }
    public User(){}
    //endregion

    //region Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(Boolean hasPassword) {
        this.hasPassword = hasPassword;
    }
    //endregion

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @UniqueElements
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "has_password")
    private Boolean hasPassword;


}
