import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';

import { Observable } from 'rxjs/internal/Observable';
import { catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs';
import { ToastrService } from 'ngx-toastr';

import { Series } from '../models/Series';
import { Season } from '../models/Season';
import { Episode } from '../models/Episode';
import { User } from '../models/User';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = 'http://localhost:8080/users';

  constructor(private http: HttpClient,
              private auth: AuthService,
              private router: Router,
              private toastr: ToastrService) { }

  toggleSeriesInWatched(series: Series) {
    return this.http.put<void>(
      `${this.baseUrl}/${this.getUsername()}/toggle-series-in-watched/series/${series.id}`,
      {'seriesId': series.id})
      .pipe(
        tap(() => console.log('Toggle whole series in watched episodes')),
        catchError(response => this.handleError(response))
      );
  }

  toggleSingleSeason(season: Season) {
    return this.http.put<void>(
      `${this.baseUrl}/${this.getUsername()}/toggle-season-in-watched/season/${season.id}`,
      {'seasonId': season.id})
      .pipe(
        tap(() => console.log('Toggle single season in watched episodes')),
        catchError(response => this.handleError(response))
      );
  }

  toggleSingleEpisode(episode: Episode) {
    return this.http.put<void>(
      `${this.baseUrl}/${this.getUsername()}/toggle-episode-in-watched/episode/${episode.id}`,
      {'episodeId': episode.id})
      .pipe(
        tap(() => console.log('Toggle single episode in watched episodes')),
        catchError(response => this.handleError(response))
      );
  }

  toggleFavourites(series: Series) {
    return this.http.put<void>(
      `${this.baseUrl}/${this.getUsername()}/toggle-series-in-favourites/series/${series.id}`,
      {'seriesId': series.id})
      .pipe(
        tap(() => console.log('Toggle series in favourites')),
        catchError(response => this.handleError(response))
      );
  }

  toggleWatchlist(series: Series) {
    return this.http.put<void>(
      `${this.baseUrl}/${this.getUsername()}/toggle-series-in-watchlist/series/${series.id}`,
      {'seriesId': series.id})
      .pipe(
        tap(() => console.log('Toggle series in watchlist')),
        catchError(response => this.handleError(response))
      );
  }

  getUser(): Observable<User> {
    return this.http.get<any>(`${this.baseUrl}/${this.getUsername()}`)
      .pipe(
        tap(() => console.log('Getting User')),
        catchError(response => this.handleError(response))
      );
  }

  getUsername(): string {
    return this.auth.getUsername();
  }

  validateJoin(username: string, email: string, password: string, confirmPassword: string) {
    this.http.post<void>(`${this.baseUrl}/join`,
      {'username': username, 'email': email, 'password': password, 'confirmPassword': confirmPassword})
      .pipe(
        tap(() => console.log('User join')),
        catchError(response => this.handleError(response)))
      .subscribe(
        () => this.toastr.success('You registered successfully!')
      );
  }

  validateLogin(username: string, password: string) {
    this.http.post<void>(`${this.baseUrl}/login`,
      {'username': username, 'password': password},
      { observe: 'response'})
      .pipe(
        tap(() => console.log('User login'))
      )
      .subscribe(
        (response: HttpResponse<any>) => {
          if (response.headers.get('Authorization') !== null) {
            this.toastr.success('You logged in successfully!');
            this.auth.sendToken(response.headers.get('Authorization'));
          }
        },
        (response: HttpErrorResponse) => this.handleError(response));
  }

  logoutUser() {
    this.auth.logout();
  }

  getRecommendedSeries(genreName: string) {
    return this.http.get<Series[]>(
      `${this.baseUrl}/${this.getUsername()}/recommended-series?genre=${genreName}`)
      .pipe(
        tap(() => console.log('Recommended series found!')),
        catchError(response => this.handleError(response))
      );
  }

  private handleError<T> (error: HttpErrorResponse, result?: T) {
    console.error(error);
    console.error(error.error['error']);
    console.error(error.error['message']);
    this.toastr.error(error.error['message']);
    if (error.status === 401) {
      sessionStorage.removeItem('token');
      this.router.navigate(['/']);
    }
    return of(result as T);
  }

}
