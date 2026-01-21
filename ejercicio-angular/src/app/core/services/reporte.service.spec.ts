import 'zone.js';
import { TestBed } from '@angular/core/testing';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';

import { ReporteService } from './reporte.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('ReporteService', () => {
  let service: ReporteService;
  let httpMock: HttpTestingController;

  beforeAll(() => {
    TestBed.initTestEnvironment(BrowserDynamicTestingModule, platformBrowserDynamicTesting());
  });

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(ReporteService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call getReporte and return data', () => {
    const mockResponse = [{ numeroCuenta: '123', fecha: '2024-01-01' }];
    service.getReporte('2024-01-01', '2024-01-31').subscribe(data => {
      expect(data).toEqual(mockResponse);
    });
    const req = httpMock.expectOne('/api/reportes/porfecha?fechaInicio=2024-01-01&fechaFin=2024-01-31');
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('should download PDF using downloadPDF', () => {
    const base64 = btoa('test-pdf-content');
    const saveAsSpy = jest.spyOn(require('file-saver'), 'saveAs').mockImplementation(jest.fn());
    service.downloadPDF(base64, 'test.pdf');
    expect(saveAsSpy).toHaveBeenCalled();
    saveAsSpy.mockRestore();
  });
});
