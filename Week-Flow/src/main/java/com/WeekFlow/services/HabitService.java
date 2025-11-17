package com.WeekFlow.services;

import com.WeekFlow.entities.DTOS.HabitRequestDTO;
import com.WeekFlow.entities.DTOS.HabitResposeDTO;
import com.WeekFlow.entities.Habit;
import com.WeekFlow.entities.User;
import com.WeekFlow.exceptions.IsNotYoursException;
import com.WeekFlow.exceptions.NotFoundException;
import com.WeekFlow.repositories.HabitRepository;
import com.WeekFlow.repositories.UserRepository;
import com.WeekFlow.security.TokenService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitService {

    private final HabitRepository habitRepository;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public HabitService(HabitRepository habitRepository, TokenService tokenService, UserRepository userRepository) {
        this.habitRepository = habitRepository;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    public List<HabitResposeDTO> getAllHabits(String authHeader){
        User user = findUserByToken(authHeader);
        List<Habit> allHabits = user.getHabits();
        return allHabits.stream().map(HabitResposeDTO::new).toList();
    }

    public HabitResposeDTO getHabitById(String authHeader, Long id){
        User user = findUserByToken(authHeader);
        Habit habit = habitRepository.findById(id).orElseThrow(() -> new NotFoundException("Not exist habit with this ID"));
        if(!user.getHabits().contains(habit)) throw new IsNotYoursException("This habit is not your's");
        return new HabitResposeDTO(habit);
    }

    public HabitResposeDTO createHabit(String authHeader, HabitRequestDTO habit){
        User user = findUserByToken(authHeader);
        Habit newHabit = new Habit(habit);
        newHabit.setUser(user);
        habitRepository.save(newHabit);
        return new HabitResposeDTO(newHabit);
    }

    public HabitResposeDTO updateHabit(String authHeader, HabitRequestDTO habit, Long id){
        User user = findUserByToken(authHeader);
        Habit updatedHabit = habitRepository.findById(id).orElseThrow(() -> new NotFoundException("Not exist habit with this ID"));
        if(!user.getHabits().contains(updatedHabit)) throw new IsNotYoursException("This habit is not your's");
        BeanUtils.copyProperties(updatedHabit, habit, "id");
        habitRepository.save(updatedHabit);
        return new HabitResposeDTO(updatedHabit);
    }

    public void deleteHabit(Long id){
        if(habitRepository.existsById(id)){
            habitRepository.deleteById(id);
        }else{
            throw new NotFoundException("Not exist habit with this ID");
        }
    }


    public User findUserByToken(String authHeader){
        String token = authHeader.replace("Bearer ", "");
        if(token.isEmpty()) throw new JWTVerificationException("null token");
        String email = tokenService.validateToken(token);
        return userRepository.findUserByEmail(email);
    }
}
