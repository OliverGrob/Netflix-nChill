package com.codecool.netflixandchill.repository;

import com.codecool.netflixandchill.model.Episode;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface EpisodeRepository extends JpaRepository<Episode, Long> {

}
