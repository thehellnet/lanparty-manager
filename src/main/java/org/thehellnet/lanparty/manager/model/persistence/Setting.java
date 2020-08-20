package org.thehellnet.lanparty.manager.model.persistence;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.rest.core.annotation.Description;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "setting")
@Description("Server settings")
public class Setting extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "setting_id_seq")
    @SequenceGenerator(name = "setting_id_seq", sequenceName = "setting_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('setting_id_seq')")
    @Description("Primary key")
    private Long id;

    @Basic
    @Column(name = "param", nullable = false, unique = true)
    @Description("Param")
    private String param;

    @Basic
    @Column(name = "value", nullable = false)
    @Description("Value")
    private String value;

    public Setting() {
        super();
    }

    public Setting(String param) {
        super();

        this.param = param;
    }

    public Setting(String param, String value) {
        this(param);

        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Setting setting = (Setting) o;
        return id.equals(setting.id) &&
                param.equals(setting.param) &&
                value.equals(setting.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        return String.format("%s: %s", param, value);
    }
}
