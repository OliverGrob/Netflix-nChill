import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgxSpinnerService } from 'ngx-spinner';

import { Series } from '../../models/Series';
import { SeriesService } from '../../services/series.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  searchResult: Series[];
  selectedShow: Series;

  constructor(private seriesService: SeriesService,
              private route: ActivatedRoute,
              private spinner: NgxSpinnerService) { }

  ngOnInit() {
    this.spinner.show();
    this.seriesService.searchResult.subscribe(series => {
      this.searchResult = series ? series : [];
      this.spinner.hide();
    });
    this.seriesService.searchSeries(this.route.snapshot.queryParams['searchTerm']);
  }

  selectShow(show: Series) {
    this.seriesService.selectedSeries.next(show);
    this.selectedShow = show;
  }

}

