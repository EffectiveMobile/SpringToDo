package emobile.by.smertex.springtodo.database.entity.realisation;

import emobile.by.smertex.springtodo.database.entity.interfaces.BaseEntity;
import emobile.by.smertex.springtodo.database.entity.realisation.enums.Priority;
import emobile.by.smertex.springtodo.database.entity.realisation.enums.Status;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = {"metainfo", "performer"})
@Builder
public class Task implements BaseEntity<UUID> {

    private UUID id;

    private String name;

    private Status status;

    private String description;

    private Priority priority;

    private Metainfo metainfo;

    private User performer;
}
