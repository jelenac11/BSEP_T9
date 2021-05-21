import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { LogsPage } from '../core/model/response/logs-page.model';
import { LogsService } from '../core/services/logs.service';

@Component({
  selector: 'app-logs',
  templateUrl: './logs.component.html',
  styleUrls: ['./logs.component.scss']
})
export class LogsComponent implements OnInit {
  logs: LogsPage = { content: [], totalElements: 0 };
  page = 1;
  size = 10;
  loggedIn = '';
  formSearch: FormGroup;
  facilities = ['KERN', 'USER', 'MAIL', 'DAEMON', 'AUTH', 'SYSLOG', 'LPR', 'NEWS',
    'UUCP', 'SOLARISCRON', 'AUTHPRIV', 'FTP', 'NTP', 'CONSOLE', 'SECURITY',
    'CRON', 'LOCAL0', 'LOCAL1', 'LOCAL2', 'LOCAL3', 'LOCAL4', 'LOCAL5', 'LOCAL6', 'LOCAL7'];
  severities = ['DEBUG', 'INFORMATIONAL', 'NOTICE', 'WARNING', 'ERROR', 'CRITICAL', 'ALERT', 'EMERGENCY', 'TRACE'];

  constructor(
    private fb: FormBuilder,
    private logsService: LogsService
  ) { }

  ngOnInit(): void {
    this.formSearch = this.fb.group({
      from: [null],
      to: [null],
      ip: [""],
      facility: [""],
      severity: [""],
      message: [""],
      source: [""],
    });
    this.getLogs();
  }

  search(): void {
    this.page = 1;
    this.getLogs();
  }

  clear(): void {
    this.formSearch = this.fb.group({
      from: [null],
      to: [null],
      ip: [""],
      facility: [""],
      severity: [""],
      message: [""],
      source: [""],
    });
    this.page = 1;
    this.getLogs();
  }

  getLogs(): void {
    let search = { 
      from: this.formSearch.controls.from.value, 
      to: this.formSearch.controls.to.value, 
      ip: this.formSearch.controls.ip.value, 
      facility: this.formSearch.controls.facility.value, 
      severity: this.formSearch.controls.severity.value, 
      message: this.formSearch.controls.message.value,
      source: this.formSearch.controls.source.value 
    };
    this.logsService.getLogs(this.size, this.page - 1, search).subscribe((data: LogsPage) => {
      this.logs = data;
    });
  }

  handlePageChange($event: number): void {
    this.page = $event;
    this.getLogs();
  }

}
