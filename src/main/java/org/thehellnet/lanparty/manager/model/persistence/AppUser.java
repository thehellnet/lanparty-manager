package org.thehellnet.lanparty.manager.model.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.rest.core.annotation.Description;
import org.thehellnet.utility.ConfirmCodeUtility;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "app_user")
@Description("Physical user for Lan Party")
public class AppUser extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_id_seq")
    @SequenceGenerator(name = "app_user_id_seq", sequenceName = "app_user_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('app_user_id_seq')")
    @Description("Primary key")
    private Long id;

    @Basic
    @Column(name = "enabled", nullable = false)
    @ColumnDefault("FALSE")
    @Description("User enabled or not")
    private Boolean enabled = Boolean.FALSE;

    @Basic
    @Column(name = "confirm_code")
    @Description("Confirmation code")
    private String confirmCode;

    @Basic
    @Column(name = "email", nullable = false, unique = true)
    @Description("User e-mail address (used for login)")
    private String email;

    @JsonIgnore
    @Basic
    @Column(name = "password", nullable = false)
    @Description("User password")
    private String password;

    @Basic
    @Column(name = "nickname", unique = true)
    @Description("User default nickname")
    private String nickname;

    @Basic
    @Column(name = "register_ts", nullable = false)
    @ColumnDefault("now()")
    @Description("Date & time of registration")
    private LocalDateTime registerTs = LocalDateTime.now();

    @Basic
    @Column(name = "confirm_ts")
    @Description("Date & time of account confirmation")
    private LocalDateTime confirmTs;

    @Basic
    @Column(name = "last_login_ts")
    @Description("Date & time of last login")
    private LocalDateTime lastLoginTs;

    @Basic
    @Column(name = "barcode", unique = true)
    @Description("Contactless barcode")
    private String barcode;

    @JsonIgnore
    @OneToMany(mappedBy = "appUser", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @Description("User tokens")
    private Set<AppUserToken> appUserTokens = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "appuser_role",
            joinColumns = @JoinColumn(name = "appuser_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    @Description("Associated roles")
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

    public AppUser(String email, String password, String name, String nickname) {
        this(email, password, name);
        this.nickname = nickname;
    }

    public AppUser(String email, String password, String name, List<Role> roles, String barcode) {
        this(email, password, name);
        this.roles = roles;
        this.barcode = barcode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public LocalDateTime getRegisterTs() {
        return registerTs;
    }

    public void setRegisterTs(LocalDateTime registerTs) {
        this.registerTs = registerTs;
    }

    public LocalDateTime getConfirmTs() {
        return confirmTs;
    }

    public void setConfirmTs(LocalDateTime confirmTs) {
        this.confirmTs = confirmTs;
    }

    public LocalDateTime getLastLoginTs() {
        return lastLoginTs;
    }

    public void setLastLoginTs(LocalDateTime lastLoginTs) {
        this.lastLoginTs = lastLoginTs;
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

    public void updateLastLogin() {
        lastLoginTs = LocalDateTime.now();
    }

    public void generateConfirmCode() {
        confirmCode = ConfirmCodeUtility.generate();
    }

    public void confirm() {
        enabled = true;
        confirmCode = null;
        confirmTs = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AppUser appUser = (AppUser) o;
        return id.equals(appUser.id) &&
                enabled.equals(appUser.enabled) &&
                Objects.equals(confirmCode, appUser.confirmCode) &&
                email.equals(appUser.email) &&
                password.equals(appUser.password) &&
                Objects.equals(nickname, appUser.nickname) &&
                registerTs.equals(appUser.registerTs) &&
                Objects.equals(confirmTs, appUser.confirmTs) &&
                Objects.equals(lastLoginTs, appUser.lastLoginTs) &&
                Objects.equals(barcode, appUser.barcode) &&
                appUserTokens.equals(appUser.appUserTokens) &&
                roles.equals(appUser.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        return name.length() > 0 ? name : email;
    }
}
