package org.thehellnet.lanparty.manager.model.persistence;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.rest.core.annotation.Description;
import org.thehellnet.lanparty.manager.model.constant.FileHash;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(
        name = "game_file_hash",
        uniqueConstraints = {
                @UniqueConstraint(name = "game_file_file_hash_uniq", columnNames = {"game_file_id", "file_hash"})
        }
)
@Description("Game file hash")
public class GameFileHash extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_file_hash_id_seq")
    @SequenceGenerator(name = "game_file_hash_id_seq", sequenceName = "game_file_hash_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('game_file_hash_id_seq')")
    @Description("Primary key")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_file_id", nullable = false)
    @Description("Related file")
    private GameFile gameFile;

    @Basic
    @Column(name = "file_hash", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'SHA256'")
    @Description("Hash algorithm")
    private FileHash fileHash = FileHash.SHA256;

    @Basic
    @Column(name = "hash_value", nullable = false)
    @Description("Hash value")
    private String hashValue;

    public GameFileHash() {
    }

    public GameFileHash(GameFile gameFile, FileHash fileHash, String hashValue) {
        this.gameFile = gameFile;
        this.fileHash = fileHash;
        this.hashValue = hashValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GameFile getGameFile() {
        return gameFile;
    }

    public void setGameFile(GameFile gameFile) {
        this.gameFile = gameFile;
    }

    public FileHash getFileHash() {
        return fileHash;
    }

    public void setFileHash(FileHash fileHash) {
        this.fileHash = fileHash;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GameFileHash that = (GameFileHash) o;
        return id.equals(that.id) &&
                gameFile.equals(that.gameFile) &&
                fileHash == that.fileHash &&
                hashValue.equals(that.hashValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s - %s %s", gameFile.getFilename(), fileHash, hashValue);
    }
}
