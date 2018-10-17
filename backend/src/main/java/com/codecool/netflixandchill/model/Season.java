package com.codecool.netflixandchill.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "season", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @Column(nullable = false)
    @ToString.Exclude
    private List<Episode> episodes = new ArrayList<>();

    @Temporal(TemporalType.DATE)
    private Date year;

    @Column(nullable = false)
    private int seasonNumber;

    @ManyToOne
    private Series series;

    @Builder
    public Season(String title, Date year, int seasonNumber) {
        this.title = title;
        this.year = year;
        this.seasonNumber = seasonNumber;
    }

    public void addEpisode(Episode episode) {
        episodes.add(episode);
        episode.setSeason(this);
    }
}
