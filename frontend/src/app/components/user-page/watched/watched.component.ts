import { Component, OnInit } from '@angular/core';

import { Episode } from '../../../models/Episode';
import { User } from '../../../models/User';
import { UserService } from '../../../services/user/user.service';

@Component({
  selector: 'app-watched',
  templateUrl: './watched.component.html',
  styleUrls: ['./watched.component.css']
})
export class WatchedComponent implements OnInit {

  watchedEpisodes: Episode[];
  timeWasted;

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.userService.getUser()
      .subscribe(
        (user: User) => {
          this.watchedEpisodes = user.watchedEpisodes;
          this.timeWasted = user.timeWasted;
        }
      );
  }

}
