import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, UrlSegment } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

import { UserService } from '../../services/user/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  showPart;
  loginForm: FormGroup;
  joinForm: FormGroup;

  constructor(private userService: UserService,
              private router: Router,
              private route: ActivatedRoute,
              private toastr: ToastrService) { }

  ngOnInit() {
    this.route.url
      .subscribe(
        (urlSegments: UrlSegment[]) => {
          this.showPart = urlSegments[0].path;
        }
      );
    this.userService.loginStatus.subscribe(type => {
      this.showPart = type;
    });
    this.initForms();
  }

  resetCredentials(type: string) {
    this.router.navigate(['/' + type]);
  }

  onJoinSubmit() {
    if (this.joinForm.value['password-join'] !== this.joinForm.value['password-confirm']) {
      this.toastr.error('Passwords do not match!', 'Wrong');
      return;
    }
    this.userService.validateJoin(
      this.joinForm.value['username-join'],
      this.joinForm.value['email'],
      this.joinForm.value['password-join'],
      this.joinForm.value['password-confirm']);
    this.joinForm.reset();
  }

  onLoginSubmit() {
    this.userService.validateLogin(
      this.loginForm.value['username-login'],
      this.loginForm.value['password-login']);
    this.joinForm.reset();
    this.router.navigate(['/']);
  }

  private initForms() {
    this.joinForm = new FormGroup({
      'username-join': new FormControl(null, Validators.required),
      'email': new FormControl(null, [Validators.required, Validators.email]),
      'password-join': new FormControl('', Validators.required),
      'password-confirm': new FormControl('', Validators.required)
    }, this.validatePasswords.bind(this));
    this.loginForm = new FormGroup({
      'username-login': new FormControl(null, Validators.required),
      'password-login': new FormControl(null, Validators.required)
    });
  }

  validatePasswords(control: FormControl) {
    const password = control.value['password-join'];
    const confirmPassword = control.value['password-confirm'];

    if (password === '' || password !== confirmPassword) {
      return {
        validatePassword: {
          valid: false
        }
      };
    }

    return null;
  }

}
