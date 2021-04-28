import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { RulesService } from '../core/services/rules.service';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { AddRuleComponent } from '../add-rule/add-rule.component';

@Component({
  selector: 'app-rules',
  templateUrl: './rules.component.html',
  styleUrls: ['./rules.component.scss']
})
export class RulesComponent implements OnInit {
  clicked = 'a';
  tabs: string[] = ['Default rules', 'Created rules'];
  defaultRules = { content: [], totalElements: 0 };
  createdRules = { content: [], totalElements: 0 };
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
        this.defaultRules = { content: data.content, totalElements: data.totalElements };
      });
    } else {
      this.rulesService.getCreatedRules(this.size, this.page - 1).subscribe((data) => {
        this.createdRules = { content: data.content, totalElements: data.totalElements };
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

  addRule(): void {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.minHeight = '440px';
    dialogConfig.minWidth = '400px';
    const dialogRef = this.dialog.open(AddRuleComponent, dialogConfig);
    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        this.getRules();
      }
    });
  }

}
