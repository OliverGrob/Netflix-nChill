package com.codecool.netflixandchill.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false, name = "username")
    private String username;

    @Column(unique = true, nullable = false, name = "email_address")
    private String emailAddress;

    @Column(nullable = false)
    private String password;

    @Column(name = "time_wasted", nullable = false)
    private int timeWasted;

    @Column(name = "registration_date")
    @Temporal(TemporalType.DATE)
    private Date registrationDate;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "users_watchlist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "episode_id"))
    @ToString.Exclude
    private Collection<Series> watchlist = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "users_favourites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "episode_id"))
    @ToString.Exclude
    private Collection<Series> favourites = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "episode_id"))
    @ToString.Exclude
    @JsonManagedReference
    private Collection<Episode> watchedEpisodes = new ArrayList<>();


    @Builder
    public User(String username, String emailAddress, String password, Collection<Series> watchlist,
                Collection<Series> favourites, Collection<Episode> watchedEpisodes, Date registrationDate) {
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
        this.watchlist = watchlist;
        this.favourites = favourites;
        this.watchedEpisodes = watchedEpisodes;
        this.registrationDate = registrationDate;
    }

    public void addEpisodeToWatched(Episode episode) {
        this.watchedEpisodes.add(episode);
        this.timeWasted += episode.getRuntime();
        episode.addUser(this);
    }

    public void removeEpisodeFromWatched(Episode episode) {
        this.watchedEpisodes.remove(episode);
        this.timeWasted -= episode.getRuntime();
    }

    public void addSeasonToWatched(Season season) {
        season.getEpisodes().forEach(episode -> {
            if (!this.watchedEpisodes.contains(episode)) {
                this.addEpisodeToWatched(episode);
            }
        });
    }

    public void removeSeasonFromWatched(Season season) {
        season.getEpisodes().forEach(this::removeEpisodeFromWatched);
    }

    public void addSeriesToWatched(Series series) {
        series.getSeasons().forEach(this::addSeasonToWatched);
    }

    public void removeSeriesFromWatched(Series series) {
        series.getSeasons().forEach(this::removeSeasonFromWatched);
    }

    public void addSeriesToFavourites(Series series) {
        this.favourites.add(series);
    }

    public void removeSeriesFromFavourites(Series series) {
        this.favourites.remove(series);
    }

    public void addSeriesToWatchlist(Series series) {
        this.watchlist.add(series);
    }

    public void removeSeriesFromWatchlist(Series series) {
        this.watchlist.remove(series);
    }

}