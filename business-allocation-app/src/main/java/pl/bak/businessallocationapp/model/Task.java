package pl.bak.businessallocationapp.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Task")
@Table(
        name = "task",
        uniqueConstraints = {
        @UniqueConstraint(name = "task_task_name_unique", columnNames = "task_name")
}
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

    @Column(
            name = "task_is_completed",
            columnDefinition = "BOOLEAN"
    )
    private boolean taskIsCompleted;

    @Column(
            name = "task_status",
            columnDefinition = "BOOLEAN"
    )
    private boolean taskStatus;

    @ManyToMany(
            mappedBy = "tasks",
            cascade = CascadeType.MERGE
    )
    private Set<User> users = new HashSet<>();


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

    public boolean isTaskIsCompleted() {
        return taskIsCompleted;
    }

    public void setTaskIsCompleted(boolean taskIsCompleted) {
        this.taskIsCompleted = taskIsCompleted;
    }

    public boolean isTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(boolean taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}
