import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPressureRuleComponent } from './add-pressure-rule.component';

describe('AddPressureRuleComponent', () => {
  let component: AddPressureRuleComponent;
  let fixture: ComponentFixture<AddPressureRuleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddPressureRuleComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddPressureRuleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
