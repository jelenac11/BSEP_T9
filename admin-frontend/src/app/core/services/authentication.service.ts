import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { UserLogin } from '../model/request/user-login-request.models';
import { UserTokenState } from '../model/response/user-token-state.model';
import { JwtService } from './jwt.service';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(
    private http: HttpClient,
    private jwtService: JwtService
  ) { }

  login(user: UserLogin): Observable<UserTokenState> {
    return this.http.post(`${environment.auth_url}login`, user, {headers: this.headers, responseType: 'json'});
  }

  logout(): void {
    this.jwtService.destroyToken();
  }

  isAuthenticated(): boolean {
    if (!this.jwtService.getToken()) {
      return false;
    }
    return true;
  }

  getCurrentUser(): Observable<any> {
    return this.http.get(`${environment.auth_url}current-user`, { responseType: 'json' });
  }

  getRole(): string {
    return this.jwtService.getRole();
  }

}
