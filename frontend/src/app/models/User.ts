import { Series } from './Series';

export class User {

  username: string;
  emailAddress: string;
  timeWasted: number;
  registrationDate: Date;
  watchlist: Series[];
  favourites: Series[];
  watchedEpisodesSeries: Series[];

}
