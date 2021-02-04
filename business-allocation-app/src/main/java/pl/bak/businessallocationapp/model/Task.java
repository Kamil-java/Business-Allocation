package pl.bak.businessallocationapp.model;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "Task")
@Table(
        name = "task"
)
public class Task {
    @Id
    @SequenceGenerator(
            name = "task_sequence",
            sequenceName = "task_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "task_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "task_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String taskName;

    @Column(
          name = "description",
          columnDefinition = "TEXT"
    )
    private String description;

    @ManyToMany(
            cascade = CascadeType.MERGE
    )
    private Set<User> user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<User> getUser() {
        return user;
    }

    public void setUser(Set<User> user) {
        this.user = user;
    }
}
