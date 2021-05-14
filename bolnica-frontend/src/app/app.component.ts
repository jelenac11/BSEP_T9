import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './core/services/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  showNavbar = true;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {
    this.router.events.subscribe((url: any) => {
      if (this.router.url.startsWith('/logs') || this.router.url.startsWith('/alarms') || this.router.url.startsWith('/rules') || this.router.url.startsWith('/report') || this.router.url.startsWith('/csr') || this.router.url.startsWith('/messages') || this.router.url.startsWith('/alarms-doctor')) {
        this.showNavbar = true;
      } else {
        this.showNavbar = false;
      }
    });
  }

  ngOnInit() {
    this.authService.renewAuth();
  }
}
