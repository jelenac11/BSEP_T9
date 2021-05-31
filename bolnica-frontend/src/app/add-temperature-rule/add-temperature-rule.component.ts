import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RulesService } from '../core/services/rules.service';
import { Snackbar } from '../snackbar';

@Component({
  selector: 'app-add-temperature-rule',
  templateUrl: './add-temperature-rule.component.html',
  styleUrls: ['./add-temperature-rule.component.scss']
})
export class AddTemperatureRuleComponent implements OnInit {
  formTemperatureRule: FormGroup;
  submitted = false;
  higher = "true";
  
  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AddTemperatureRuleComponent>,
    private snackBar: Snackbar,
    private rulesService: RulesService,
    @Inject(MAT_DIALOG_DATA) private data
  ) { }

  ngOnInit(): void {
    this.formTemperatureRule = this.fb.group({
      temperature: [1],
      message: ["", [Validators.required]]
    });
  }

  get f(): { [key: string]: AbstractControl; } { return this.formTemperatureRule.controls; }

  add(): void {
    this.submitted = true;
    if (this.formTemperatureRule.invalid) {
      return;
    }
    if (this.formTemperatureRule.controls.temperature.value < 1) {
      this.snackBar.error("Temperature min value is 1.")
      return;
    }
    const rule = { id: null, temperature: 1, message: '', hospital: '' };
    rule.temperature = this.formTemperatureRule.value.temperature;
    rule.message = this.formTemperatureRule.value.message;
    rule.hospital = JSON.parse(localStorage.getItem("user_data")).hospital;
    if (this.higher == "true") {
      this.rulesService.addTemperatureRuleHigher(rule).subscribe((data: any) => {
        this.dialogRef.close(true);
      },
      error => {
        this.snackBar.error(error.error);
      });
    } else if (this.higher == "false") {
      this.rulesService.addTemperatureRuleLower(rule).subscribe((data: any) => {
        this.dialogRef.close(true);
      },
      error => {
        this.snackBar.error(error.error);
      });
    }
  }

  close(): void {
    this.dialogRef.close(false);
  }

  changeHigher(isHigher): void {
    this.higher = isHigher;
  }

}
