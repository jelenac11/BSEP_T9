import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddMessagesRuleComponent } from './add-messages-rule.component';

describe('AddMessagesRuleComponent', () => {
  let component: AddMessagesRuleComponent;
  let fixture: ComponentFixture<AddMessagesRuleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddMessagesRuleComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddMessagesRuleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
