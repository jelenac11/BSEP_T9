import { Component, OnInit } from '@angular/core';
import { AlarmsService } from '../core/services/alarms.service';

@Component({
  selector: 'app-alarms-doctor',
  templateUrl: './alarms-doctor.component.html',
  styleUrls: ['./alarms-doctor.component.scss']
})
export class AlarmsDoctorComponent implements OnInit {
  alarms: any = { content: [], totalElements: 0 };
  page = 1;
  size = 10;
  loggedIn = '';

  constructor(
    private alarmsService: AlarmsService
  ) { }

  ngOnInit(): void {
    this.getAlarmsDoctor();
  }

  search(): void {
    this.page = 1;
    this.getAlarmsDoctor();
  }

  getAlarmsDoctor(): void {
    this.alarmsService.getAlarmsDoctor(this.size, this.page - 1).subscribe((data: any) => {
      this.alarms = data;
    });
  }

  handlePageChange($event: number): void {
    this.page = $event;
    this.getAlarmsDoctor();
  }

}
