import { Component, OnInit } from '@angular/core';

import { Series } from '../../../models/Series';
import { User } from '../../../models/User';
import { UserService } from '../../../services/user/user.service';

@Component({
  selector: 'app-watchlist',
  templateUrl: './watchlist.component.html',
  styleUrls: ['./watchlist.component.css']
})
export class WatchlistComponent implements OnInit {

  watchList: Series[];

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.userService.getUser()
      .subscribe(
        (user: User) => {
          this.watchList = user.watchlist;
        }
      );
  }

}
