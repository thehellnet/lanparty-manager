package org.thehellnet.lanparty.manager.model.persistence;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "server_binary")
public class ServerBinary extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "server_binary_id_seq")
    @SequenceGenerator(name = "server_binary_id_seq", sequenceName = "server_binary_id_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Basic
    @Column(name = "base_directory", nullable = false, unique = true)
    private String baseDirectory = "";

    @Basic
    @Column(name = "executable", nullable = false, unique = true)
    private String executable = "";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getBaseDirectory() {
        return baseDirectory;
    }

    public void setBaseDirectory(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    public String getExecutable() {
        return executable;
    }

    public void setExecutable(String executable) {
        this.executable = executable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ServerBinary that = (ServerBinary) o;
        return id.equals(that.id) &&
                game.equals(that.game) &&
                baseDirectory.equals(that.baseDirectory) &&
                executable.equals(that.executable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", game, executable);
    }
}
