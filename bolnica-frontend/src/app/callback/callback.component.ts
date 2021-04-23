import {Component, OnInit} from '@angular/core';
import { AuthService } from '../core/services/authentication.service';

@Component({
  selector: 'app-callback',
  templateUrl: './callback.component.html',
  styleUrls: ['./callback.component.scss']
})
export class CallbackComponent implements OnInit {

  constructor(public authService: AuthService) {
  }

  ngOnInit() {
    this.authService.handleLoginCallback();
  }

}
