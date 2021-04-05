import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  showNavbar = true;

  constructor(
    private router: Router
  ) {
    this.router.events.subscribe((url: any) => {
      if (this.router.url === '/sign-in' ||
      this.router.url.startsWith('/verify-csr/')) {
        this.showNavbar = false;
      } else {
        this.showNavbar = true;
      }
    });
  }
}
