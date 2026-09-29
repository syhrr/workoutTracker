package dev.rhys.workouttracker.exercise;

import jakarta.persistence.*;

@Entity
@Table(name = "exercise")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private targetMuscle targetMuscle;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }



}
