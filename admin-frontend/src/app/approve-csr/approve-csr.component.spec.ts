import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApproveCsrComponent } from './approve-csr.component';

describe('ApproveCsrComponent', () => {
  let component: ApproveCsrComponent;
  let fixture: ComponentFixture<ApproveCsrComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApproveCsrComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproveCsrComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
