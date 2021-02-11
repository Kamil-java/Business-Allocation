package pl.bak.businessallocationapp.domain.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.bak.businessallocationapp.domain.dao.SkillRepository;
import pl.bak.businessallocationapp.domain.dao.UserRepository;
import pl.bak.businessallocationapp.dto.SkillDto;
import pl.bak.businessallocationapp.dto.UserDto;
import pl.bak.businessallocationapp.model.SeniorityLevel;
import pl.bak.businessallocationapp.model.Skill;
import pl.bak.businessallocationapp.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SkillService {
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public SkillService(SkillRepository skillRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public Optional<UserDto> addSkillToUser(SkillDto skillDto, String username) {
        Skill skill = modelMapper.map(skillDto, Skill.class);
        Optional<User> user = userRepository.findUserByUsername(username);
        UserDto userDto = new UserDto();

        if (user.isPresent()) {
            User appUser = user.get();
            Set<Skill> skills = appUser.getSkills();
            for (Skill appUserSkill : skills) {
                boolean nameExist = appUserSkill.getNameSkill().equals(skill.getNameSkill());

                if (nameExist) {
                    return Optional.empty();
                }

                skills.add(skill);
                skillRepository.save(skill);
                userRepository.save(appUser);
                modelMapper.map(appUser, userDto);
            }
        }

        return Optional.of(userDto);
    }

    public Optional<SkillDto> updateDescriptionById(long id, String description) {
        Optional<Skill> skill = skillRepository.findById(id);

        if (skill.isPresent()) {
            Skill getSkill = skill.get();
            getSkill.setDescription(description);
            skillRepository.save(getSkill);
            return Optional.of(modelMapper.map(getSkill, SkillDto.class));
        }

        return Optional.empty();
    }

    public Optional<UserDto> updateSeniorityLevel(String username, long id, SeniorityLevel seniorityLevel) {
        Optional<User> userByUsername = userRepository.findUserByUsername(username);
        Optional<Skill> byId = skillRepository.findById(id);
        Optional<Skill> first = userByUsername.get().getSkills().stream()
                .filter(skill -> skill.getNameSkill().equals(byId.get().getNameSkill()))
                .findFirst();
        userByUsername.get().getSkills().remove(first.get());


        Skill skill = new Skill();
        modelMapper.map(first.get(), skill);
        skill.setSeniorityLevel(seniorityLevel);



        skill.setId(null);
        userByUsername.get().getSkills().add(skill);

        skillRepository.save(skill);
        userRepository.save(userByUsername.get());
        return Optional.of(modelMapper.map(userByUsername.get(), UserDto.class));
    }
}
