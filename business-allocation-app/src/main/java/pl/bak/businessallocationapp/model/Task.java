package pl.bak.businessallocationapp.model;

import javax.persistence.*;
import java.net.URL;
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
            name = "ready_to_be_checked",
            columnDefinition = "BOOLEAN"
    )
    private boolean readyToBeChecked = false;

    @Column(
            name = "is_completed",
            columnDefinition = "BOOLEAN"
    )
    private boolean isCompleted = true;

    @Column(
            name = "url_to_project",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private URL workEffectRepository;

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

    public boolean isReadyToBeChecked() {
        return readyToBeChecked;
    }

    public void setReadyToBeChecked(boolean taskIsCompleted) {
        this.readyToBeChecked = taskIsCompleted;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public URL getWorkEffectRepository() {
        return workEffectRepository;
    }

    public void setWorkEffectRepository(URL workEffectRepository) {
        this.workEffectRepository = workEffectRepository;
    }
}
