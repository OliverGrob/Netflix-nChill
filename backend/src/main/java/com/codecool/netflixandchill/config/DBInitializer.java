package com.codecool.netflixandchill.config;

import com.codecool.netflixandchill.model.*;
import com.codecool.netflixandchill.service.SeriesService;
import com.codecool.netflixandchill.util.RemoteURLReader;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
class DBInitializer {

    private static final int NUMBER_OF_PAGES_TO_DOWNLOAD = 1;
    private static final int MAX_NUMBER_OF_PAGES_TO_DOWNLOAD = 545;

    private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat FORMAT_2 = new SimpleDateFormat("MMM/dd/yyyy");

    @Autowired
    private RemoteURLReader urlReader;

    @Autowired
    private SeriesService seriesService;


    private JsonArray getAllSeriesJson() throws IOException {
        JsonArray seriesArray = new JsonArray();
        JsonParser jsonParser = new JsonParser();

        for (int i = 1; i <= NUMBER_OF_PAGES_TO_DOWNLOAD; i++) {
            String seriesPath = "https://www.episodate.com/api/most-popular?page=" + i;
            String result = urlReader.readFromUrl(seriesPath);
            JsonObject page = (JsonObject) jsonParser.parse(result);
            page.get("tv_shows").getAsJsonArray().forEach(seriesArray::add);
        }

        return seriesArray;
    }

    private List<Integer> getAllSeriesId() throws IOException {
        List<Integer> seriesIdList = new ArrayList<>();

        getAllSeriesJson().forEach(series -> seriesIdList.add(series.getAsJsonObject().get("id").getAsInt()));

        return seriesIdList;
    }

    private JsonArray getAllSeriesWithSeasonAndEpisodeJson() throws IOException {
        JsonArray seriesArray = new JsonArray();
        JsonParser jsonParser = new JsonParser();

        for (Integer id : getAllSeriesId()) {
            String seriesPath = "https://www.episodate.com/api/show-details?q=" + id.toString();
            String result = urlReader.readFromUrl(seriesPath);
            seriesArray.add(jsonParser.parse(result));
        }

        return seriesArray;
    }

    @PostConstruct
    void populateDB() throws IOException {
        for (JsonElement element : getAllSeriesWithSeasonAndEpisodeJson()) {
            Episode episode;
            Season season;

            JsonObject currentSeries = (JsonObject) element.getAsJsonObject().get("tvShow");

            Series series = Series.builder()
                    .title(currentSeries.get("name").getAsString())
                    .description(currentSeries.get("description").getAsString())
                    .airDate(formatStringToDate(currentSeries.get("start_date").getAsString()))
                    .image(currentSeries.get("image_path").getAsString())
                    .trailer(currentSeries.get("youtube_link").getAsString())
                    .status(Status.valueOf(currentSeries.get("status").getAsString()
                    .replace(" ", "_").toUpperCase()))
                    .genres((convertJsonArrayToEnumGenre(currentSeries.get("genres").getAsJsonArray())))
                    .build();

            Set<Integer> seasonSet = new HashSet<>();

            JsonArray episodes = currentSeries.get("episodes").getAsJsonArray();

            for (int j = 0; j < episodes.size(); j++) {
                seasonSet.add(episodes.get(j).getAsJsonObject().get("season").getAsInt());
            }

            for (int j = 0; j < seasonSet.size(); j++) {
                season = Season.builder()
                        .title("Season " + (j + 1))
                        .year(formatStringToDate(episodes.get(j).getAsJsonObject().get("air_date").getAsString()))
                        .seasonNumber(j + 1)
                        .build();

                series.addSeason(season);

                for (int k = 0; k < episodes.size(); k++) {
                    if (episodes.get(k).getAsJsonObject().get("season").getAsInt() == season.getSeasonNumber()) {
                        episode = Episode.builder()
                                .title(episodes.get(k).getAsJsonObject().get("name").getAsString())
                                .releaseDate(formatStringToDate(episodes.get(k).getAsJsonObject().get("air_date").getAsString()))
                                .runtime(currentSeries.get("runtime").getAsInt())
                                .episodeNumber(episodes.get(k).getAsJsonObject().get("episode").getAsInt())
                                .build();

                        season.addEpisode(episode);
                    }
                }
            }
            seriesService.addSeries(series);
        }
    }

    private List<Genre> convertJsonArrayToEnumGenre(JsonArray genreList) {
        List<Genre> genres = new LinkedList<>();

        genreList.forEach(genre -> genres.add(Genre.valueOf(genre.getAsString()
                .replace("-", "_")
                .toUpperCase())));

        return genres;
    }

    private Date formatStringToDate(String stringDate) {
        try {
            return FORMAT.parse(stringDate);
        } catch (ParseException e) {
            try {
                return FORMAT_2.parse(stringDate);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

}