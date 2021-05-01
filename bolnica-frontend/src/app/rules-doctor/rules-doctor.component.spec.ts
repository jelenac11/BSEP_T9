import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RulesDoctorComponent } from './rules-doctor.component';

describe('RulesDoctorComponent', () => {
  let component: RulesDoctorComponent;
  let fixture: ComponentFixture<RulesDoctorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RulesDoctorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RulesDoctorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
