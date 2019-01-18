package com.codecool.netflixandchill.controller;

import com.codecool.netflixandchill.dto.ErrorDTO;
import com.codecool.netflixandchill.dto.UserDTO;
import com.codecool.netflixandchill.dto.WatchedDTO;
import com.codecool.netflixandchill.model.Genre;
import com.codecool.netflixandchill.model.Series;
import com.codecool.netflixandchill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/join")
    public ResponseEntity join(@RequestBody Map<String, String> requestJson) {
        if (this.userService.handleJoin(requestJson)) return new ResponseEntity(HttpStatus.OK);

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorDTO.builder()
                        .error("invalid credentials")
                        .message("This username or email already exists!")
                        .build());
    }

    @GetMapping("/{username}")
    public UserDTO getUserDetails(@PathVariable String username) {
        return this.userService.createUpdatedUser(username);
    }

    @GetMapping("/{username}/watchlist")
    public List<Series> getWatchlistForUser(@PathVariable String username) {
        return userService.getWatchlistForUser(username);
    }

    @GetMapping("/{username}/favourites")
    public List<Series> getFavouritesForUser(@PathVariable String username) {
        return this.userService.getFavouritesForUser(username);
    }

    @GetMapping("/{username}/already-watched")
    public WatchedDTO getWatchedEpisodesWithSeriesForUser(@PathVariable String username) {
        return this.userService.getWatchedEpisodesWithSeriesForUser(username);
    }

    @PutMapping("/{username}/toggle-episode-in-watched/episode/{episodeId}")
    public ResponseEntity toggleEpisodeInWatched(@PathVariable String username, @PathVariable Long episodeId) {
        if (this.userService.toggleEpisodeInWatched(username, episodeId)) return new ResponseEntity(HttpStatus.OK);

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorDTO.builder()
                        .error("invalid parameters")
                        .message("Username and/or episode's id in the URL is incorrect!")
                        .build());
    }

    @PutMapping("/{username}/toggle-season-in-watched/season/{seasonId}")
    public ResponseEntity toggleSeasonInWatched(@PathVariable String username, @PathVariable Long seasonId) {
        if (this.userService.toggleSeasonInWatched(username, seasonId)) return new ResponseEntity(HttpStatus.OK);

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorDTO.builder()
                        .error("invalid parameters")
                        .message("Username and/or season's id in the URL is incorrect!")
                        .build());
    }

    @PutMapping("/{username}/toggle-series-in-watched/series/{seriesId}")
    public ResponseEntity toggleSeriesInWatched(@PathVariable String username, @PathVariable Long seriesId) {
        if (this.userService.toggleSeriesInWatched(username, seriesId)) return new ResponseEntity(HttpStatus.OK);

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorDTO.builder()
                        .error("invalid parameters")
                        .message("Username and/or series' id in the URL is incorrect!")
                        .build());
    }

    @PutMapping("/{username}/toggle-series-in-favourites/series/{seriesId}")
    public ResponseEntity toggleSeriesInFavourites(@PathVariable String username, @PathVariable Long seriesId) {
        if (this.userService.toggleSeriesInFavourites(username, seriesId)) return new ResponseEntity(HttpStatus.OK);

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorDTO.builder()
                        .error("invalid parameters")
                        .message("Username and/or series' id in the URL is incorrect!")
                        .build());
    }

    @PutMapping("{username}/toggle-series-in-watchlist/series/{seriesId}")
    public ResponseEntity toggleSeriesInWatchlist(@PathVariable String username, @PathVariable Long seriesId) {
        if (this.userService.toggleSeriesInWatchlist(username, seriesId)) return new ResponseEntity(HttpStatus.OK);

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorDTO.builder()
                        .error("invalid parameters")
                        .message("Username and/or series' id in the URL is incorrect!")
                        .build());
    }

    @GetMapping("{username}/recommended-series")
    public ResponseEntity getRecommendedSeries(@PathVariable String username, @RequestParam Genre genre) {
        List<Series> series = this.userService.getRecommendedSeries(username, genre);

        if (series.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorDTO.builder()
                            .error("series cannot be found")
                            .message("This genre(s) cannot be found in the database!")
                            .build());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(series);
    }

}
