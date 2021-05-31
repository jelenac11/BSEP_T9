import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MessagesService } from '../core/services/messages.service';
import { PatientsService } from '../core/services/patients.service';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.scss']
})
export class MessagesComponent implements OnInit {
  messages: any = { content: [], totalElements: 0 };
  page = 1;
  size = 10;
  loggedIn = '';
  formFilter: FormGroup;
  patients = []; 

  constructor(
    private fb: FormBuilder,
    private messagesService: MessagesService,
    private patientsService: PatientsService
  ) { }

  ngOnInit(): void {
    this.formFilter = this.fb.group({
      patient: [""],
    });
    this.getPatients();
    this.getMessages();
  }

  filter(): void {
    this.page = 1;
    this.getMessages();
  }

  clear(): void {
    this.formFilter = this.fb.group({
      patient: [""],
    });
    this.page = 1;
    this.getMessages();
  }

  getMessages(): void {
    let filter = { 
      id: this.formFilter.controls.patient.value 
    };
    this.messagesService.getMessages(this.size, this.page - 1, filter).subscribe((data: any) => {
      this.messages = data;
    });
  }

  getPatients(): void {
    this.patientsService.getPatients().subscribe((data: any) => {
      this.patients = data;
    });
  }

  handlePageChange($event: number): void {
    this.page = $event;
    this.getMessages();
  }

}