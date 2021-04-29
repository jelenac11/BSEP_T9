import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ReportService } from '../core/services/report.service';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss']
})
export class ReportComponent implements OnInit {
  report = { from: 0, to: 0, logs: 0, alarms: 0, debug: 0, informational: 0, notice: 0, warning: 0, error: 0, critical: 0, alert: 0, emergency: 0 };
  reported = false;
  formReport: FormGroup;

  constructor(
    private fb: FormBuilder,
    private reportService: ReportService
  ) { }

  ngOnInit(): void {
    this.formReport = this.fb.group({
      from: [null],
      to: [null]
    });
  }

  onSubmit(): void {
    if (this.formReport.invalid) {
      return;
    }
    this.reported = true;
    let request = { 
      from: this.formReport.controls.from.value, 
      to: this.formReport.controls.to.value
    };
    this.reportService.getReport(request).subscribe((data) => {
      this.report = data;
    });
  }

  createNewReport(): void {
    this.reported = false;
    this.report = { from: 0, to: 0,logs: 0, alarms: 0, debug: 0, informational: 0, notice: 0, warning: 0, error: 0, critical: 0, alert: 0, emergency: 0 };
  }

}
