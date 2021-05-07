import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddLowOxygenLevelRuleComponent } from './add-low-oxygen-level-rule.component';

describe('AddLowOxygenLevelRuleComponent', () => {
  let component: AddLowOxygenLevelRuleComponent;
  let fixture: ComponentFixture<AddLowOxygenLevelRuleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddLowOxygenLevelRuleComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddLowOxygenLevelRuleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
