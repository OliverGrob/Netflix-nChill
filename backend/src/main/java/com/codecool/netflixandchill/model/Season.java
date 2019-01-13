package com.codecool.netflixandchill.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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
    private Long id;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "season", cascade = CascadeType.PERSIST)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Column(nullable = false)
    @ToString.Exclude
    @JsonManagedReference
    private List<Episode> episodes = new ArrayList<>();

    @Temporal(TemporalType.DATE)
    private Date year;

    @Column(nullable = false, name = "season_number")
    private int seasonNumber;

    @ManyToOne
    @JsonBackReference
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
