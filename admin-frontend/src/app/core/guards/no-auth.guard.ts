import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class NoAuthGuard implements CanActivate {

  constructor(
    private router: Router,
    public auth: AuthService
  ) {}

  canActivate(): boolean {
    if (this.auth.isAuthenticated) {
      this.router.navigate(['/certificates']);
      return false;
    }
    return true;
  }

}
