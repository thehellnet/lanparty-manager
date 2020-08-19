package org.thehellnet.lanparty.manager.model.persistence;

import org.hibernate.annotations.ColumnDefault;

import org.springframework.data.rest.core.annotation.Description;
import org.thehellnet.utility.TokenUtility;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "app_user_token")
@Description("User token")
public class AppUserToken extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_token_id_seq")
    @SequenceGenerator(name = "app_user_token_id_seq", sequenceName = "app_user_token_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('app_user_token_id_seq')")
    @Description("Primary key")
    private Long id;

    @Basic
    @Column(name = "token", nullable = false, unique = true)
    @Description("Token")
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appuser_id", nullable = false)
    @Description("Related user")
    private AppUser appUser;

    @Basic
    @Column(name = "creation_datetime", nullable = false)
    @ColumnDefault("now()")
    @Description("Date & Time of creation")
    private LocalDateTime creationTs = LocalDateTime.now();

    @Basic
    @Column(name = "expiration_datetime")
    @Description("Date & Time of expiration")
    private LocalDateTime expirationTs;

    public AppUserToken() {
        expirationTs = TokenUtility.generateExpiration(creationTs);
    }

    public AppUserToken(String token, AppUser appUser) {
        this();
        this.token = token;
        this.appUser = appUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public LocalDateTime getCreationTs() {
        return creationTs;
    }

    public void setCreationTs(LocalDateTime creationDatetime) {
        this.creationTs = creationDatetime;
    }

    public LocalDateTime getExpirationTs() {
        return expirationTs;
    }

    public void setExpirationTs(LocalDateTime expirationDateTime) {
        this.expirationTs = expirationDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AppUserToken that = (AppUserToken) o;
        return id.equals(that.id) &&
                token.equals(that.token) &&
                appUser.equals(that.appUser) &&
                creationTs.equals(that.creationTs) &&
                Objects.equals(expirationTs, that.expirationTs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        return token;
    }
}
