package com.codecool.netflixandchill.service;

import com.codecool.netflixandchill.model.Series;
import com.codecool.netflixandchill.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SeriesService {

    @Autowired
    private SeriesRepository seriesRepository;


    public void addSeries(Series series) {
        this.seriesRepository.save(series);
    }

    public Series getSingleSeriesById(Long seriesId) {
        Optional<Series> seriesOptional = this.seriesRepository.findById(seriesId);
        return seriesOptional.orElse(null);
    }

    public List<Series> getAllSeries() {
        return this.seriesRepository.findAll();
    }

    public List<Series> getSeriesBySubstring(String substring) {
        return this.seriesRepository.findByTitleContainingIgnoreCase(substring);
    }

    public List<Series> getTrendingSeries() {
        List<Series> trendingSeries = this.seriesRepository.findTop5ByOrderByAirDateDesc();

        Collections.shuffle(trendingSeries);

        return trendingSeries;
    }

}
