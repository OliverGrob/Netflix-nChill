import { Component, OnInit } from '@angular/core';

import { Series } from '../../../models/Series';
import { UserService } from '../../../services/user/user.service';
import { User } from '../../../models/User';

@Component({
  selector: 'app-favourites',
  templateUrl: './favourites.component.html',
  styleUrls: ['./favourites.component.css']
})
export class FavouritesComponent implements OnInit {

  favourites: Series[];

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.userService.getUser()
      .subscribe(
        (user: User) => {
          this.favourites = user.favourites;
        }
      );
  }

}
