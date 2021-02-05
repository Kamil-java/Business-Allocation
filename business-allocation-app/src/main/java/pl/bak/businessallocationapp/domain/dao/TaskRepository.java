package pl.bak.businessallocationapp.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.bak.businessallocationapp.model.Task;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.readyToBeChecked= :flag")
    List<Task> findAllTaskToBeCheckFlag(boolean flag);

    Optional<Task> findTaskByTaskName(String taskName);

    @Query("SELECT t FROM Task t WHERE t.isCompleted= :flag")
    List<Task> findAllByStatusIsCompleted(boolean flag);


}
