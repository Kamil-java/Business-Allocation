package pl.bak.businessallocationapp.model;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "Skill")
@Table(
        name = "skill"
)
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "name_skill",
            columnDefinition = "TEXT"
    )
    private String nameSkill;

    @Column(
            name = "seniority_level"
    )
    @Enumerated(EnumType.STRING)
    private SeniorityLevel seniorityLevel;

    @Column(
            name = "description",
            columnDefinition = "TEXT"
    )
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameSkill() {
        return nameSkill;
    }

    public void setNameSkill(String nameSkill) {
        this.nameSkill = nameSkill;
    }

    public SeniorityLevel getSeniorityLevel() {
        return seniorityLevel;
    }

    public void setSeniorityLevel(SeniorityLevel seniorityLevel) {
        this.seniorityLevel = seniorityLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return Objects.equals(id, skill.id) && Objects.equals(nameSkill, skill.nameSkill) && Objects.equals(seniorityLevel, skill.seniorityLevel) && Objects.equals(description, skill.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameSkill, seniorityLevel, description);
    }
}
