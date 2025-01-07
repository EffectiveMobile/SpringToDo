package emobile.by.smertex.springtodo.database.entity.sql.realisation;

import emobile.by.smertex.springtodo.database.entity.sql.interfaces.BaseEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = "createdBy")
@Builder
public class Metainfo implements BaseEntity<UUID> {

    private UUID id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private User createdBy;
}
