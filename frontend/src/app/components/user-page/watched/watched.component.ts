import { Component, OnInit } from '@angular/core';

import { Episode } from '../../../models/Episode';
import { User } from '../../../models/User';
import { UserPageService } from '../../../services/user-page.service';
import { UserService } from '../../../services/user.service';
import { Season } from '../../../models/Season';

@Component({
  selector: 'app-watched',
  templateUrl: './watched.component.html',
  styleUrls: ['./watched.component.css']
})
export class WatchedComponent implements OnInit {

  user: User;
  watchedEpisodes: Episode[] = [];
  timeWasted: number;
  descriptionsOpen = [];
  seasonsOpen = [];
  seasonEpisodesOpen: Season;

  constructor(private userService: UserService,
              private userPageService: UserPageService) { }

  ngOnInit() {
    this.userPageService.user.subscribe(
      (user: User) => {
        if (user === undefined) {
          this.userService.getUser().subscribe(
            (userSafe: User) => this.initializeFields(userSafe)
          );
          return;
        }
        this.initializeFields(user);
      }
    );
    this.userPageService.requestUser.next();
  }

  initializeFields(user: User) {
    this.user = user;
    user.watchedEpisodesSeries.forEach(
      series => series.seasons.forEach(
        season => this.watchedEpisodes.push(...season.episodes)
      )
    );
    this.timeWasted = user.timeWasted;
  }

  onSelectDescription(seriesId: number) {
    this.seasonsOpen = [];
    this.seasonEpisodesOpen = undefined;
    this.descriptionsOpen.push(seriesId);
  }

  onSelectSeasons(seriesId: number) {
    this.descriptionsOpen = [];
    this.seasonsOpen.push(seriesId);
  }

  onSelectSingleSeason(season: Season, seriesId: number) {
    this.seasonsOpen = this.seasonsOpen.filter(currentSeriesId => currentSeriesId === seriesId);
    this.seasonEpisodesOpen = season;
  }

}
