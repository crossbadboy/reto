import 'zone.js';
import { TestBed } from '@angular/core/testing';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { MovimientoService } from './movimiento.service';
import { Movimiento } from '../models/movimiento.model';

describe('MovimientoService', () => {
  let service: MovimientoService;
  let httpMock: HttpTestingController;
  const mockMovimiento: Movimiento = {
    id: 1,
    fecha: '2024-01-01',
    tipoMovimiento: 'Depósito',
    valor: 100,
    saldo: 200,
    numeroCuenta: '123'
  };

  beforeAll(() => {
    TestBed.initTestEnvironment(BrowserDynamicTestingModule, platformBrowserDynamicTesting());
  });

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [MovimientoService]
    });
    service = TestBed.inject(MovimientoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all movimientos', () => {
    service.getAll().subscribe(data => {
      expect(data.length).toBe(1);
      expect(data[0]).toEqual(mockMovimiento);
    });
    const req = httpMock.expectOne(service['API']);
    expect(req.request.method).toBe('GET');
    req.flush([mockMovimiento]);
  });

  it('should get movimiento by id', () => {
    service.getById(1).subscribe(data => {
      expect(data).toEqual(mockMovimiento);
    });
    const req = httpMock.expectOne(`${service['API']}/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockMovimiento);
  });

  it('should create a movimiento', () => {
    service.create(mockMovimiento).subscribe(data => {
      expect(data).toEqual(mockMovimiento);
    });
    const req = httpMock.expectOne(service['API']);
    expect(req.request.method).toBe('POST');
    req.flush(mockMovimiento);
  });

  it('should update a movimiento', () => {
    service.update(1, mockMovimiento).subscribe(data => {
      expect(data).toEqual(mockMovimiento);
    });
    const req = httpMock.expectOne(`${service['API']}/1`);
    expect(req.request.method).toBe('PUT');
    req.flush(mockMovimiento);
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
    service.create(mockMovimiento).subscribe({
      next: () => fail('should have failed'),
      error: (err) => {
        expect(err.status).toBe(400);
      }
    });
    const req = httpMock.expectOne(service['API']);
    req.flush('Bad request', { status: 400, statusText: 'Bad Request' });
  });

  it('should handle error on update', () => {
    service.update(1, mockMovimiento).subscribe({
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
