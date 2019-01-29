import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { NgxSpinnerService } from 'ngx-spinner';

import { Series } from '../../models/Series';
import { SeriesService } from '../../services/series.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  contentLoaded = false;
  searchResult: Series[];
  selectedShow: Series;
  searchTerm: string;

  constructor(private seriesService: SeriesService,
              private route: ActivatedRoute,
              private spinner: NgxSpinnerService) { }

  ngOnInit() {
    this.route.queryParams.subscribe(
      (params: Params) => {
        this.contentLoaded = false;
        this.spinner.show();
        this.searchTerm = params['searchTerm'];
        this.seriesService.searchSeries(params['searchTerm']).subscribe(
          (series: Series[]) => {
            this.searchResult = series ? series : [];
            this.contentLoaded = true;
            this.spinner.hide();
          }
        );
      }
    );
  }

  selectSeries(series: Series) {
    this.selectedShow = series;
  }

  isLinkActive(url: string) {
    console.log(url);
  }

}

