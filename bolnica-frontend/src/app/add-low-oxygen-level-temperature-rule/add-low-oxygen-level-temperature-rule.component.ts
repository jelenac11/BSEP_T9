import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RulesService } from '../core/services/rules.service';
import { Snackbar } from '../snackbar';

@Component({
  selector: 'app-add-low-oxygen-level-temperature-rule',
  templateUrl: './add-low-oxygen-level-temperature-rule.component.html',
  styleUrls: ['./add-low-oxygen-level-temperature-rule.component.scss']
})
export class AddLowOxygenLevelTemperatureRuleComponent implements OnInit {
  formLowOxygenLevelAndTemperatureRule: FormGroup;
  submitted = false;
  
  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AddLowOxygenLevelTemperatureRuleComponent>,
    private snackBar: Snackbar,
    private rulesService: RulesService,
    @Inject(MAT_DIALOG_DATA) private data
  ) { }

  ngOnInit(): void {
    this.formLowOxygenLevelAndTemperatureRule = this.fb.group({
      oxygenLevel: [1],
      temperature: [1],
      message: ["", [Validators.required]]
    });
  }

  get f(): { [key: string]: AbstractControl; } { return this.formLowOxygenLevelAndTemperatureRule.controls; }

  add(): void {
    this.submitted = true;
    if (this.formLowOxygenLevelAndTemperatureRule.invalid) {
      return;
    }
    if (this.formLowOxygenLevelAndTemperatureRule.controls.oxygenLevel.value < 1) {
      this.snackBar.error("Oxygen level min value is 1.")
      return;
    }
    if (this.formLowOxygenLevelAndTemperatureRule.controls.temperature.value < 1) {
      this.snackBar.error("Temperature min value is 1.")
      return;
    }
    if (this.formLowOxygenLevelAndTemperatureRule.controls.oxygenLevel.value > 100) {
      this.snackBar.error("Oxygen level max value is 100.")
      return;
    }
    const rule = { id: null, oxygenLevel: 1, temperature: 1, message: '' };
    rule.oxygenLevel = this.formLowOxygenLevelAndTemperatureRule.value.oxygenLevel;
    rule.temperature = this.formLowOxygenLevelAndTemperatureRule.value.temperature;
    rule.message = this.formLowOxygenLevelAndTemperatureRule.value.message;
    this.rulesService.addLowOxygenLevelAndTemperatureRule(rule).subscribe((data: any) => {
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
