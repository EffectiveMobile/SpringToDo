package emobile.by.smertex.springtodo.database.entity.sql.realisation;

import emobile.by.smertex.springtodo.database.entity.sql.interfaces.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "createdBy")
@Builder
@Entity
@Table(name = "comment")
public class Comment implements BaseEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) && Objects.equals(task, comment.task) && Objects.equals(content, comment.content) && Objects.equals(createdAt, comment.createdAt) && Objects.equals(createdBy, comment.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, task, content, createdAt, createdBy);
    }
}
