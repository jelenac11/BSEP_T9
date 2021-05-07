import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddTemperatureRuleComponent } from './add-temperature-rule.component';

describe('AddTemperatureRuleComponent', () => {
  let component: AddTemperatureRuleComponent;
  let fixture: ComponentFixture<AddTemperatureRuleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddTemperatureRuleComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddTemperatureRuleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
