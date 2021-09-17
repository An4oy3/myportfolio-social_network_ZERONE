package ru.skillbox.socialnetwork.model.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "post_file")
public class PostFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    private String name;

    private String path;


}
