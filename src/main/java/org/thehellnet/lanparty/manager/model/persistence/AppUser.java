package org.thehellnet.lanparty.manager.model.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "appuser")
public class AppUser extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq")
    private Long Id;

    @Basic
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = Boolean.TRUE;

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

    @ManyToMany
    @JoinTable(name = "appuser_role",
            joinColumns = @JoinColumn(name = "appuser_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<Role> roles = new ArrayList<>();

    public AppUser() {
    }

    public AppUser(String email) {
        this.email = email;
    }

    public AppUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public AppUser(String email, String password, String name) {
        this(email, password);
        this.name = name;
    }

    public AppUser(String email, String password, String name, List<Role> roles, String barcode) {
        this(email, password, name);
        this.roles = roles;
        this.barcode = barcode;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        this.Id = id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> appUserRoles) {
        this.roles = appUserRoles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return Id.equals(appUser.Id) &&
                enabled.equals(appUser.enabled) &&
                email.equals(appUser.email) &&
                password.equals(appUser.password) &&
                Objects.equals(name, appUser.name) &&
                Objects.equals(barcode, appUser.barcode) &&
                appUserTokens.equals(appUser.appUserTokens) &&
                roles.equals(appUser.roles);
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
