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
      if (this.router.url.startsWith('/certificates')) {
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
