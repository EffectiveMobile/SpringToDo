package emobile.by.smertex.springtodo.database.entity.sql.realisation;

import emobile.by.smertex.springtodo.database.entity.sql.interfaces.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "createdBy")
@Builder
@Setter
@Getter
@Entity
@Table(name = "metainfo")
public class Metainfo implements BaseEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metainfo metainfo = (Metainfo) o;
        return Objects.equals(id, metainfo.id) && Objects.equals(createdAt, metainfo.createdAt) && Objects.equals(updatedAt, metainfo.updatedAt) && Objects.equals(createdBy, metainfo.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, updatedAt, createdBy);
    }
}
