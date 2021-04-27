import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
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

  constructor(
    private fb: FormBuilder,
    private logsService: LogsService
  ) { }

  ngOnInit(): void {
    this.getLogs();

    this.formSearch = this.fb.group({
      search: [""]
    });
  }

  getLogs(): void {
    this.logsService.getLogs(this.size, this.page - 1).subscribe((data: LogsPage) => {
      this.logs = data;
    });
  }

  handlePageChange($event: number): void {
    this.page = $event;
    this.getLogs();
  }

  search(): void {
    console.log("Search");
  }

}
