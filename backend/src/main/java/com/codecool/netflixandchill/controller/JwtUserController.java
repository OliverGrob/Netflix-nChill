package com.codecool.netflixandchill.controller;

import com.codecool.netflixandchill.model.Episode;
import com.codecool.netflixandchill.model.Series;
import com.codecool.netflixandchill.model.User;
import com.codecool.netflixandchill.service.UserService;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class JwtUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @PostMapping("/join")
    public ResponseEntity join(@RequestBody Map<String, String> requestJson) {
        if (userService.checkIfEmailAlreadyExists(requestJson.get("email")) || userService.checkIfUserNameAlreadyExists("username")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("email or username already exists");
        } else {
            this.userService.addUser(requestJson.get("username"),
                    requestJson.get("email"),
                    bCryptPasswordEncoder.encode(requestJson.get("password")));
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    @GetMapping("/{username}")
    public User getUserDetails(@PathVariable String username) {
        return this.userService.findByUsername(username);
    }

    @GetMapping("/{username}/watchlist")
    public List<Series> getWatchlistForUser(@PathVariable String username) {
        return this.userService.getWatchlistForUser(username);
    }

    @GetMapping("/{username}/favourites")
    public List<Series> getFavouritesForUser(@PathVariable String username) {
        return this.userService.getFavouritesForUser(username);
    }

    @GetMapping("/{username}/already-watched")
    public List<Episode> getWatchedEpisodesForUser(@PathVariable String username) {
        return this.userService.getWatchedEpisodesForUser(username);
    }

    @PutMapping("/{username}/add-episode-to-watched/episode/{id}")
    public void addEpisodeToWatched(@PathVariable String username, @PathVariable Long id) {
        this.userService.addEpisodeToWatched(username, id);
    }

    @PutMapping("/{username}/add-season-to-watched/season/{id}")
    public void addSeasonToWatched(@PathVariable String username, @PathVariable Long id) {
        this.userService.addSeasonToWatched(username, id);
    }

    @PutMapping("/{username}/add-series-to-watched/series/{id}")
    public void addSeriesToWatched(@PathVariable String username, @PathVariable Long id) {
        this.userService.addSeriesToWatched(username, id);
    }

    @PutMapping("/{username}/add-series-to-favourites/series/{id}")
    public void addSeriesToFavourites(@PathVariable String username, @PathVariable Long id) {
        this.userService.addSeriesToFavourites(username, id);
    }

    @PutMapping("/{username}/add-series-to-watchlist/series/{id}")
    public void addSeriesToWatchlist(@PathVariable String username, @PathVariable Long id) {
        this.userService.addSeriesToWatchlist(username, id);
    }
}
