import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { Observable, of, Subject } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';

import { Series } from '../models/Series';

@Injectable({
  providedIn: 'root'
})
export class SeriesService {

  private baseUrl = 'http://localhost:8080/series';
  searchResult = new Subject<Series[]>();
  selectedSeries = new Subject<Series>();

  constructor(private http: HttpClient) { }

  getSingleSeries(seriesId: number) {
    return this.http.get<Series>(`${this.baseUrl}/${seriesId}`)
      .pipe(
        tap(() => console.log(`Series id=${seriesId} found!`)),
        catchError(response => this.handleError(response))
      );
  }

  searchSeries(searchTerm: string): Observable<Series[]> {
    if (!searchTerm.trim()) { return of([]); }

    this.http.get<Series[]>(`${this.baseUrl}/search?searchTerm=${searchTerm}`)
      .pipe(
        tap(() => console.log('More series found!')),
        catchError(response => this.handleError(response))
      ).subscribe((series: Series[]) => {
        this.searchResult.next(series);
      });
  }

  getTrendingSeries() {
    return this.http.get<Series[]>(`${this.baseUrl}/trending`)
      .pipe(
        tap(() => console.log('Trending series found!')),
        catchError(response => this.handleError(response))
      );
  }

  private handleError<T> (error: HttpErrorResponse, result?: T) {
    console.error(error);
    console.error(error.error['error']);
    console.error(error.error['message']);
    return of(result as T);
  }

}
