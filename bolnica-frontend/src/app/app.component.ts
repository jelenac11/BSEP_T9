import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './core/services/authentication.service';
import { Snackbar } from './snackbar';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { JwtHelperService } from '@auth0/angular-jwt';
import { filter, mergeMap } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  showNavbar = true;
  stompClient;
  successfullyConnected: boolean;
  subscription: any;

  constructor(
    private router: Router,
    private authService: AuthService,
    private snackbar: Snackbar,
    private http: HttpClient
  ) {
    this.router.events.subscribe((url: any) => {
      if (this.router.url.startsWith('/logs') || this.router.url.startsWith('/alarms') || this.router.url.startsWith('/rules') || this.router.url.startsWith('/report') || this.router.url.startsWith('/csr') || this.router.url.startsWith('/messages') || this.router.url.startsWith('/alarms-doctor') || this.router.url.startsWith('/patients')) {
        this.showNavbar = true;
      } else {
        this.showNavbar = false;
      }
    });

    const socket = new SockJS("https://localhost:8080/ws-doctor");
    this.stompClient = Stomp.over(socket);

    this.stompClient.connect({},
      () =>  {
        let role = authService.role;
        if (role.indexOf('read:alarmsDoctor') !== -1) {
          this.successfullyConnected = true;
          this.subscription = this.stompClient.subscribe(`/send-doctor-alarms`, (message) => {
            if (message.body) {
              let hospital = JSON.parse(localStorage.getItem("user_data")).hospital;
              let department = JSON.parse(localStorage.getItem("user_data")).department;
              if (hospital == JSON.parse(message.body).hospital && department == JSON.parse(message.body).department) {
                this.snackbar.warning("ALARM: " + JSON.parse(message.body).message + " for patient " + JSON.parse(message.body).patient + ".");
              }
            }
            else {
              console.log('Empty body in a websocket message!');
            }
          });
        }
        if (role.indexOf('read:alarms') !== -1) {
          console.log("1");
          this.successfullyConnected = true;
          this.subscription = this.stompClient.subscribe(`/send-doctor-alarms`, (message) => {
            if (message.body) {
              let hospital = JSON.parse(localStorage.getItem("user_data")).hospital;
              console.log(hospital);
              console.log(JSON.parse(message.body).hospital);
              if (hospital == JSON.parse(message.body).hospital || JSON.parse(message.body).hospital == "") {
                this.snackbar.warning("ALARM: " + JSON.parse(message.body).message + ".");
              }
            }
            else {
              console.log('Empty body in a websocket message!');
            }
          });
        }
        
      },
      () => this.successfullyConnected = false
    );
  }

  ngOnInit() {
    this.authService.renewAuth();
  }
}
