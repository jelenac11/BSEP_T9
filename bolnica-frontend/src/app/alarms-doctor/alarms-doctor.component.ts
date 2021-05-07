import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AddLowOxygenLevelRuleComponent } from '../add-low-oxygen-level-rule/add-low-oxygen-level-rule.component';
import { AddPressureRuleComponent } from '../add-pressure-rule/add-pressure-rule.component';
import { AddTemperatureRuleComponent } from '../add-temperature-rule/add-temperature-rule.component';
import { AlarmsService } from '../core/services/alarms.service';
import { RulesService } from '../core/services/rules.service';
import { Snackbar } from '../snackbar';

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
    private alarmsService: AlarmsService,
    private snackBar: Snackbar,
    private dialog: MatDialog
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

  addTemperatureRule(): void {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.minHeight = '440px';
    dialogConfig.minWidth = '400px';
    const dialogRef = this.dialog.open(AddTemperatureRuleComponent, dialogConfig);
    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        this.snackBar.success("Temperature Rule added successfully!");
      }
    });
  }

  addLowOxygenLevelRule(): void {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.minHeight = '440px';
    dialogConfig.minWidth = '400px';
    const dialogRef = this.dialog.open(AddLowOxygenLevelRuleComponent, dialogConfig);
    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        this.snackBar.success("Low Oxygen Level Rule added successfully!");
      }
    });
  }

  addPressureRule(): void {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.minHeight = '440px';
    dialogConfig.minWidth = '400px';
    const dialogRef = this.dialog.open(AddPressureRuleComponent, dialogConfig);
    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        this.snackBar.success("Pressure Rule added successfully!");
      }
    });
  }

}