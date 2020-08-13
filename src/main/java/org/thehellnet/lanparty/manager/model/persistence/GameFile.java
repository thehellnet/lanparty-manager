package org.thehellnet.lanparty.manager.model.persistence;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "gamefile",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"game_id", "filename"})
        }
)
public class GameFile extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_file_id_seq")
    @SequenceGenerator(name = "game_file_id_seq", sequenceName = "game_file_id_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Basic
    @Column(name = "filename", nullable = false)
    private String filename;

    @Basic
    @Column(name = "required", nullable = false)
    private Boolean required = Boolean.TRUE;

    @OneToMany(mappedBy = "gameFile", cascade = CascadeType.ALL)
    private List<GameFileHash> gameFileHashes = new ArrayList<>();

    public GameFile() {
    }

    public GameFile(Game game, String filename) {
        this.game = game;
        this.filename = filename;
    }

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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public List<GameFileHash> getGameFileHashes() {
        return gameFileHashes;
    }

    public void setGameFileHashes(List<GameFileHash> gameFileHashes) {
        this.gameFileHashes = gameFileHashes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GameFile gameFile = (GameFile) o;
        return id.equals(gameFile.id) &&
                game.equals(gameFile.game) &&
                filename.equals(gameFile.filename) &&
                required.equals(gameFile.required) &&
                gameFileHashes.equals(gameFile.gameFileHashes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", game.getFriendlyName(), filename);
    }
}
