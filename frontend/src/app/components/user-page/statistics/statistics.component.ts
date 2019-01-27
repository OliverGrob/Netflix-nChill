import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';

import { Chart } from 'node_modules/chart.js';

import { Series } from '../../../models/Series';
import { UserPageService } from '../../../services/user-page.service';
import { User } from '../../../models/User';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {

  @ViewChild('pieChartCanvas') pieChartCanvas: ElementRef;
  context: CanvasRenderingContext2D;

  pieChart: Chart = [];
  allGenres: {name: string, amount: number}[] = [];
  user: User;

  constructor(private userService: UserService,
              private userPageService: UserPageService) { }

  ngOnInit() {
    this.userPageService.user.subscribe(
      (user: User) => {
        if (user === undefined) {
          this.userService.getUser().subscribe(
            (userSafe: User) => {
              this.user = userSafe;
              setTimeout(() => this.initPieChart(), 0);
            }
          );
          return;
        }
        this.user = user;
        setTimeout(() => this.initPieChart(), 0);
      }
    );
    this.userPageService.requestUser.next();
  }

  collectGenres() {
    this.user.watchedEpisodesSeries.forEach(
      series => series.genres.forEach(
        genre => {
          if (this.isGenreAlreadyInAllGenres(genre)) {
            this.allGenres.forEach(genreObject => {
              if (genreObject.name === genre) genreObject.amount++;
            });
          } else {
            this.allGenres.push({name: genre, amount: 1});
          }
        }
      )
    );
    this.allGenres.sort((x, y) => {
      if (x.amount < y.amount) return 1;
      if (x.amount > y.amount) return -1;
      return 0;
    });
  }

  isGenreAlreadyInAllGenres(genre: string) {
    let genreIsInList = false;
    this.allGenres.forEach(genreObject => {
      if (genreObject.name === genre) {
        genreIsInList = true;
        return;
      }
    });
    return genreIsInList;
  }

  formGenre(genre: string) {
    const words = genre.split('_');
    words.forEach(word => words[words.indexOf(word)] = word.charAt(0) + word.slice(1).toLowerCase());
    return words.join(' ');
  }

  initPieChart() {
    this.collectGenres();

    const genres = [];
    const genreCount = [];

    if (this.allGenres.length > 0) {
      this.allGenres.forEach(genreObject => {
        genres.push(this.formGenre(genreObject.name));
        genreCount.push(genreObject.amount);
      });
    }

    this.context = (<HTMLCanvasElement>this.pieChartCanvas.nativeElement).getContext('2d');
    this.pieChart = new Chart(this.context, {
      type: 'pie',
      data: {
        labels: genres,
        datasets: [{
          label: '# of Genres',
          data: genreCount,
          backgroundColor: [
            'rgb(142, 36, 170)',
            'rgb(229, 57, 53)',
            'rgba(0, 135, 108, 1)',
            'rgba(239, 146, 80, 1)',
            'rgba(244, 224, 127, 1)',
            'rgba(96, 168, 109, 1)',
            'rgba(233, 119, 76, 1)',
            'rgba(168, 198, 113, 1)',
            'rgba(244, 199, 106, 1)',
            'rgba(132, 183, 110, 1)',
            'rgba(243, 173, 90, 1)',
            'rgba(224, 91, 77, 1)',
            'rgba(60, 152, 109, 1)',
            'rgba(212, 61, 81, 1)',
            'rgba(205, 212, 118, 1)',
            'rgba(255, 99, 132, 1)',
            'rgba(54, 162, 235, 1)',
            'rgba(255, 206, 86, 1)',
            'rgba(75, 192, 192, 1)',
            'rgba(153, 102, 255, 1)',
            'rgba(255, 159, 64, 1)'
          ],
          borderColor: [
            'rgb(142, 36, 170)',
            'rgb(229, 57, 53)',
            'rgba(0, 135, 108, 1)',
            'rgba(239, 146, 80, 1)',
            'rgba(244, 224, 127, 1)',
            'rgba(96, 168, 109, 1)',
            'rgba(233, 119, 76, 1)',
            'rgba(168, 198, 113, 1)',
            'rgba(244, 199, 106, 1)',
            'rgba(132, 183, 110, 1)',
            'rgba(243, 173, 90, 1)',
            'rgba(224, 91, 77, 1)',
            'rgba(60, 152, 109, 1)',
            'rgba(212, 61, 81, 1)',
            'rgba(205, 212, 118, 1)',
            'rgba(255, 99, 132, 1)',
            'rgba(54, 162, 235, 1)',
            'rgba(255, 206, 86, 1)',
            'rgba(75, 192, 192, 1)',
            'rgba(153, 102, 255, 1)',
            'rgba(255, 159, 64, 1)'
          ],
          borderWidth: 1
        }]
      },
      options: {
        legend: {
          position: 'right',
          labels: {
            fontColor: 'white',
            fontSize: 15
          }
        },
        tooltips: {
          titleFontSize: 20,
          bodyFontSize: 20,
          callbacks: {
            label: (tooltipItem, data) => {
              const dataset = data.datasets[tooltipItem.datasetIndex];
              const meta = dataset._meta[Object.keys(dataset._meta)[0]];
              const total = meta.total;
              const currentValue = dataset.data[tooltipItem.index];
              const percentage = parseFloat((currentValue / total * 100).toFixed(1));
              return currentValue + ' (' + percentage + '%)';
            },
            title: (tooltipItem, data) => {
              return data.labels[tooltipItem[0].index];
            }
          }
        }
      }
    });
  }

}
