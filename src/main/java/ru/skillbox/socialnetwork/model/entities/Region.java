package ru.skillbox.socialnetwork.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "region")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Country country;

    private String name;

    @Column(name = "crt_date",
            columnDefinition = "TIMESTAMP with time zone NOT NULL DEFAULT NOW()")
    private LocalDateTime createDate;
}
