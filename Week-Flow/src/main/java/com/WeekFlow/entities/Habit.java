package com.WeekFlow.entities;

import com.WeekFlow.entities.DTOS.HabitRequestDTO;
import com.WeekFlow.entities.ENUMS.Status;
import com.WeekFlow.entities.ENUMS.Week;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name= "habits")
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be null")
    private String name;

    private String description;

    private LocalTime initialTime;

    private LocalTime finalTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    private List<Week> daysWeek;

    @NotNull
    private Boolean recurrent;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate createdAt = LocalDate.now();

    public Habit(HabitRequestDTO data){
        this.name = data.name();
        this.description = data.description();
        this.initialTime = data.initialTime();
        this.finalTime = data.finalTime();
        this.daysWeek = data.daysWeek();
        this.recurrent = data.recurrent();
    }

}
