import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { SearchComponent } from '../../components/search/search.component';
import { UserPageComponent } from '../../components/user-page/user-page.component';
import { TrendingComponent } from '../../components/trending/trending.component';
import { LoginComponent } from '../../components/login/login.component';
import { WatchlistComponent } from '../../components/user-page/watchlist/watchlist.component';
import { FavouritesComponent } from '../../components/user-page/favourites/favourites.component';
import { WatchedComponent } from '../../components/user-page/watched/watched.component';
import { AuthGuard } from '../../guards/auth/auth.guard';

const routes: Routes = [
  { path: '', component: TrendingComponent },
  { path: 'join', component: LoginComponent },
  { path: 'login', component: LoginComponent },
  { path: 'search', component: SearchComponent },
  { path: 'user-page', component: UserPageComponent, children: [
      { path: '', redirectTo: 'watchlist', pathMatch: 'full' },
      { path: 'watchlist', component: WatchlistComponent },
      { path: 'favourites', component: FavouritesComponent },
      { path: 'watched', component: WatchedComponent }
    ], canActivate: [AuthGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { onSameUrlNavigation: 'reload' })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
