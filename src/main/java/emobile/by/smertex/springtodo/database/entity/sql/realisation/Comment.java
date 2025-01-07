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
public class Comment implements BaseEntity<UUID> {

    private UUID id;

    private Task task;

    private String content;

    private LocalDateTime createdAt;

    private User createdBy;
}
