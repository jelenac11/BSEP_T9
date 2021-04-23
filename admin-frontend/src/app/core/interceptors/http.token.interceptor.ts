import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';
import { EMPTY, Observable } from 'rxjs';
import { AuthService } from '../services/authentication.service';
import { filter, mergeMap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class HttpTokenInterceptor implements HttpInterceptor {
  constructor(
    private authService: AuthService
  ) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!this.authService.isAuthenticated) {
      return EMPTY;
    }
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
