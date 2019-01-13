package com.codecool.netflixandchill.model;

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
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "air_date")
    @Temporal(TemporalType.DATE)
    private Date airDate;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String trailer;

    @OneToMany(mappedBy = "series", cascade = CascadeType.PERSIST)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Column(nullable = false)
    @ToString.Exclude
    @JsonManagedReference
    private List<Season> seasons = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Genre.class)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Column(name = "genre", nullable = false)
    @ToString.Exclude
    private List<Genre> genres = new ArrayList<>();

    @Builder
    public Series(String title, String image, String trailer, String description,
                  Status status, Date airDate, List<Genre> genres) {
        this.title = title;
        this.image = image;
        this.trailer = trailer;
        this.description = description;
        this.status = status;
        this.airDate = airDate;
        this.genres = genres;
    }

    public void addSeason(Season season) {
        seasons.add(season);
        season.setSeries(this);
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public List<Episode> getAllEpisodes() {
        List<Episode> allEpisodes = new ArrayList<>();

        this.seasons.forEach(season -> allEpisodes.addAll(season.getEpisodes()));

        return allEpisodes;
    }

}
