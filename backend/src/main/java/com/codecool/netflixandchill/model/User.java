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
    private long id;

    @Column(unique = true, nullable = false, name = "user_name")
    private String userName;

    @Column(unique = true, nullable = false, name = "email_address")
    private String emailAddress;

    @Column(nullable = false)
    private String password;

    @Column(name = "time_wasted", nullable = false)
    private int timeWasted;

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

    @Column(name = "registration_date")
    @Temporal(TemporalType.DATE)
    private Date registrationDate;


    @Builder
    public User(String userName, String emailAddress, String password, Collection<Series> watchlist,
                Collection<Series> favourites, Collection<Episode> watchedEpisodes, Date registrationDate) {
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.watchlist = watchlist;
        this.favourites = favourites;
        this.watchedEpisodes = watchedEpisodes;
        this.registrationDate = registrationDate;
    }

    public void addWatchedEpisodes(Episode episode) {
        watchedEpisodes.add(episode);
        episode.addUser(this);
    }

    public void addSeriesToFavouriteList(Series series) {
        favourites.add(series);
    }

    public void addSeriesToWatchList(Series series) {
        watchlist.add(series);
    }

    public void removeFromWatchedEpisode(Episode episode) {
        watchedEpisodes.remove(episode);
    }

    public void removeFromFavouriteList(Series series) {
        favourites.remove(series);
    }

    public void removeFromWatchList(Series series) {
        watchlist.remove(series);
    }

}