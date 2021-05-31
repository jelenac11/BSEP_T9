import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import auth0 from 'auth0-js';
import { environment } from 'src/environments/environment';
import { BehaviorSubject, bindNodeCallback } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  userProfile: any;

  auth0 = new auth0.WebAuth({
    clientID: environment.auth.clientID,
    domain: environment.auth.domain,
    responseType: 'token id_token',
    audience: environment.auth.audience,
    redirectUri: environment.auth.redirect,
    scope: 'openid profile'
  });

  private authFlag = 'isLoggedIn';

  token$ = new BehaviorSubject<string>(null);
  userProfile$ = new BehaviorSubject<any>(null);
  onAuthSuccessUrlAdmin = '/logs';
  onAuthSuccessUrlDoctor = '/messages';
  onAuthFailureUrl = '/';
  logoutUrl = environment.auth.logout;

  parseHash$ = bindNodeCallback(this.auth0.parseHash.bind(this.auth0));
  checkSession$ = bindNodeCallback(this.auth0.checkSession.bind(this.auth0));

  constructor(
    public router: Router,
    private http: HttpClient
  ) {}

  public login(): void {
    this.auth0.authorize();
  }

  handleLoginCallback() {
    if (window.location.hash && !this.isAuthenticated) {
      this.parseHash$().subscribe(
        authResult => {
          this.localLogin(authResult);
          if (this.role.includes('read:logs')) {
            this.router.navigate([this.onAuthSuccessUrlAdmin]).then();
          } else if (this.role.includes('read:messages')) {
            this.router.navigate([this.onAuthSuccessUrlDoctor]).then();
          }
        },
        err => this.handleError(err)
      );
    }
  }

  private localLogin(authResult) {
    this.token$.next(authResult.accessToken);
    this.userProfile$.next(authResult.idTokenPayload);
    localStorage.setItem(this.authFlag, JSON.stringify(true));

    const jwt: JwtHelperService = new JwtHelperService();
    localStorage.setItem("role", JSON.stringify(jwt.decodeToken(authResult.accessToken).permissions));
    let username = jwt.decodeToken(authResult.accessToken).sub;
    this.http.get("https://dev-lsmn3kc2.eu.auth0.com/api/v2/users/" + username + "?fields=user_metadata").subscribe(data => {
      localStorage.setItem("user_data", JSON.stringify(JSON.parse(JSON.stringify(data)).user_metadata));
    });
  }

  get isAuthenticated(): boolean {
    return JSON.parse(localStorage.getItem(this.authFlag));
  }

  get role(): any {
    return JSON.parse(localStorage.getItem("role"));
  }

  get userData(): any {
    return JSON.parse(localStorage.getItem("user_data"));
  }

  renewAuth() {
    if (this.isAuthenticated) {
      this.checkSession$({}).subscribe(
        authResult => {
          this.localLogin(authResult);
        },
        err => {
          localStorage.removeItem(this.authFlag);
          this.router.navigate([this.onAuthFailureUrl]).then();
        }
      );
    }
  }

  private localLogout() {
    localStorage.setItem(this.authFlag, JSON.stringify(false));
    localStorage.setItem("role", JSON.stringify(""));
    this.token$.next(null);
    this.userProfile$.next(null);
  }

  logout() {
    this.localLogout();
    this.auth0.logout({
      returnTo: this.logoutUrl,
      clientID: environment.auth.clientID
    });
  }

  private handleError(err) {
    if (err.error_description) {
      console.error(err.error_description);
    } else {
      console.error(JSON.stringify(err));
    }
  }

  public userHasScopes(scopes: Array<string>): boolean {
    const grantedScopes = JSON.parse(localStorage.getItem('scopes')).split(' ');
    return scopes.every(scope => grantedScopes.includes(scope));
  }
}