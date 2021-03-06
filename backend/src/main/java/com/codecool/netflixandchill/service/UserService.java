package com.codecool.netflixandchill.service;

import com.codecool.netflixandchill.dto.UserDTO;
import com.codecool.netflixandchill.dto.WatchedDTO;
import com.codecool.netflixandchill.model.*;
import com.codecool.netflixandchill.repository.EpisodeRepository;
import com.codecool.netflixandchill.repository.SeasonRepository;
import com.codecool.netflixandchill.repository.SeriesRepository;
import com.codecool.netflixandchill.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public boolean handleJoin(Map<String, String> requestJson) {
        if (this.checkIfUsernameAlreadyExists(requestJson.get("username")) ||
                this.checkIfEmailAlreadyExists(requestJson.get("email"))) return false;

        this.addUser(requestJson.get("username"),
                requestJson.get("email"),
                this.bCryptPasswordEncoder.encode(requestJson.get("password")));
        return true;
    }

    boolean checkIfUsernameAlreadyExists(String userName) {
        return userRepository.findByUsername(userName) != null;
    }

    boolean checkIfEmailAlreadyExists(String email) {
        return userRepository.findUserByEmailAddress(email) != null;
    }

    private void addUser(String username, String email, String hashedPassword) {
        User user = User.builder()
                .username(username)
                .emailAddress(email)
                .password(hashedPassword)
                .registrationDate(new Date())
                .build();
        this.userRepository.save(user);
    }

    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public UserDTO createUpdatedUser(String username) {
        User user = this.findByUsername(username);

        return UserDTO.builder()
                .username(user.getUsername())
                .emailAddress(user.getEmailAddress())
                .timeWasted(user.getTimeWasted())
                .registrationDate(user.getRegistrationDate())
                .watchlist(user.getWatchlist())
                .favourites(user.getFavourites())
                .watchedEpisodesSeries(this.getWatchedEpisodesWithSeriesForUser(username).getWatchedSeries())
                .build();
    }

    public List<Series> getWatchlistForUser(String username) {
        return (List<Series>) this.findByUsername(username).getWatchlist();
    }

    public List<Series> getFavouritesForUser(String username) {
        return (List<Series>) this.findByUsername(username).getFavourites();
    }

    public List<Episode> getWatchedEpisodesForUser(String username) {
        return (List<Episode>) this.findByUsername(username).getWatchedEpisodes();
    }

    private List<Season> getWatchedEpisodesSeasons(List<Episode> episodes) {
        return episodes.stream()
                .map(Episode::getSeason)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Series> getWatchedEpisodesSeries(List<Episode> episodes) {
        return episodes.stream()
                .map(episode -> episode.getSeason().getSeries())
                .distinct()
                .collect(Collectors.toList());
    }

    public WatchedDTO getWatchedEpisodesWithSeriesForUser(String username) {
        List<Episode> episodes = this.getWatchedEpisodesForUser(username);
        List<Season> seasons = this.getWatchedEpisodesSeasons(episodes);
        List<Series> series = this.getWatchedEpisodesSeries(episodes);

        series.forEach(currentSeries -> currentSeries.setSeasons(
                seasons.stream()
                        .filter(season -> season.getSeries().getId().equals(currentSeries.getId()))
                        .collect(Collectors.toList())
        ));
        series.forEach(currentSeries -> currentSeries.getSeasons()
                .forEach(currentSeason -> currentSeason.setEpisodes(
                        episodes.stream()
                                .filter(episode -> episode.getSeason().getId().equals(currentSeason.getId()))
                                .collect(Collectors.toList())
                )));

        return new WatchedDTO(episodes, seasons, series);
    }

    public boolean toggleEpisodeInWatched(String username, Long episodeId) {
        User user = this.userRepository.findByUsername(username);
        Optional<Episode> episodeOptional = this.episodeRepository.findById(episodeId);

        if (user == null || !episodeOptional.isPresent()) return false;

        Episode episodeToToggle = episodeOptional.get();

        if (user.getWatchedEpisodes().contains(episodeToToggle)) user.removeEpisodeFromWatched(episodeToToggle);
        else user.addEpisodeToWatched(episodeToToggle);

        this.userRepository.save(user);
        return true;
    }

    public boolean toggleSeasonInWatched(String username, Long seasonId) {
        User user = this.userRepository.findByUsername(username);
        Optional<Season> seasonOptional = this.seasonRepository.findById(seasonId);

        if (user == null || !seasonOptional.isPresent()) return false;

        Season season = seasonOptional.get();

        if (user.getWatchedEpisodes().containsAll(season.getEpisodes())) user.removeSeasonFromWatched(season);
        else user.addSeasonToWatched(season);

        this.userRepository.save(user);
        return true;
    }

    public boolean toggleSeriesInWatched(String username, Long seriesId) {
        User user = this.userRepository.findByUsername(username);
        Optional<Series> seriesOptional = this.seriesRepository.findById(seriesId);

        if (user == null || !seriesOptional.isPresent()) return false;

        Series series = seriesOptional.get();

        if (user.getWatchedEpisodes().containsAll(series.getAllEpisodes())) user.removeSeriesFromWatched(series);
        else user.addSeriesToWatched(series);

        this.userRepository.save(user);
        return true;
    }

    public boolean toggleSeriesInFavourites(String username, Long seriesId) {
        User user = this.userRepository.findByUsername(username);
        Optional<Series> seriesOptional = this.seriesRepository.findById(seriesId);

        if (user == null || !seriesOptional.isPresent()) return false;

        Series series = seriesOptional.get();

        if (user.getFavourites().contains(series)) user.removeSeriesFromFavourites(series);
        else user.addSeriesToFavourites(series);

        this.userRepository.save(user);
        return true;
    }

    public boolean toggleSeriesInWatchlist(String username, Long seriesId) {
        User user = this.userRepository.findByUsername(username);
        Optional<Series> seriesOptional = this.seriesRepository.findById(seriesId);

        if (user == null || !seriesOptional.isPresent()) return false;

        Series series = seriesOptional.get();

        if (user.getWatchlist().contains(series)) user.removeSeriesFromWatchlist(series);
        else user.addSeriesToWatchlist(series);

        this.userRepository.save(user);
        return true;
    }

    public List<Series> getRecommendedSeries(String username, Genre genreName) {
        List<Series> usersWatchedSeries = this.getWatchedEpisodesWithSeriesForUser(username).getWatchedSeries();
        List<Series> allRecommendedSeries = this.seriesRepository.findByGenresContaining(genreName);

        Collections.shuffle(allRecommendedSeries);

        return allRecommendedSeries.stream()
                .filter(series -> !usersWatchedSeries.contains(series))
                .limit(5)
                .collect(Collectors.toList());
    }

}
