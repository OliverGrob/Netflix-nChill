import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

import { User } from '../models/User';

@Injectable({
  providedIn: 'root'
})
export class UserPageService {

  requestUser = new Subject<any>();
  user = new Subject<User>();

  constructor() { }

}
