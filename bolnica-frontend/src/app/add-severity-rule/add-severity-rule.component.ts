import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RulesService } from '../core/services/rules.service';
import { Snackbar } from '../snackbar';

@Component({
  selector: 'app-add-severity-rule',
  templateUrl: './add-severity-rule.component.html',
  styleUrls: ['./add-severity-rule.component.scss']
})
export class AddSeverityRuleComponent implements OnInit {
  formSeverityRule: FormGroup;
  submitted = false;
  severities = ['DEBUG', 'INFORMATIONAL', 'NOTICE', 'WARNING', 'ERROR', 'CRITICAL', 'ALERT', 'EMERGENCY'];
  
  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AddSeverityRuleComponent>,
    private snackBar: Snackbar,
    private rulesService: RulesService,
    @Inject(MAT_DIALOG_DATA) private data
  ) { }

  ngOnInit(): void {
    this.formSeverityRule = this.fb.group({
      severity: ["", [Validators.required]],
      timePeriod: [1],
      count: [1],
      message: ["", [Validators.required]]
    });
  }

  get f(): { [key: string]: AbstractControl; } { return this.formSeverityRule.controls; }

  add(): void {
    this.submitted = true;
    if (this.formSeverityRule.invalid) {
      return;
    }
    const rule = { severity: '', timePeriod: 1, count: 1, message: '' };
    rule.severity = this.formSeverityRule.value.severity;
    rule.timePeriod = this.formSeverityRule.value.timePeriod;
    rule.count = this.formSeverityRule.value.count;
    rule.message = this.formSeverityRule.value.message;
    this.rulesService.addSeverityRule(rule).subscribe((data: any) => {
      this.dialogRef.close(true);
    },
    error => {
      this.snackBar.error(error.error);
    });
  }

  close(): void {
    this.dialogRef.close(false);
  }

}
