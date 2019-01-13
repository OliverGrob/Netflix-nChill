package com.codecool.netflixandchill.controller;

import com.codecool.netflixandchill.dto.ErrorDTO;
import com.codecool.netflixandchill.model.Series;
import com.codecool.netflixandchill.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SeriesController {

    @Autowired
    private SeriesService seriesService;


    @GetMapping
    public List<Series> getAllSeries() {
        return this.seriesService.getAllSeries();
    }

    @GetMapping("/{seriesId}")
    public ResponseEntity getSingleSeries(@PathVariable Long seriesId) {
        Series series = this.seriesService.getSingleSeriesById(seriesId);

        if (series != null) return ResponseEntity.status(HttpStatus.OK).body(series);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorDTO.builder()
                            .error("series cannot be found")
                            .message("Series' id in the URL is incorrect!")
                            .build());
    }

    @GetMapping("/search")
    public ResponseEntity getSeriesBySubstring(@RequestParam("searchTerm") String substring) {
        List<Series> series = this.seriesService.getSeriesBySubstring(substring);

        if (series.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorDTO.builder()
                            .error("series cannot be found")
                            .message("This series cannot be found in the database!")
                            .build());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(series);
    }

    @GetMapping("/trending")
    public List<Series> getTrendingSeries() {
        return this.seriesService.getTrendingSeries();
    }

}
