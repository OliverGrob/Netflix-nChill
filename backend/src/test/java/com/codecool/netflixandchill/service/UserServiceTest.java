package com.codecool.netflixandchill.service;

import com.codecool.netflixandchill.model.Episode;
import com.codecool.netflixandchill.model.Series;
import com.codecool.netflixandchill.model.Status;
import com.codecool.netflixandchill.model.User;
import com.codecool.netflixandchill.repository.EpisodeRepository;
import com.codecool.netflixandchill.repository.SeasonRepository;
import com.codecool.netflixandchill.repository.SeriesRepository;
import com.codecool.netflixandchill.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @Configuration
    static class UserServiceTestContextConfiguration {

        @Bean
        public UserService userService() {
            return new UserService();
        }
    }

    @Autowired
    private UserService userService;

    @MockBean
    private EpisodeRepository episodeRepository;

    @MockBean
    private SeasonRepository seasonRepository;

    @MockBean
    private SeriesRepository seriesRepository;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        List<Series> watchList = new ArrayList<>();
        Series watchListSeries = new Series("title", "image.jpg", "trailer", "description", Status.ENDED, new Date(), null);
        watchList.add(watchListSeries);

        List<Series> favouriteList = new ArrayList<>();
        Series favouriteSeries = new Series("title", "image.jpg", "trailer", "description", Status.ENDED, new Date(), null);
        favouriteList.add(favouriteSeries);

        List<Episode> watchedEpisodes = new ArrayList<>();
        Episode epsiodes = new Episode("title", new Date(), 120, 1);
        watchedEpisodes.add(epsiodes);

        User tivadar = new User("tivadar", "tivadar@tivadar.hu", "tivadar", watchList, favouriteList, watchedEpisodes, new Date());

        Mockito.when(userRepository.findByUsername(tivadar.getUsername()))
                .thenReturn(tivadar);

        Mockito.when(userRepository.findUserByEmailAddress(tivadar.getEmailAddress()))
                .thenReturn(tivadar);

    }


    @Test
    public void testFindUserByUserNameWhenUserExists() {
        String userName = "tivadar";
        User found = userService.findByUsername(userName);

        assertThat(found.getUsername())
                .isEqualTo(userName);
    }

    @Test
    public void testCheckIfEmailAlreadyExists() {
        String email = "tivadar@tivadar.hu";
        boolean emailExists = userService.checkIfEmailAlreadyExists(email);

        assertTrue(emailExists);
    }

    @Test
    public void testCheckIfEmailAlreadyExistsWhenEmailDoesNotExist() {
        String email = "ödön@tivadar.hu";
        boolean emailExists = userService.checkIfEmailAlreadyExists(email);

        assertFalse(emailExists);
    }

    @Test
    public void testCheckIfUserNameAlreadyExists() {
        String userName = "tivadar";
        boolean userNameExists = userService.checkIfUsernameAlreadyExists(userName);

        assertTrue(userNameExists);
    }

    @Test
    public void testCheckIfUserNameAlreadyExistsWhenUserNameDoesNotExist() {
        String userName = "ödön";
        boolean userNameExists = userService.checkIfUsernameAlreadyExists(userName);

        assertFalse(userNameExists);
    }


    @Test
    public void testGetWatchListForUser() {
        String userName = "tivadar";
        List<Series> watchListExpected = new ArrayList<>();
        Series watchListSeries = new Series("title", "image.jpg", "trailer", "description", Status.ENDED, new Date(), null);
        watchListExpected.add(watchListSeries);

        List<Series> watchList = userService.getWatchlistForUser(userName);

        assertThat(watchList)
                .isEqualTo(watchListExpected);
    }


    @Test
    public void testGetFavouritesForUser() {
        String userName = "tivadar";
        List<Series> favouritesExpected = new ArrayList<>();
        Series favouriteSeries = new Series("title", "image.jpg", "trailer", "description", Status.ENDED, new Date(), null);
        favouritesExpected.add(favouriteSeries);

        List<Series> watchList = userService.getFavouritesForUser(userName);

        assertThat(watchList)
                .isEqualTo(favouritesExpected);
    }


    @Test
    public void testGetWatchedEpsiodesForUser() {
        String userName = "tivadar";
        List<Episode> watchedEpisodesExpected = new ArrayList<>();
        Episode episodes = new Episode("title", new Date(), 120, 1);
        watchedEpisodesExpected.add(episodes);

        List<Episode> watchList = userService.getWatchedEpisodesForUser(userName);

        assertThat(watchList)
                .isEqualTo(watchedEpisodesExpected);
    }
}
