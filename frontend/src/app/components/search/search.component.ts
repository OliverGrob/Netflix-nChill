import { Component, OnInit } from '@angular/core';

import { Series } from '../../models/Series';
import { SeriesService } from '../../services/series/series.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  searchResult: Series[] = [];
  selectedShow: Series;

  constructor(private seriesService: SeriesService) { }

  ngOnInit() {
    this.seriesService.searchResult.subscribe(series => {
      this.searchResult = series;
      console.log(this.searchResult);
    });
  }

  selectShow(show: Series) {
    this.seriesService.selectedSeries.next(show);
    this.selectedShow = show;
  }

}

