import {Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../core/services/authentication.service';

@Component({
  selector: 'app-callback',
  templateUrl: './callback.component.html',
  styleUrls: ['./callback.component.scss']
})
export class CallbackComponent implements OnInit {

  constructor(
    public authService: AuthService,
    public router: Router
  ) {}

  ngOnInit() {
    this.authService.login();
    this.authService.handleLoginCallback();
    if (this.authService.isAuthenticated) {
      this.router.navigate(['/certificates']).then();
    }
  }

}
