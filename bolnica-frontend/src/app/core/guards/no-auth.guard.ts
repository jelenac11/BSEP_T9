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
      if (this.auth.role.includes('write:csr')) {
        this.router.navigate(['/csr']);
      } else {
        this.router.navigate(['/messages']);
      }
      return false;
    }
    return true;
  }

}
