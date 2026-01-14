import 'zone.js';
import { TestBed } from '@angular/core/testing';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';

import { ReporteService } from './reporte.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('Reporte', () => {
  let service: ReporteService;

  beforeAll(() => {
    TestBed.initTestEnvironment(BrowserDynamicTestingModule, platformBrowserDynamicTesting());
  });

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(ReporteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
