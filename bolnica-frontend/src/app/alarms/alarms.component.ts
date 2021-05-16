import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AddMessagesRuleComponent } from '../add-messages-rule/add-messages-rule.component';
import { AddSeverityRuleComponent } from '../add-severity-rule/add-severity-rule.component';
import { AlarmsPage } from '../core/model/response/alarms-page.model';
import { AlarmsService } from '../core/services/alarms.service';
import { Snackbar } from '../snackbar';

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
    private alarmsService: AlarmsService,
    private dialog: MatDialog,
    private snackBar: Snackbar,
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

  addSeverityRule(): void {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.minHeight = '440px';
    dialogConfig.minWidth = '400px';
    const dialogRef = this.dialog.open(AddSeverityRuleComponent, dialogConfig);
    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        this.snackBar.success("Severity Rule added successfully!");
      }
    });
  }

  addMessagesRule(): void {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.minHeight = '440px';
    dialogConfig.minWidth = '400px';
    const dialogRef = this.dialog.open(AddMessagesRuleComponent, dialogConfig);
    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        this.snackBar.success("Messages Rule added successfully!");
      }
    });
  }

}
