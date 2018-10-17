import { Component, Input, OnInit } from '@angular/core';

import { Series } from '../../../models/Series';
import { Episode } from '../../../models/Episode';
import { Season } from '../../../models/Season';
import { UserService } from '../../../services/user/user.service';

@Component({
  selector: 'app-series-detail',
  templateUrl: './series-detail.component.html',
  styleUrls: ['./series-detail.component.css']
})
export class SeriesDetailComponent implements OnInit {

  @Input() series: Series;

  constructor(private userService: UserService) { }

  ngOnInit() {
  }

  addWholeSeries(series: Series): void {
    console.log('all seasons added');
    this.userService.addWholeSeries(series)
      .subscribe(answer => console.log(answer));
  }

  addSingleSeason(season: Season): void {
    console.log('season added');
    this.userService.addSingleSeason(season).subscribe();
  }

  addSingleEpisode(episode: Episode): void {
    console.log('episode added');
    this.userService.addSingleEpisode(episode).subscribe();
  }

}
