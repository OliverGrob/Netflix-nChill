import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

import { Subject } from 'rxjs';
import * as jwt_decode from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  loggedIn = new Subject<boolean>();

  constructor(private router: Router) { }

  sendToken(token: string) {
    sessionStorage.setItem('token', token.substring(7));
    this.loggedIn.next(true);
  }

  logout() {
    sessionStorage.removeItem('token');
    this.loggedIn.next(false);
    this.router.navigate(['/']);
  }

  isLoggedIn(): boolean {
    return (this.getUsername() !== undefined) && (!this.isTokenExpired());
  }

  getUsername(): string {
    try {
      return jwt_decode(sessionStorage.getItem('token'))['sub'];
    } catch (error) {
      console.log('For some reason this gives an error, not my b tho: ' + error);
    }
  }

  isTokenExpired(): boolean {
    return +jwt_decode(sessionStorage.getItem('token'))['exp'] < Date.now() / 1000;
  }

}
