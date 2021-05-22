import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RulesService } from '../core/services/rules.service';
import { Snackbar } from '../snackbar';

@Component({
  selector: 'app-add-messages-rule',
  templateUrl: './add-messages-rule.component.html',
  styleUrls: ['./add-messages-rule.component.scss']
})
export class AddMessagesRuleComponent implements OnInit {
  formMessagesRule: FormGroup;
  submitted = false;
  
  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AddMessagesRuleComponent>,
    private snackBar: Snackbar,
    private rulesService: RulesService,
    @Inject(MAT_DIALOG_DATA) private data
  ) { }

  ngOnInit(): void {
    this.formMessagesRule = this.fb.group({
      messageRegexBefore: ["", [Validators.required]],
      messageRegexAfter: ["", [Validators.required]],
      timePeriod: [1],
      message: ["", [Validators.required]]
    });
  }

  get f(): { [key: string]: AbstractControl; } { return this.formMessagesRule.controls; }

  add(): void {
    this.submitted = true;
    if (this.formMessagesRule.invalid) {
      return;
    }
    const rule = { messageRegexBefore: '', messageRegexAfter: '', timePeriod: 1, message: '' };
    rule.messageRegexBefore = this.formMessagesRule.value.messageRegexBefore;
    rule.messageRegexAfter = this.formMessagesRule.value.messageRegexAfter;
    rule.timePeriod = this.formMessagesRule.value.timePeriod;
    rule.message = this.formMessagesRule.value.message;
    this.rulesService.addMessagesRule(rule).subscribe((data: any) => {
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
