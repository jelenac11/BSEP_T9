import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpClient, HttpHeaders } from '@angular/common/http';
import { EMPTY, Observable } from 'rxjs';
import { AuthService } from '../services/authentication.service';
import { filter, mergeMap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class HttpTokenInterceptor implements HttpInterceptor {
  constructor(
    private authService: AuthService,
    private http: HttpClient
  ) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let headers = new HttpHeaders({'Content-Type': 'application/json'});
    if (req.url.startsWith("https://dev-lsmn3kc2.eu.auth0.com/api/v2/users/")) {
      let request = {
        "client_id": "tsCd8kuR6ELNl5VvNdMWsZbmdu7bQiVb",
        "client_secret": "lPeVJRoqDJAMsCqiDAC4AZuxoK2JTUURMwd9FtVg9_Lk13ED4LS8gm6yCmpmbUW0",
        "audience": "https://dev-lsmn3kc2.eu.auth0.com/api/v2/",
        "grant_type": "client_credentials"
      }
      return this.http.post("https://dev-lsmn3kc2.eu.auth0.com/oauth/token", request, {headers: headers}).pipe(
        mergeMap(token => {
          const newReq = req.clone({
            setHeaders: {'Authorization': `Bearer ${JSON.parse(JSON.stringify(token)).access_token}`}
          });
          return next.handle(newReq);
        })
      );
    }
    if (!this.authService.isAuthenticated) {
      return EMPTY;
    } else {
      return this.authService.token$.pipe(
        filter(token => typeof token === 'string'),
        mergeMap(token => {
          const newReq = req.clone({
            setHeaders: {Authorization: `Bearer ${token}`}
          });
          return next.handle(newReq);
        })
      );
    }
  }
}
