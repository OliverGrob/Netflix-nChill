package com.codecool.netflixandchill.dto;

import com.codecool.netflixandchill.model.Series;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDTO {

    private String username;
    private String emailAddress;
    private int timeWasted;
    private Date registrationDate;
    private Collection<Series> watchlist = new ArrayList<>();
    private Collection<Series> favourites = new ArrayList<>();
    private Collection<Series> watchedEpisodesSeries = new ArrayList<>();

}
