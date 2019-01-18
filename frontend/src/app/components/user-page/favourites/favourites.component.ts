import { Component, OnInit } from '@angular/core';

import { Series } from '../../../models/Series';
import { User } from '../../../models/User';
import { UserPageService } from '../../../services/user-page.service';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-favourites',
  templateUrl: './favourites.component.html',
  styleUrls: ['./favourites.component.css']
})
export class FavouritesComponent implements OnInit {

  user: User;
  favourites: Series[];

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
    this.favourites = user.favourites;
  }

}
