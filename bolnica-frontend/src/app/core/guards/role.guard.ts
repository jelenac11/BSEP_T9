import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(
    private router: Router,
    public auth: AuthService,
  ) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const expectedRoles: string = route.data.expectedRoles;
    
    if (!this.auth.isAuthenticated) {
      this.router.navigate(['/homepage']);
      return false;
    }
    
    const role = this.auth.role;
    const roles: string[] = expectedRoles.split('|', 2);
    let found = false;
    role.forEach((curr) => { 
      if (roles.indexOf(curr) !== -1) {
        found = true;
      } 
    })
    if (!found) {
      if (role.indexOf('read:logs') !== -1) {
        this.router.navigate(['/logs']);
      } else if (role.indexOf('read:messages') !== -1) {
        this.router.navigate(['/messages']);
      }
      return false;
    }
    return true;
  }

}
