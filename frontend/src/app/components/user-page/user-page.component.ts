import { Component, ElementRef, EventEmitter, OnInit, ViewChild } from '@angular/core';

import { Chart } from 'node_modules/chart.js';
import { NgxSpinnerService } from 'ngx-spinner';

import { User } from '../../models/User';
import { UserService } from '../../services/user.service';
import { UserPageService } from '../../services/user-page.service';
import { Series } from '../../models/Series';

@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrls: ['./user-page.component.css']
})
export class UserPageComponent implements OnInit {

  @ViewChild('pieChartCanvas') pieChartCanvas: ElementRef;
  context: CanvasRenderingContext2D;

  contentLoaded = false;
  user: User;
  watchedEpisodesNum = 0;
  allGenres: {name: string, amount: number}[] = [];
  pieChart: Chart = [];
  recommendedSeries: Series[];
  recommendedSeriesDescriptionOpen: Series[] = [];

  constructor(private userService: UserService,
              private userPageService: UserPageService,
              private spinner: NgxSpinnerService) { }

  ngOnInit() {
    this.spinner.show();
    setTimeout(() => this.userService.getUser()
      .subscribe(user => {
        if (!user) return;
        this.user = user;
        user.watchedEpisodesSeries.forEach(
          series => this.watchedEpisodesNum += series.allEpisodes.length
        );
        this.collectGenres();
        this.getRecommendedSeries();
        this.contentLoaded = true;
        this.spinner.hide();
      }), 1000);
    this.userPageService.requestUser.subscribe(
      () => this.userPageService.user.next(this.user)
    );
  }

  onShowMoreOrLess(series: Series) {
    if (this.recommendedSeriesDescriptionOpen.includes(series)) {
      this.recommendedSeriesDescriptionOpen = this.recommendedSeriesDescriptionOpen.filter(
        currentSeries => currentSeries.id !== series.id);
      return;
    }
    this.recommendedSeriesDescriptionOpen.push(series);
  }

  isRecommendedSeriesDescriptionOpen(series: Series): boolean {
    return this.recommendedSeriesDescriptionOpen.includes(series);
  }

  getRecommendedSeries() {
    const genreName = this.allGenres[0].name;

    this.userService.getRecommendedSeries(genreName).subscribe(
      (series: Series[]) => this.recommendedSeries = series
    );
  }

  collectGenres() {
    this.user.watchedEpisodesSeries.forEach(
      series => series.genres.forEach(
        genre => {
          if (this.isGenreAlreadyInAllGenres(genre)) {
            this.allGenres.forEach(genreObject => {
              if (genreObject.name === genre) genreObject.amount++;
            });
          } else {
            this.allGenres.push({name: genre, amount: 1});
          }
        }
      )
    );
    this.allGenres.sort((x, y) => {
      if (x.amount < y.amount) return 1;
      if (x.amount > y.amount) return -1;
      return 0;
    });
  }

  isGenreAlreadyInAllGenres(genre: string) {
    let genreIsInList = false;
    this.allGenres.forEach(genreObject => {
      if (genreObject.name === genre) {
        genreIsInList = true;
        return;
      }
    });
    return genreIsInList;
  }

}
