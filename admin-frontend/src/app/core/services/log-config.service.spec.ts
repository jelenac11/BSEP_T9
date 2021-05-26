import { TestBed } from '@angular/core/testing';

import { LogConfigService } from './log-config.service';

describe('LogConfigService', () => {
  let service: LogConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LogConfigService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
