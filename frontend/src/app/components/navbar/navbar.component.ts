import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  isLoggedIn: boolean;
  searchToggle = false;

  constructor(private userService: UserService,
              private router: Router,
              private auth: AuthService) { }

  ngOnInit() {
    this.isLoggedIn = this.auth.isLoggedIn();
    this.auth.loggedIn.subscribe(status => this.isLoggedIn = status);
  }

  showSearchField(): void {
    this.searchToggle = !this.searchToggle;
  }

  search(searchInput: string): void {
    searchInput = searchInput.trim();
    if (searchInput) {
      this.router.navigate(['/search'], {queryParams: {'searchTerm': searchInput}});
      this.searchToggle = !this.searchToggle;
    }
  }

  logout(): void {
    this.userService.logoutUser();
    this.router.navigate(['/']);
  }

}
