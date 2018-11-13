import { Component, Input, OnInit } from '@angular/core';

import { Series } from '../../../models/Series';
import { Episode } from '../../../models/Episode';
import { Season } from '../../../models/Season';
import { UserService } from '../../../services/user/user.service';
import { User } from '../../../models/User';
import { SeriesService } from '../../../services/series/series.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-series-detail',
  templateUrl: './series-detail.component.html',
  styleUrls: ['./series-detail.component.css']
})
export class SeriesDetailComponent implements OnInit {

  series: Series;
  user: User;
  token: string = sessionStorage.getItem('token');
  showSeasons: number[] = [];
  hearted: number[] = [];
  checkedEpisodes: number[] = [];

  constructor(private userService: UserService,
              private seriesService: SeriesService,
              private toastr: ToastrService) { }

  ngOnInit() {
    this.userService.getUser(localStorage.getItem('username')).subscribe(response => {
      // if (response['data']) {
        console.log(response);
        this.user = response;
        console.log(this.user);
      // } else {
      //   console.log(response['error']);
      // }
    });
  }

  addWholeSeries(series: Series): void {
    console.log('all seasons added');
    this.userService.addWholeSeries(localStorage.getItem('username'), series).subscribe(response => {
      console.log(this.handleResponse(response));
      this.toastr.success(series.title + ' added to your list!');
      series.seasons.forEach(season => season.episodes
        .forEach(episode => this.toggleEpisode(episode.id)));
    });
  }

  removeWholeSeries(series: Series): void {
    console.log('all series removed');
    this.userService.removeWholeSeries(localStorage.getItem('username'), series).subscribe(response => {
      console.log(this.handleResponse(response));
      this.toastr.info(series.title + ' removed from your list!');
    });
  }

  addSingleSeason(season: Season): void {
    console.log('season added');
    this.userService.addSingleSeason(localStorage.getItem('username'), season).subscribe(response => {
      console.log(this.handleResponse(response));
      this.toastr.success('Season ' + season.seasonNumber + ' added to your list!');
    });
  }

  removeSingleSeason(season: Season): void {
    console.log('season removed');
    this.userService.removeSingleSeason(localStorage.getItem('username'), season).subscribe(response => {
      console.log(this.handleResponse(response));
      this.toastr.info('Season ' + season.seasonNumber + ' removed from your list!');
    });
  }

  addSingleEpisode(episode: Episode): void {
    console.log('episode added');
    this.userService.addSingleEpisode(localStorage.getItem('username'), episode).subscribe(response => {
      console.log(this.handleResponse(response));
      this.toastr.success('Episode ' + episode.episodeNumber + ': ' + episode.title + ' added to your list!');
    });
  }

  removeSingleEpisode(episode: Episode): void {
    console.log('episode removed');
    this.userService.removeSingleEpisode(localStorage.getItem('username'), episode).subscribe(response => {
      console.log(this.handleResponse(response));
      this.toastr.info('Episode ' + episode.episodeNumber + ' removed from your list!');
    });
  }

  addToFavourites(series: Series): void {
    console.log('added to favourites');
    this.userService.addToFavourites(localStorage.getItem('username'), series).subscribe(response => {
      console.log(this.handleResponse(response));
      this.toastr.success(series.title + ' added to your favourites!');
    });
  }

  removeFromFavourites(series: Series): void {
    console.log('removed from favourites');
    this.userService.removeFromFavourites(localStorage.getItem('username'), series).subscribe(response => {
      console.log(this.handleResponse(response));
      this.toastr.info(series.title + ' removed from your favourites!');
    });
  }

  addToWatchlist(series: Series): void {
    console.log('added to watchlist');
    this.userService.addToWatchlist(localStorage.getItem('username'), series).subscribe(response => {
      console.log(this.handleResponse(response));
      this.toastr.success(series.title + ' added to your watchlist!');
    });
  }

  handleResponse(response) {
    // if (response['data']) {
      return response;
    // } else {
    //   return response['error'];
    // }
  }

  selectShow(seriesId: number) {
    this.seriesService.getSingleSeries(seriesId).subscribe(response => {
      console.log(response);
      this.series = response;
    });
  }

  toggleSeason(seasonId: number) {
    if (this.showSeasons.includes(seasonId)) {
      this.showSeasons = this.showSeasons.filter(currentId => currentId !== seasonId);
      return;
    }

    this.showSeasons.push(seasonId);
  }

  alreadyOpened(seasonId: number): boolean {
    return this.showSeasons.includes(seasonId);
  }

  toggleEpisode(episodeId: number) {
    if (this.checkedEpisodes.includes(episodeId)) {
      this.checkedEpisodes = this.checkedEpisodes.filter(currentId => currentId !== episodeId);
      return;
    }

    this.checkedEpisodes.push(episodeId);
  }

  alreadyCheckedEpisode(episodeId: number): boolean {
    return this.checkedEpisodes.includes(episodeId);
  }

  alreadyCheckedSeason(season: Season): boolean {
    let episodesIds: number[] = [];
    season.episodes.forEach(episode => episodesIds.push(episode.id));
    for (let id of episodesIds) {
      if (!this.checkedEpisodes.includes(id)) {
        return false;
      }
    }
    return true;
  }

  handleWatchedSeries(series: Series, checkbox) {
    series.seasons.forEach(season => season.episodes
      .forEach(episode => this.toggleEpisode(episode.id)));
    if (checkbox.classList.contains('checked')) {
      this.removeWholeSeries(series);
      return;
    }

    this.addWholeSeries(series);
  }

  handleWatchedSeason(season: Season, checkbox) {
    season.episodes.forEach(episode => this.toggleEpisode(episode.id));
    if (checkbox.classList.contains('checked')) {
      this.removeSingleSeason(season);
      return;
    }

    this.addSingleSeason(season);
  }

  handleWatchedEpisode(episode: Episode, checkbox) {
    this.toggleEpisode(episode.id);
    if (checkbox.classList.contains('checked')) {
      this.removeSingleEpisode(episode);
      return;
    }

    this.addSingleEpisode(episode);
  }

  toggleFavourite(seriesId: number) {
    if (this.hearted.includes(seriesId)) {
      this.hearted = this.hearted.filter(currentId => currentId !== seriesId);
      return;
    }

    this.hearted.push(seriesId);
  }

  alreadyFavourite(series: Series): boolean {
    console.log(this.hearted.includes(series.id));
    return this.hearted.includes(series.id);
  }

  handleFavourites(series: Series, checkbox) {
    this.toggleFavourite(series.id);
    if (checkbox.classList.contains('hearted')) {
      this.removeFromFavourites(series);
      return;
    }

    this.addToFavourites(series);
  }

}
