import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';

import { Subject } from 'rxjs/internal/Subject';
import { Observable } from 'rxjs/internal/Observable';
import { catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs';
import { ToastrService } from 'ngx-toastr';

import { Series } from '../../models/Series';
import { Season } from '../../models/Season';
import { Episode } from '../../models/Episode';
import { User } from '../../models/User';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = 'http://localhost:8080';

  loginStatus = new Subject<string>();
  user = new Subject<User>();

  constructor(private http: HttpClient,
              private auth: AuthService,
              private router: Router,
              private toastr: ToastrService) { }

  addWholeSeries(series: Series): Observable<Series> {
    return this.http.put<any>(
      `${this.baseUrl}/users/${this.getUsername()}/add-series-to-watched/series/${series.id}`,
      {'series': series.id})
      .pipe(
        tap(_ => console.log(`Series added`)),
        catchError(response => this.handleError(response))
      );
  }

  removeWholeSeries(series: Series): Observable<Series> {
    return this.http.delete<any>(
      `${this.baseUrl}/users/${this.getUsername()}/remove-series-from-watched/series/${series.id}`)
      .pipe(
        tap(_ => console.log(`Series removed`)),
        catchError(response => this.handleError(response))
      );
  }

  addSingleSeason(season: Season): Observable<Season> {
    return this.http.put<any>(
      `${this.baseUrl}/users/${this.getUsername()}/add-season-to-watched/season/${season.id}`,
      {'season': season.id})
      .pipe(
        tap(_ => console.log(`Season added`)),
        catchError(response => this.handleError(response))
      );
  }

  removeSingleSeason(season: Season): Observable<Season> {
    return this.http.delete<any>(
      `${this.baseUrl}/users/${this.getUsername()}/remove-season-from-watched/season/${season.id}`)
      .pipe(
        tap(_ => console.log('Season removed')),
        catchError(response => this.handleError(response))
      );
  }

  addSingleEpisode(episode: Episode): Observable<Episode> {
    return this.http.put<any>(
      `${this.baseUrl}/users/${this.getUsername()}/add-episode-to-watched/episode/${episode.id}`,
      {'episode': episode.id})
      .pipe(
        tap(_ => console.log(`Episode added`)),
        catchError(response => this.handleError(response))
      );
  }

  removeSingleEpisode(episode: Episode): Observable<Episode> {
    return this.http.delete<any>(
      `${this.baseUrl}/users/${this.getUsername()}/remove-episode-from-watched/episode/${episode.id}`)
      .pipe(
        tap(_ => console.log(`Episode removed`)),
        catchError(response => this.handleError(response))
      );
  }

  addToFavourites(series: Series): Observable<Series> {
    return this.http.put<any>(
      `${this.baseUrl}/users/${this.getUsername()}/add-series-to-favourites/series/${series.id}`,
      {'favourite': series.id})
      .pipe(
        tap(_ => console.log(`Series added to favourites`)),
        catchError(response => this.handleError(response))
      );
  }

  removeFromFavourites(series: Series): Observable<Series> {
    return this.http.delete<any>(
      `${this.baseUrl}/users/${this.getUsername()}/remove-series-from-favourites/series/${series.id}`)
      .pipe(
        tap(_ => console.log(`Series removed from favourites`)),
        catchError(response => this.handleError(response))
      );
  }

  addToWatchlist(series: Series): Observable<Series> {
    return this.http.put<any>(
      `${this.baseUrl}/users/${this.getUsername()}/add-series-to-watchlist/series/${series.id}`,
      {'watchlist': series.id})
      .pipe(
        tap(_ => console.log(`Series added to watchlist`)),
        catchError(response => this.handleError(response))
      );
  }

  getUser(): Observable<User> {
    return this.http.get<any>(`${this.baseUrl}/users/${this.getUsername()}`)
      .pipe(
        tap(_ => console.log(`Getting User`)),
        catchError(response => this.handleError(response))
      );
  }

  getUsername(): string {
    return this.auth.getUsername();
  }

  validateJoin(username: string, email: string, password: string, confirmPassword: string): Observable<any> {
    if (!username.trim() || !email.trim() || !password.trim() || !confirmPassword.trim()) { return of(); }

    this.http.post<any>(`${this.baseUrl}/users/join`, {
      'username': username, 'email': email, 'password': password, 'confirmPassword': confirmPassword})
      .pipe(
        tap(_ => console.log(`User join`)),
        catchError(response => this.handleError(response))
      ).subscribe(response => {
        this.toastr.success('You registered successfully!');
        console.log(response);
    });
  }

  validateLogin(username: string, password: string): Observable<User> {
    if (!username.trim() || !password.trim()) { return of(); }

    this.http.post(`${this.baseUrl}/users/login`, {'username': username, 'password': password},
      { observe: 'response'})
      .pipe(
        tap(_ => console.log(`User login, should get back User`)),
        catchError(response => this.handleError(response))
      ).subscribe((response: HttpResponse<any>) => {
        console.log(response);
        if (response.headers.get('Authorization') !== null) {
          console.log(response.headers.get('Authorization'));
          this.auth.sendToken(response.headers.get('Authorization'));
          this.getUser().subscribe(responseUser => {
            this.toastr.success('You logged in successfully!');
            console.log(responseUser);
            return this.user.next(responseUser);
          });
        }
    });
  }

  private handleError<T> (error: HttpErrorResponse, result?: T) {
    console.error(error);
    console.error(error.error['error']);
    console.error(error.error['message']);
    if (error.status === 401) {
      sessionStorage.removeItem('token');
      this.router.navigate(['/']);
    }
    return of(result as T);
  }

  logoutUser() {
    this.auth.logout();
  }

}
