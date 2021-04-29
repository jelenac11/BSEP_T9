import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddSeverityRuleComponent } from './add-severity-rule.component';

describe('AddSeverityRuleComponent', () => {
  let component: AddSeverityRuleComponent;
  let fixture: ComponentFixture<AddSeverityRuleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddSeverityRuleComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddSeverityRuleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
