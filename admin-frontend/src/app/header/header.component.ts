import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from '../core/services/authentication.service';
import { JwtService } from '../core/services/jwt.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  role = '';
  user: any = { email: '', username: '', firstName: '', lastName: '' };

  constructor(
    private authenticationService: AuthenticationService,
    private jwtService: JwtService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.role = this.jwtService.getRole();
    if (this.role) {
      this.authenticationService.getCurrentUser().subscribe((currentUser: any) => {
        this.user = currentUser;
      });
    }
  }

  logout(): void {
    this.authenticationService.logout();
    this.router.navigate(['/sign-in']);
  }

}
