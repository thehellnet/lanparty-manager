package org.thehellnet.lanparty.manager.model.persistence;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "privilege")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Privilege extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "privilege_id_seq")
    @SequenceGenerator(name = "privilege_id_seq", sequenceName = "privilege_id_seq")
    private Long Id;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "privileges")
    @JsonIdentityReference(alwaysAsId = true)
    private List<Role> roles = new ArrayList<>();

    public Privilege() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Privilege privilege = (Privilege) o;
        return Id.equals(privilege.Id) &&
                name.equals(privilege.name) &&
                roles.equals(privilege.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }

    @Override
    public String toString() {
        return name;
    }
}
