package com.ajcp.ud.exam.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "exams")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @JsonIgnoreProperties(value = {"exam"}, allowSetters = true)
    @OneToMany(mappedBy = "exam", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subjects;

    private boolean isEnabled;

    public Exam() {
        this.questions = new ArrayList<>();
    }

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @PrePersist
    public void prePersist() {
        this.setEnabled(true);
        this.setCreateAt(LocalDateTime.now());
    }

    @PreUpdate
    public void preUpdate() {
        this.setUpdateAt(LocalDateTime.now());
    }

    public void setQuestions(List<Question> questions) {
        this.questions.clear();
        questions.forEach(this::addQuestion);
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
        question.setExam(this);
    }

    public void removeQuestion(Question question) {
        this.questions.remove(question);
        question.setExam(null);
    }

}
