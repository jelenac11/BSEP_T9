import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RulesService } from '../core/services/rules.service';
import { Snackbar } from '../snackbar';

@Component({
  selector: 'app-add-pressure-rule',
  templateUrl: './add-pressure-rule.component.html',
  styleUrls: ['./add-pressure-rule.component.scss']
})
export class AddPressureRuleComponent implements OnInit {
  formPressureRule: FormGroup;
  submitted = false;
  
  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AddPressureRuleComponent>,
    private snackBar: Snackbar,
    private rulesService: RulesService,
    @Inject(MAT_DIALOG_DATA) private data
  ) { }

  ngOnInit(): void {
    this.formPressureRule = this.fb.group({
      systolicFrom: [1],
      systolicTo: [1],
      diastolicFrom: [1],
      diastolicTo: [1],
      heartRateFrom: [1],
      heartRateTo: [1],
      message: ["", [Validators.required]]
    });
  }

  get f(): { [key: string]: AbstractControl; } { return this.formPressureRule.controls; }

  add(): void {
    this.submitted = true;
    if (this.formPressureRule.invalid) {
      return;
    }
    if (this.formPressureRule.controls.systolicFrom.value < 1 || this.formPressureRule.controls.systolicTo.value < 1) {
      this.snackBar.error("Systolic min value is 1.")
      return;
    }
    if (this.formPressureRule.controls.systolicTo.value <= this.formPressureRule.controls.systolicFrom.value) {
      this.snackBar.error("Systolic from must be smaller than systolic to.")
      return;
    }
    if (this.formPressureRule.controls.diastolicFrom.value < 1 || this.formPressureRule.controls.diastolicTo.value < 1) {
      this.snackBar.error("Diastolic min value is 1.")
      return;
    }
    if (this.formPressureRule.controls.diastolicTo.value <= this.formPressureRule.controls.diastolicFrom.value) {
      this.snackBar.error("Diastolic from must be smaller than diastolic to.")
      return;
    }
    if (this.formPressureRule.controls.heartRateFrom.value < 1 || this.formPressureRule.controls.heartRateTo.value < 1) {
      this.snackBar.error("Heart rate min value is 1.")
      return;
    }
    if (this.formPressureRule.controls.heartRateTo.value <= this.formPressureRule.controls.heartRateFrom.value) {
      this.snackBar.error("Heart rate from must be smaller than heart rate to.")
      return;
    }
    const rule = { id: null, systolicFrom: 1, systolicTo: 1, diastolicFrom: 1, diastolicTo: 1, heartRateFrom: 1, heartRateTo: 1, message: '', hospital: '' };
    rule.systolicFrom = this.formPressureRule.value.systolicFrom;
    rule.systolicTo = this.formPressureRule.value.systolicTo;
    rule.diastolicFrom = this.formPressureRule.value.diastolicFrom;
    rule.diastolicTo = this.formPressureRule.value.diastolicTo;
    rule.heartRateFrom = this.formPressureRule.value.heartRateFrom;
    rule.heartRateTo = this.formPressureRule.value.heartRateTo;
    rule.message = this.formPressureRule.value.message;
    rule.hospital = JSON.parse(localStorage.getItem("user_data")).hospital;
    this.rulesService.addPressureRule(rule).subscribe((data: any) => {
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
