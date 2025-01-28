package emobile.by.smertex.springtodo.database.entity.sql.realisation;

import emobile.by.smertex.springtodo.database.entity.sql.interfaces.BaseEntity;
import emobile.by.smertex.springtodo.database.entity.sql.realisation.enums.Priority;
import emobile.by.smertex.springtodo.database.entity.sql.realisation.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@NamedQueries(
        @NamedQuery(name = "findAllByFilterTask",
                    query = """
                            select t from Task t
                               join fetch t.performer u1
                               join Metainfo m on t.metainfo.id = m.id
                               join User u on t.performer.id = u.id
                               where (:filterCreatedByEmail is null or :filterCreatedByEmail = m.createdBy.email)
                                    and (:filterCreatedByRole is null or :filterCreatedByRole = m.createdBy.role)
                                    and (cast(cast(:filterCreatedAt as text) as timestamp) is null or :filterCreatedAt <= m.createdAt)
                                    and (cast(cast(:filterUpdatedAt as text) as timestamp) is null or :filterUpdatedAt >= m.updatedAt)
                                    and (:status is null or :status = t.status)
                                    and (:priority is null or :priority = t.priority)
                                    and (:filterPerformerEmail is null or :filterPerformerEmail = t.performer.email)
                                    and (:filterPerformerRole is null or :filterPerformerRole = t.performer.role)
                                    and (:filterName is null or t.name like concat('%', cast(:filterName as text), '%'))
                                    and (:userEmail = t.performer.email or :userIsAdmin = true)
                            """)
)
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = {"comments", "metainfo", "performer"})
@Builder
@Entity
@Table(name = "task")
@Getter
@Setter
public class Task implements BaseEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "metainfo_id")
    private Metainfo metainfo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performer_id")
    private User performer;

    @Builder.Default
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name) && status == task.status && Objects.equals(description, task.description) && priority == task.priority && Objects.equals(metainfo, task.metainfo) && Objects.equals(performer, task.performer) && Objects.equals(comments, task.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, description, priority, metainfo, performer, comments);
    }
}