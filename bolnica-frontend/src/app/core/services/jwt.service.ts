import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { UserTokenState } from '../model/response/user-token-state.model';

@Injectable({
  providedIn: 'root'
})
export class JwtService {

  constructor() { }

  getToken(): UserTokenState {
    return JSON.parse(localStorage.getItem('jwtToken'));
  }

  saveToken(userToken: UserTokenState): void {
    localStorage.setItem('jwtToken', JSON.stringify(userToken));
  }

  destroyToken(): void {
    localStorage.removeItem('jwtToken');
  }

  getRole(): string {
    const jwt: JwtHelperService = new JwtHelperService();
    const token: UserTokenState = JSON.parse(localStorage.getItem('jwtToken'));
    if (token) {
      return jwt.decodeToken(token.accessToken).role;
    }
    return '';
  }

  getEmail(): string {
    const jwt: JwtHelperService = new JwtHelperService();
    const token: UserTokenState = JSON.parse(localStorage.getItem('jwtToken'));
    if (token) {
      return jwt.decodeToken(token.accessToken).sub;
    }
    return '';
  }
}
