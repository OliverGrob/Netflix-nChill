import { Season } from './Season';
import { Episode } from './Episode';

export class Series {

  id: number;
  title: string;
  description: string;
  status: string;
  airDate: Date;
  seasons: Season[];
  genres: string[];
  image: string;
  allEpisodes: Episode[];

}
