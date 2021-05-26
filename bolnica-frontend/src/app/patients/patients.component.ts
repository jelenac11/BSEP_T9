import { Component, OnInit } from '@angular/core';
import { PatientsService } from '../core/services/patients.service';

@Component({
  selector: 'app-patients',
  templateUrl: './patients.component.html',
  styleUrls: ['./patients.component.scss']
})
export class PatientsComponent implements OnInit {
  patients = [];

  constructor(
    private patientsService: PatientsService
  ) { }

  ngOnInit(): void {
    this.patientsService.getPatients().subscribe((data: any) => {
      this.patients = data;
    });
  }

}
