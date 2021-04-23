import { Component, OnInit } from '@angular/core';
import { AuthService } from '../core/services/authentication.service';

@Component({
  selector: 'app-certificates',
  templateUrl: './certificates.component.html',
  styleUrls: ['./certificates.component.scss']
})
export class CertificatesComponent implements OnInit {

  constructor(public authService: AuthService) {
  }

  ngOnInit() {
    this.authService.handleLoginCallback();
  }

}
