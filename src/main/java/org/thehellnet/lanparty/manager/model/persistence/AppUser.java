package org.thehellnet.lanparty.manager.model.persistence;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.thehellnet.lanparty.manager.model.constant.Role;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "appuser")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AppUser extends AbstractEntity<AppUser> {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq")
    private Long Id;

    @Basic
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Basic
    @Column(name = "password", nullable = false)
    private String password;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "barcode", unique = true)
    private String barcode;

    @JsonIgnore
    @OneToMany(mappedBy = "appUser", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<AppUserToken> appUserTokens = new HashSet<>();

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "appuser_role", joinColumns = @JoinColumn(name = "appuser_id"))
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Role> appUserRoles = new HashSet<>();

    public AppUser() {
    }

    public AppUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public AppUser(String email, String password, String name) {
        this(email, password);
        this.name = name;
    }

    public AppUser(String email, String password, String name, Set<Role> appUserRoles, String barcode) {
        this(email, password, name);
        this.appUserRoles = appUserRoles;
        this.barcode = barcode;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        this.Id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Set<AppUserToken> getAppUserTokens() {
        return appUserTokens;
    }

    public void setAppUserTokens(Set<AppUserToken> appUserTokens) {
        this.appUserTokens = appUserTokens;
    }

    public Set<Role> getAppUserRoles() {
        return appUserRoles;
    }

    public void setAppUserRoles(Set<Role> appUserRoles) {
        this.appUserRoles = appUserRoles;
    }

    @Override
    public void updateFromEntity(AppUser dto) {
        email = dto.email;
        password = dto.password;
        name = dto.name;
        barcode = dto.barcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return Id.equals(appUser.Id) &&
                email.equals(appUser.email) &&
                password.equals(appUser.password) &&
                Objects.equals(name, appUser.name) &&
                Objects.equals(barcode, appUser.barcode) &&
                appUserTokens.equals(appUser.appUserTokens) &&
                appUserRoles.equals(appUser.appUserRoles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }

    @Override
    public String toString() {
        return name != null && name.length() > 0 ? name : email;
    }
}
