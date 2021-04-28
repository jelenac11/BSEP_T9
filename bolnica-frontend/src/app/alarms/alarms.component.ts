import { Component, OnInit } from '@angular/core';
import { AlarmsPage } from '../core/model/response/alarms-page.model';
import { AlarmsService } from '../core/services/alarms.service';

@Component({
  selector: 'app-alarms',
  templateUrl: './alarms.component.html',
  styleUrls: ['./alarms.component.scss']
})
export class AlarmsComponent implements OnInit {
  alarms: AlarmsPage = { content: [], totalElements: 0 };
  page = 1;
  size = 10;
  loggedIn = '';

  constructor(
    private alarmsService: AlarmsService
  ) { }

  ngOnInit(): void {
    this.getAlarms();
  }

  search(): void {
    this.page = 1;
    this.getAlarms();
  }

  getAlarms(): void {
    this.alarmsService.getAlarms(this.size, this.page - 1).subscribe((data: AlarmsPage) => {
      this.alarms = data;
    });
  }

  handlePageChange($event: number): void {
    this.page = $event;
    this.getAlarms();
  }

}
