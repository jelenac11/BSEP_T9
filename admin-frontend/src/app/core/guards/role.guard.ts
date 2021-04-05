import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { UserTokenState } from '../model/response/user-token-state.model';
import { JwtService } from 'src/app/core/services/jwt.service';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(
    private router: Router,
    private jwtService: JwtService
  ) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const expectedRoles: string = route.data.expectedRoles;
    const token: UserTokenState = this.jwtService.getToken();
    const jwt: JwtHelperService = new JwtHelperService();
    if (!token) {
      this.router.navigate(['/sign-in']);
      return false;
    }
    const role: string = jwt.decodeToken(token.accessToken).role;
    const roles: string[] = expectedRoles.split('|', 2);
    if (roles.indexOf(role) === -1) {
      this.router.navigate(['/']);
      return false;
    }
    return true;
  }

}
