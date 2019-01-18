package com.codecool.netflixandchill.dto;

import com.codecool.netflixandchill.model.Episode;
import com.codecool.netflixandchill.model.Season;
import com.codecool.netflixandchill.model.Series;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WatchedDTO {

    List<Episode> watchedEpisodes;
    List<Season> watchedSeason;
    List<Series> watchedSeries;

}
