package com.codecool.netflixandchill.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, name = "release_date")
    @Temporal(TemporalType.DATE)
    @ToString.Exclude
    private Date releaseDate;

    @Column(nullable = false, name = "runtime")
    private int runtime;

    @Column(nullable = false, name = "episode_number")
    private int episodeNumber;

    @ManyToOne
    @JsonBackReference
    private Season season;

    @ManyToMany(mappedBy = "watchedEpisodes")
    @LazyCollection(LazyCollectionOption.TRUE)
    @ToString.Exclude
    @JsonBackReference
    private Collection<User> users = new ArrayList<>();

    @Builder
    public Episode(String title, Date releaseDate, int runtime, int episodeNumber) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.episodeNumber = episodeNumber;
    }

    void addUser(User user) {
        users.add(user);
    }

}
