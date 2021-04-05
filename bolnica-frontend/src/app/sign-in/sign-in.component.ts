import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MyErrorStateMatcher } from '../core/error-matchers/ErrorStateMatcher';
import { UserLogin } from '../core/model/request/user-login-request.models';
import { UserTokenState } from '../core/model/response/user-token-state.model';
import { AuthenticationService } from '../core/services/authentication.service';
import { JwtService } from '../core/services/jwt.service';
import { Snackbar } from '../snackbar';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss']
})
export class SignInComponent implements OnInit {
  loginForm: FormGroup;
  submitted = false;
  hidePassword = true;
  matcher: MyErrorStateMatcher = new MyErrorStateMatcher();

  constructor(
    private formBuilder: FormBuilder,
    private snackBar: Snackbar,
    private router: Router,
    private jwtService: JwtService,
    private authenticationService: AuthenticationService
  ) { }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  get f(): { [key: string]: AbstractControl; } { return this.loginForm.controls; }

  onSubmit(): void {
    this.submitted = true;
    if (this.loginForm.invalid) {
      return;
    }
    const credentials: UserLogin = { email: '', password: '' };
    credentials.email = this.loginForm.value.email;
    credentials.password = this.loginForm.value.password;
    this.authenticationService.login(credentials).subscribe((data: UserTokenState) => {
      this.jwtService.saveToken(data);
      this.router.navigate(['/']);
    },
    error => {
      this.snackBar.error(error.error);
    });
  }
}
