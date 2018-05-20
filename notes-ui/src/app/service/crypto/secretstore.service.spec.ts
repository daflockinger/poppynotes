import { TestBed, inject } from '@angular/core/testing';

import { SecretstoreService } from './secretstore.service';

describe('SecretstoreService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SecretstoreService]
    });
  });

  it('should be created', inject([SecretstoreService], (service: SecretstoreService) => {
    expect(service).toBeTruthy();
  }));
});
