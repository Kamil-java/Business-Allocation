package pl.bak.businessallocationapp.dto;

import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotBlank;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    private String taskName;

    private String description;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean readyToBeChecked;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean isCompleted;

    private URL workEffectRepository;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<UserDto> userDtos = new ArrayList<>();

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

    public void setReadyToBeChecked(boolean readyToBeChecked) {
        this.readyToBeChecked = readyToBeChecked;
    }

    public List<UserDto> getUserDtos() {
        return userDtos;
    }

    public void setUserDtos(List<UserDto> userDtos) {
        this.userDtos = userDtos;
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
