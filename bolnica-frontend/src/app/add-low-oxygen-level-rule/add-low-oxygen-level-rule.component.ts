import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RulesService } from '../core/services/rules.service';
import { Snackbar } from '../snackbar';

@Component({
  selector: 'app-add-low-oxygen-level-rule',
  templateUrl: './add-low-oxygen-level-rule.component.html',
  styleUrls: ['./add-low-oxygen-level-rule.component.scss']
})
export class AddLowOxygenLevelRuleComponent implements OnInit {
  formLowOxygenLevelRule: FormGroup;
  submitted = false;
  
  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AddLowOxygenLevelRuleComponent>,
    private snackBar: Snackbar,
    private rulesService: RulesService,
    @Inject(MAT_DIALOG_DATA) private data
  ) { }

  ngOnInit(): void {
    this.formLowOxygenLevelRule = this.fb.group({
      oxygenLevel: [1],
      message: ["", [Validators.required]]
    });
  }

  get f(): { [key: string]: AbstractControl; } { return this.formLowOxygenLevelRule.controls; }

  add(): void {
    this.submitted = true;
    if (this.formLowOxygenLevelRule.invalid) {
      return;
    }
    if (this.formLowOxygenLevelRule.controls.oxygenLevel.value < 1) {
      this.snackBar.error("Oxygen level min value is 1.")
      return;
    }
    if (this.formLowOxygenLevelRule.controls.oxygenLevel.value > 100) {
      this.snackBar.error("Oxygen level max value is 100.")
      return;
    }
    const rule = { id: null, oxygenLevel: 1, message: '' };
    rule.oxygenLevel = this.formLowOxygenLevelRule.value.oxygenLevel;
    rule.message = this.formLowOxygenLevelRule.value.message;
    this.rulesService.addLowOxygenLevelRule(rule).subscribe((data: any) => {
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
