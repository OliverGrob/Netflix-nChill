import { DomSanitizer } from '@angular/platform-browser';
import { Component, OnInit } from '@angular/core';

import { NgxSpinnerService } from 'ngx-spinner';

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

  constructor(private seriesService: SeriesService,
              private spinner: NgxSpinnerService,
              private sanitizer: DomSanitizer) { }

  ngOnInit() {
    this.spinner.show();
    this.seriesService.getTrendingSeries().subscribe(
      (trendingSeries: Series[]) => {
        this.trendingSeries = trendingSeries;
        this.contentLoaded = true;
        this.spinner.hide();
      }
    );
  }

  sanitizeUrl(url: string) {
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }

}
