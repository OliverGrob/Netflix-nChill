package com.codecool.netflixandchill.controller;


import com.codecool.netflixandchill.model.Episode;
import com.codecool.netflixandchill.model.Series;
import com.codecool.netflixandchill.model.Status;
import com.codecool.netflixandchill.model.User;
import com.codecool.netflixandchill.service.EpisodeService;
import com.codecool.netflixandchill.service.SeasonService;
import com.codecool.netflixandchill.service.SeriesService;
import com.codecool.netflixandchill.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SeriesService seriesService;

    @MockBean
    private SeasonService seasonService;

    @MockBean
    private EpisodeService episodeService;

    @MockBean
    private UserService service;

    @Test
    @WithMockUser(username = "tivadar", password = "tivadar", roles = "USER")
    void testGetUserDetails() throws Exception {
        User tivadar = new User("tivadar", "tivadar", "tivadar", null, null, null, new Date());

        Mockito.when(service.findByUsername(tivadar.getUsername()))
                .thenReturn(tivadar);

        mvc.perform(get("/users/{username}", tivadar.getUsername()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }


    @Test
    @WithMockUser(username = "tivadar", password = "tivadar", roles = "USER")
    void testGetWatchListForUser() throws Exception {
        List<Series> watchList = new ArrayList<>();
        Series series = new Series("title", "image.jpg", "trailer", "description", Status.ENDED, new Date(), null);
        watchList.add(series);
        User tivadar = new User("tivadar", "tivadar", "tivadar", watchList, null, null, new Date());

        Mockito.when(service.getWatchlistForUser(tivadar.getUsername()))
                .thenReturn(watchList);

        mvc.perform(get("/users/{username}/watchlist", tivadar.getUsername()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)));
    }


    @Test
    @WithMockUser(username = "tivadar", password = "tivadar", roles = "USER")
    void testGetFavouriteListForUser() throws Exception {
        List<Series> favouriteList = new ArrayList<>();
        Series series = new Series("title", "image.jpg", "trailer", "description", Status.ENDED, new Date(), null);
        favouriteList.add(series);
        User tivadar = new User("tivadar", "tivadar", "tivadar", null, favouriteList, null, new Date());

        Mockito.when(service.getFavouritesForUser(tivadar.getUsername()))
                .thenReturn(favouriteList);

        mvc.perform(get("/users/{username}/favourites", tivadar.getUsername()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)));
    }


    @Test
    @WithMockUser(username = "tivadar", password = "tivadar", roles = "USER")
    void testGetWatchedEpisodesForUser() throws Exception {
        List<Episode> watchedEpisodes = new ArrayList<>();
        Episode epsiodes = new Episode("title", new Date(), 120, 1);
        watchedEpisodes.add(epsiodes);
        User tivadar = new User("tivadar", "tivadar", "tivadar", null, null, watchedEpisodes, new Date());

        Mockito.when(service.getWatchedEpisodesForUser(tivadar.getUsername()))
                .thenReturn(watchedEpisodes);

        mvc.perform(get("/users/{username}/already-watched", tivadar.getUsername()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "tivadar", password = "tivadar", roles = "USER")
    void testAddEpisodeToWatched() throws Exception {
        List<Episode> watchedEpisodes = new ArrayList<>();
        Episode epsiode = new Episode("title", new Date(), 120, 1);
        watchedEpisodes.add(epsiode);
        User tivadar = new User("tivadar", "tivadar", "tivadar", null, null, null, new Date());

        Mockito.when(service.getWatchedEpisodesForUser(tivadar.getUsername()))
                .thenReturn(watchedEpisodes);

        mvc.perform(put("/users/{username}/add-episode-to-watched/episode/{id}", tivadar.getUsername(), epsiode.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

}
