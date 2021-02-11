package pl.bak.businessallocationapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import pl.bak.businessallocationapp.model.SeniorityLevel;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class SkillDto {
    @NotBlank
    private String nameSkill;

    private SeniorityLevel seniorityLevel;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String description;

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
        SkillDto skillDto = (SkillDto) o;
        return Objects.equals(nameSkill, skillDto.nameSkill) && Objects.equals(seniorityLevel, skillDto.seniorityLevel) && Objects.equals(description, skillDto.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameSkill, seniorityLevel, description);
    }
}
