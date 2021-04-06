import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CertificatesService } from '../core/services/certificates.service';
import { Snackbar } from '../snackbar';

@Component({
  selector: 'app-revoke',
  templateUrl: './revoke.component.html',
  styleUrls: ['./revoke.component.scss']
})
export class RevokeComponent implements OnInit {
  formRevoke: FormGroup;
  submitted = false;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<RevokeComponent>,
    private snackBar: Snackbar,
    private certificatesService: CertificatesService,
    @Inject(MAT_DIALOG_DATA) private data
  ) { }

  ngOnInit(): void {
    this.formRevoke = this.fb.group({
      reason: ["", [Validators.required]],
    });
  }

  get f(): { [key: string]: AbstractControl; } { return this.formRevoke.controls; }

  revoke(): void {
    this.submitted = true;
    if (this.formRevoke.value.reason == "") {
      this.snackBar.error("You have to specify reason.");
      return;
    }
    let revokeObject = { serialNumber: this.data, reason: this.formRevoke.value.reason };
    this.certificatesService.revoke(revokeObject).subscribe((succ: string) => {
      this.snackBar.success('You have successfully revoked certificate.');
      this.dialogRef.close(true);
    },
    error => {
      this.snackBar.error(error.error);
    });
  }

  close(): void {
    this.dialogRef.close(false);
  }

  public reason_change($event) {
    this.formRevoke.patchValue({
      reason: $event.value
    });
  }
}
