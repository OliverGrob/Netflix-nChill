import { Episode } from './Episode';
import { Series } from './Series';

export class Season {

  id: number;
  year: Date;
  seasonNumber: number;
  episodes: Episode[];
  series: Series;

}
