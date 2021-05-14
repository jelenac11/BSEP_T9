import { Component, OnInit } from '@angular/core';
import { AuthService } from '../core/services/authentication.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  user: any = null;

  constructor(
    public authService: AuthService,
  ) { }

  ngOnInit(): void {
    if (this.authService.isAuthenticated) {
      this.authService.userProfile$.subscribe(
        (profile) => {
          this.user = profile;
        }
      );
    }
  }

  logout(): void {
    this.authService.logout();
  }

  switchApp(): void {
    window.location.replace('https://localhost:4205/logs');
  }

}
