package com.WeekFlow.entities;

import com.WeekFlow.entities.ENUMS.Status;
import com.WeekFlow.entities.ENUMS.Week;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name= "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be null")
    private String name;

    private String description;

    @NotNull
    private Status Status;

    private LocalTime initialTime;

    private LocalTime finalTime;

    @NotNull
    private Week daysWeek;

    @NotNull
    private Boolean recurrent;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

}
