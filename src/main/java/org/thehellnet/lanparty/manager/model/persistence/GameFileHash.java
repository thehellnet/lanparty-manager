package org.thehellnet.lanparty.manager.model.persistence;

import org.thehellnet.lanparty.manager.model.constant.FileHash;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(
        name = "gamefilehash",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"gamefile_id", "filehash"})
        }
)
public class GameFileHash extends AbstractEntity {

    @javax.persistence.Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_file_hash_id_seq")
    @SequenceGenerator(name = "game_file_hash_id_seq", sequenceName = "game_file_hash_id_seq")
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gamefile_id", nullable = false)
    private GameFile gameFile;

    @Basic
    @Column(name = "filehash", nullable = false)
    @Enumerated(EnumType.STRING)
    private FileHash fileHash = FileHash.SHA256;

    @Basic
    @Column(name = "hash_value", nullable = false)
    private String hashValue;

    public GameFileHash() {
    }

    public GameFileHash(GameFile gameFile, FileHash fileHash, String hashValue) {
        this.gameFile = gameFile;
        this.fileHash = fileHash;
        this.hashValue = hashValue;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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
        GameFileHash that = (GameFileHash) o;
        return Id.equals(that.Id) &&
                gameFile.equals(that.gameFile) &&
                fileHash == that.fileHash &&
                hashValue.equals(that.hashValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }

    @Override
    public String toString() {
        return String.format("%s - %s %s", gameFile.getFilename(), fileHash, hashValue);
    }
}
