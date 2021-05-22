import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddLowOxygenLevelTemperatureRuleComponent } from './add-low-oxygen-level-temperature-rule.component';

describe('AddLowOxygenLevelTemperatureRuleComponent', () => {
  let component: AddLowOxygenLevelTemperatureRuleComponent;
  let fixture: ComponentFixture<AddLowOxygenLevelTemperatureRuleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddLowOxygenLevelTemperatureRuleComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddLowOxygenLevelTemperatureRuleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
