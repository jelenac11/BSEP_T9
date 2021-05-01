import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlarmsDoctorComponent } from './alarms-doctor.component';

describe('AlarmsDoctorComponent', () => {
  let component: AlarmsDoctorComponent;
  let fixture: ComponentFixture<AlarmsDoctorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AlarmsDoctorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AlarmsDoctorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
