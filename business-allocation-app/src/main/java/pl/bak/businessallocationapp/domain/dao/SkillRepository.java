package pl.bak.businessallocationapp.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.bak.businessallocationapp.model.Skill;

@Repository
@Transactional(readOnly = true)
public interface SkillRepository extends JpaRepository<Skill, Long> {
}
