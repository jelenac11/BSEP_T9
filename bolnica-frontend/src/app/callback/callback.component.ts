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
    this.authService.handleLoginCallback();
    if (this.authService.isAuthenticated) {
      if (this.authService.role.includes('read:logs')) {
        this.router.navigate(['/logs']).then();
      } else if (this.authService.role.includes('read:messages')) {
        this.router.navigate(['/messages']).then();
      }
    }
  }

}
