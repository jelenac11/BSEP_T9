import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { RulesService } from '../core/services/rules.service';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { AddSeverityRuleComponent } from '../add-severity-rule/add-severity-rule.component';
import { AddMessagesRuleComponent } from '../add-messages-rule/add-messages-rule.component';

@Component({
  selector: 'app-rules',
  templateUrl: './rules.component.html',
  styleUrls: ['./rules.component.scss']
})
export class RulesComponent implements OnInit {
  clicked = 'a';
  tabs: string[] = ['Default rules', 'Severity rules', 'Messages rules'];
  defaultRules = [];
  severityRules = [];
  messagesRules = [];
  page = 1;
  size = 10;
  currentTab = 0;
  loggedIn = '';

  constructor(
    private rulesService: RulesService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.getRules();
  }

  getRules(): void {
    if (this.currentTab === 0) {
      this.rulesService.getDefaultRules(this.size, this.page - 1).subscribe((data) => {
        this.defaultRules = data;
      });
    } else if (this.currentTab === 1) {
      this.rulesService.getSeverityRules(this.size, this.page - 1).subscribe((data) => {
        this.severityRules = data;
      });
    } else if (this.currentTab === 2) {
      this.rulesService.getMessagesRules(this.size, this.page - 1).subscribe((data) => {
        this.messagesRules = data;
      });
    }
  }

  changeTab($event: MatTabChangeEvent): void {
    this.currentTab = $event.index;
    this.page = 1;
    this.getRules();
  }

  handlePageChange($event: number): void {
    this.page = $event;
    this.getRules();
  }

  addSeverityRule(): void {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.minHeight = '440px';
    dialogConfig.minWidth = '400px';
    const dialogRef = this.dialog.open(AddSeverityRuleComponent, dialogConfig);
    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        this.getRules();
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
        this.getRules();
      }
    });
  }

}
