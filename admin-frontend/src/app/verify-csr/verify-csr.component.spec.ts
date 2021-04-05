import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerifyCsrComponent } from './verify-csr.component';

describe('VerifyCsrComponent', () => {
  let component: VerifyCsrComponent;
  let fixture: ComponentFixture<VerifyCsrComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VerifyCsrComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VerifyCsrComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
