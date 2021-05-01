import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { RulesService } from '../core/services/rules.service';

@Component({
  selector: 'app-rules-doctor',
  templateUrl: './rules-doctor.component.html',
  styleUrls: ['./rules-doctor.component.scss']
})
export class RulesDoctorComponent implements OnInit {
  clicked = 'a';
  tabs: string[] = ['Rules 1', 'Rules 2', 'Rules 3'];
  rules1 = [];
  rules2 = [];
  rules3 = [];
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
      
    } else if (this.currentTab === 1) {
      
    } else if (this.currentTab === 2) {
      
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

  addRule1(): void {
    
  }

  addRule2(): void {
    
  }

  addRule3(): void {
    
  }

}
