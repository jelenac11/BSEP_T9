import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LogConfigService } from '../core/services/log-config.service';
import { Snackbar } from '../snackbar';

@Component({
  selector: 'app-log-config',
  templateUrl: './log-config.component.html',
  styleUrls: ['./log-config.component.scss']
})
export class LogConfigComponent implements OnInit {
  logConfig = { hospital: "", filePath: "", interval: 1, regexp: "" };
  formLog: FormGroup;
  hospitals = [];
  paths = ["logging1.log", "logging2.log", "logging3.log"];

  constructor(
    private fb: FormBuilder,
    private snackbar: Snackbar,
    private logService: LogConfigService
  ) { }

  ngOnInit(): void {
    this.logService.getHospitals().subscribe((data) => {
      console.log(data)
      this.hospitals = data;
    });
    this.formLog = this.fb.group({
      hospital: ["", Validators.required],
      filePath: ["", Validators.required],
      interval: [1],
      regexp: ["", Validators.required]
    });
  }

  onSubmit(): void {
    if (this.formLog.invalid) {
      return;
    }
    let log = { 
      hospital: this.formLog.controls.hospital.value,
      filePath: "../simulator/" + this.formLog.controls.filePath.value,
      interval: this.formLog.controls.interval.value * 1000,
      regexp: this.formLog.controls.regexp.value,
    };
    this.logService.createLogConfig(log).subscribe((data) => {
      this.snackbar.success("Successfully created log configuration.");
      this.formLog.reset();
      this.formLog.controls.hospital.setErrors(null);
      this.formLog.controls.filePath.setErrors(null);
      this.formLog.controls.regexp.setErrors(null);

    });
  }

}
