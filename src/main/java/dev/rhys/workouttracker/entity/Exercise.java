package dev.rhys.workouttracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "exercise")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "exercise_category", nullable = false)
    private ExerciseCategory exerciseCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_muscle", nullable = false)
    private TargetMuscle targetMuscle;

    protected Exercise() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExerciseCategory getExerciseCategory() {
        return exerciseCategory;
    }

    public void setExerciseCategory(ExerciseCategory exerciseCategory) {
        this.exerciseCategory = exerciseCategory;
    }

    public TargetMuscle getTargetMuscle() {
        return targetMuscle;
    }

    public void setTargetMuscle(TargetMuscle targetMuscle) {
        this.targetMuscle = targetMuscle;
    }
}
