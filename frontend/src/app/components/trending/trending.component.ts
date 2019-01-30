import { DomSanitizer } from '@angular/platform-browser';
import { Component, OnInit } from '@angular/core';

import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';

import { SeriesService } from '../../services/series.service';
import { Series } from '../../models/Series';

@Component({
  selector: 'app-trending',
  templateUrl: './trending.component.html',
  styleUrls: ['./trending.component.css']
})
export class TrendingComponent implements OnInit {

  contentLoaded = false;
  trendingSeries: Series[];
  randomSeries: Series;
  userChosenSeries: Series;

  constructor(private seriesService: SeriesService,
              private spinner: NgxSpinnerService,
              private sanitizer: DomSanitizer,
              private toastr: ToastrService) { }

  ngOnInit() {
    this.spinner.show();
    this.seriesService.getTrendingSeries().subscribe(
      (trendingSeries: Series[]) => {
        this.trendingSeries = trendingSeries;
        this.randomSeries = this.selectRandomSeries();
        this.contentLoaded = true;
        this.spinner.hide();
      }
    );
  }

  sanitizeUrl(url: string) {
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }

  selectRandomSeries() {
    const randomIndex = Math.floor(Math.random() * (this.trendingSeries.length - 2)) + 2;

    return this.trendingSeries[randomIndex];
  }

  onSelectAnswer(series: Series) {
    this.userChosenSeries = series;
  }

  onGuess() {
    if (this.userChosenSeries === this.randomSeries) this.toastr.success('That\'s correct!');
    else this.toastr.error('Read the description again!');

    this.randomSeries = this.selectRandomSeries();
  }

}
