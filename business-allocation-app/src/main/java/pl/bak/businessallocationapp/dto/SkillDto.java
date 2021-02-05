package pl.bak.businessallocationapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class SkillDto {
    @NotBlank
    private String nameSkill;

    @NotBlank
    private String seniorityLevel;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotBlank
    private String description;

    public String getNameSkill() {
        return nameSkill;
    }

    public void setNameSkill(String nameSkill) {
        this.nameSkill = nameSkill;
    }

    public String getSeniorityLevel() {
        return seniorityLevel;
    }

    public void setSeniorityLevel(String seniorityLevel) {
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
