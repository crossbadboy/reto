import 'zone.js';
import { TestBed } from '@angular/core/testing';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { CuentaService } from './cuenta.service';
import { Cuenta } from '../models/cuenta.model';

describe('CuentaService', () => {
  beforeAll(() => {
    TestBed.initTestEnvironment(BrowserDynamicTestingModule, platformBrowserDynamicTesting());
  });
  let service: CuentaService;
  let httpMock: HttpTestingController;
  const mockCuenta: Cuenta = { id: 1, numeroCuenta: '123', tipoCuenta: 'Ahorro', saldoInicial: 100, estado: true };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CuentaService]
    });
    service = TestBed.inject(CuentaService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    if (httpMock) {
      httpMock.verify();
    }
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all cuentas', () => {
    service.getAll().subscribe(data => {
      expect(data.length).toBe(1);
      expect(data[0]).toEqual(mockCuenta);
    });
    const req = httpMock.expectOne(service['API']);
    expect(req.request.method).toBe('GET');
    req.flush([mockCuenta]);
  });

  it('should get cuenta by id', () => {
    service.getById(1).subscribe(data => {
      expect(data).toEqual(mockCuenta);
    });
    const req = httpMock.expectOne(`${service['API']}/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockCuenta);
  });

  it('should create a cuenta', () => {
    service.create(mockCuenta).subscribe(data => {
      expect(data).toEqual(mockCuenta);
    });
    const req = httpMock.expectOne(service['API']);
    expect(req.request.method).toBe('POST');
    req.flush(mockCuenta);
  });

  it('should update a cuenta', () => {
    service.update(1, mockCuenta).subscribe(data => {
      expect(data).toEqual(mockCuenta);
    });
    const req = httpMock.expectOne(`${service['API']}/1`);
    expect(req.request.method).toBe('PUT');
    req.flush(mockCuenta);
  });

  it('should handle error on getAll', () => {
    service.getAll().subscribe({
      next: () => fail('should have failed'),
      error: (err) => {
        expect(err.status).toBe(500);
      }
    });
    const req = httpMock.expectOne(service['API']);
    req.flush('Error', { status: 500, statusText: 'Server Error' });
  });

  it('should handle error on getById', () => {
    service.getById(1).subscribe({
      next: () => fail('should have failed'),
      error: (err) => {
        expect(err.status).toBe(404);
      }
    });
    const req = httpMock.expectOne(`${service['API']}/1`);
    req.flush('Not found', { status: 404, statusText: 'Not Found' });
  });

  it('should handle error on create', () => {
    service.create(mockCuenta).subscribe({
      next: () => fail('should have failed'),
      error: (err) => {
        expect(err.status).toBe(400);
      }
    });
    const req = httpMock.expectOne(service['API']);
    req.flush('Bad request', { status: 400, statusText: 'Bad Request' });
  });

  it('should handle error on update', () => {
    service.update(1, mockCuenta).subscribe({
      next: () => fail('should have failed'),
      error: (err) => {
        expect(err.status).toBe(400);
      }
    });
    const req = httpMock.expectOne(`${service['API']}/1`);
    req.flush('Bad request', { status: 400, statusText: 'Bad Request' });
  });

  it('should handle error on delete', () => {
    service.delete(1).subscribe({
      next: () => fail('should have failed'),
      error: (err) => {
        expect(err.status).toBe(500);
      }
    });
    const req = httpMock.expectOne(`${service['API']}/1`);
    req.flush('Error', { status: 500, statusText: 'Server Error' });
  });
});
