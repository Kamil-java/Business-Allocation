package pl.bak.businessallocationapp.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.bak.businessallocationapp.model.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {
}
