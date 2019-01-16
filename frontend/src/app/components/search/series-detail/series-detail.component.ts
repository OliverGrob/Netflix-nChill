import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

import { Series } from '../../../models/Series';
import { Episode } from '../../../models/Episode';
import { Season } from '../../../models/Season';
import { UserService } from '../../../services/user/user.service';
import { User } from '../../../models/User';
import { SeriesService } from '../../../services/series/series.service';
import { AuthService } from '../../../services/auth/auth.service';

@Component({
  selector: 'app-series-detail',
  templateUrl: './series-detail.component.html',
  styleUrls: ['./series-detail.component.css']
})
export class SeriesDetailComponent implements OnInit {

  series: Series;
  user: User;
  userLoggedIn: boolean = this.auth.isLoggedIn();
  openedSeasons: number[] = [];
  hearted: number[] = [];
  checkedEpisodes: number[] = [];
  checkedSeries = false;
  watchlist: number[] = [];

  constructor(private userService: UserService,
              private seriesService: SeriesService,
              private toastr: ToastrService,
              private auth: AuthService) { }

  ngOnInit() {
    if (this.auth.isLoggedIn()) {
      this.userService.getUser()
        .subscribe(user => {
          this.user = user;
          this.user.watchedEpisodes.forEach(episode => this.checkedEpisodes.push(episode.id));
          this.user.favourites.forEach(series => this.hearted.push(series.id));
          this.user.watchlist.forEach(series => this.watchlist.push(series.id));
        });
    }
    this.seriesService.selectedSeries
      .subscribe(
        series => this.series = series
      );
  }

  toggleWholeSeries(series: Series, operationIsAdd: boolean): void {
    const operation = operationIsAdd ? 'added to' : 'removed from';
    this.userService.toggleSeriesInWatched(series).subscribe(
      () => this.toastr.success(`'${series.title}' ${operation} your list!`)
    );
  }

  toggleSingleSeason(season: Season, operationIsAdd: boolean): void {
    const operation = operationIsAdd ? 'added to' : 'removed from';
    this.userService.toggleSingleSeason(season).subscribe(
      () => this.toastr.success(`Season ${season.seasonNumber} ${operation} your list!`)
    );
  }

  toggleSingleEpisode(episode: Episode, operationIsAdd: boolean): void {
    const operation = operationIsAdd ? 'added to' : 'removed from';
    this.userService.toggleSingleEpisode(episode).subscribe(
      () => this.toastr.success(`Episode ${episode.episodeNumber}: '${episode.title}' ${operation} your list!`)
    );
  }

  toggleFavourites(series: Series, operationIsAdd: boolean): void {
    const operation = operationIsAdd ? 'added to' : 'removed from';
    this.userService.toggleFavourites(series).subscribe(
      () => this.toastr.success(`'${series.title}' ${operation} your favourites!`)
    );
  }

  toggleWatchlist(series: Series, operationIsAdd: boolean): void {
    const operation = operationIsAdd ? 'added to' : 'removed from';
    this.userService.toggleWatchlist(series).subscribe(
      () => this.toastr.success(`'${series.title}' ${operation} your watchlist!`)
    );
  }

  toggleSeason(seasonId: number) {
    if (this.openedSeasons.includes(seasonId)) {
      this.openedSeasons = this.openedSeasons.filter(currentId => currentId !== seasonId);
      return;
    }

    this.openedSeasons.push(seasonId);
  }

  alreadyOpened(seasonId: number): boolean {
    return this.openedSeasons.includes(seasonId);
  }

  toggleEpisode(episodeId: number) {
    if (this.checkedEpisodes.includes(episodeId)) {
      this.checkedEpisodes = this.checkedEpisodes.filter(currentId => currentId !== episodeId);
      return;
    }

    this.checkedEpisodes.push(episodeId);
  }

  handleWatchedEpisode(episode: Episode) {
    const operationIsAdd = !this.checkedEpisodes.includes(episode.id);

    this.toggleEpisode(episode.id);
    this.toggleSingleEpisode(episode, operationIsAdd);
  }

  alreadyCheckedEpisode(episodeId: number): boolean {
    return this.checkedEpisodes.includes(episodeId);
  }

  toggleSeries() {
    this.checkedSeries = !this.checkedSeries;
  }

  handleWatchedSeries(series: Series) {
    const checked = this.alreadyCheckedSeries(series);
    this.toggleSeries();

    if (checked) {
      series.seasons.forEach(season => season.episodes
        .forEach(episode => {
          if (this.checkedEpisodes.includes(episode.id)) {
            this.toggleEpisode(episode.id);
          }
        }));
      this.toggleWholeSeries(series, false);
      return;
    }

    series.seasons.forEach(season => season.episodes
      .forEach(episode => {
        if (!this.checkedEpisodes.includes(episode.id)) {
          this.toggleEpisode(episode.id);
        }
      }));

    this.toggleWholeSeries(series, true);
  }

  alreadyCheckedSeries(series: Series): boolean {
    const episodesIds: number[] = [];
    series.seasons.forEach(season => season.episodes
      .forEach(episode => episodesIds.push(episode.id)));
    for (const id of episodesIds) {
      if (!this.checkedEpisodes.includes(id)) {
        return false;
      }
    }
    return true;
  }

  handleWatchedSeason(season: Season) {
    const checked = this.alreadyCheckedSeason(season);

    if (checked) {
      season.episodes.forEach(episode => {
        if (this.checkedEpisodes.includes(episode.id)) {
          this.toggleEpisode(episode.id);
        }
      });
      this.toggleSingleSeason(season, false);
      return;
    }

    season.episodes.forEach(episode => {
      if (!this.checkedEpisodes.includes(episode.id)) {
        this.toggleEpisode(episode.id);
      }
    });

    this.toggleSingleSeason(season, true);
  }

  alreadyCheckedSeason(season: Season): boolean {
    const episodesIds: number[] = [];
    season.episodes.forEach(episode => episodesIds.push(episode.id));
    for (const id of episodesIds) {
      if (!this.checkedEpisodes.includes(id)) {
        return false;
      }
    }
    return true;
  }

  handleFavourites(series: Series) {
    const operationIsAdd = !this.hearted.includes(series.id);

    if (this.hearted.includes(series.id)) {
      this.hearted = this.hearted.filter(currentId => currentId !== series.id);
    } else {
      this.hearted.push(series.id);
    }

    this.toggleFavourites(series, operationIsAdd);
  }

  alreadyFavourite(series: Series): boolean {
    return this.hearted.includes(series.id);
  }

  handleWatchlist(series: Series) {
    const operationIsAdd = !this.watchlist.includes(series.id);

    if (this.watchlist.includes(series.id)) {
      this.watchlist = this.watchlist.filter(currentId => currentId !== series.id);
    } else {
      this.watchlist.push(series.id);
    }

    this.toggleWatchlist(series, operationIsAdd);
  }

}
