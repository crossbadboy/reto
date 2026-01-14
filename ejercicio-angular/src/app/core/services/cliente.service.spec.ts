import 'zone.js';
import { TestBed } from '@angular/core/testing';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ClienteService } from './cliente.service';
import { Cliente } from '../models/cliente.model';

describe('ClienteService', () => {
  beforeAll(() => {
    TestBed.initTestEnvironment(BrowserDynamicTestingModule, platformBrowserDynamicTesting());
  });
  let service: ClienteService;
  let httpMock: HttpTestingController;
  const mockCliente: Cliente = {
    clienteId: '1',
    nombre: 'Jose',
    genero: 'M',
    edad: 30,
    identificacion: '123',
    direccion: 'Calle 1',
    telefono: '555',
    contrasena: 'pass',
    estado: true
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ClienteService]
    });
    service = TestBed.inject(ClienteService);
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

  it('should get all clientes', () => {
    service.getAll().subscribe(data => {
      expect(data.length).toBe(1);
      expect(data[0]).toEqual(mockCliente);
    });
    const req = httpMock.expectOne(service['API']);
    expect(req.request.method).toBe('GET');
    req.flush([mockCliente]);
  });

  it('should get cliente by id', () => {
    service.getById('1').subscribe(data => {
      expect(data).toEqual(mockCliente);
    });
    const req = httpMock.expectOne(`${service['API']}/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockCliente);
  });

  it('should create a cliente', () => {
    service.create(mockCliente).subscribe(data => {
      expect(data).toEqual(mockCliente);
    });
    const req = httpMock.expectOne(service['API']);
    expect(req.request.method).toBe('POST');
    req.flush(mockCliente);
  });

  it('should update a cliente', () => {
    service.update('1', mockCliente).subscribe(data => {
      expect(data).toEqual(mockCliente);
    });
    const req = httpMock.expectOne(`${service['API']}/1`);
    expect(req.request.method).toBe('PUT');
    req.flush(mockCliente);
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
    service.getById('1').subscribe({
      next: () => fail('should have failed'),
      error: (err) => {
        expect(err.status).toBe(404);
      }
    });
    const req = httpMock.expectOne(`${service['API']}/1`);
    req.flush('Not found', { status: 404, statusText: 'Not Found' });
  });

  it('should handle error on create', () => {
    service.create(mockCliente).subscribe({
      next: () => fail('should have failed'),
      error: (err) => {
        expect(err.status).toBe(400);
      }
    });
    const req = httpMock.expectOne(service['API']);
    req.flush('Bad request', { status: 400, statusText: 'Bad Request' });
  });

  it('should handle error on update', () => {
    service.update('1', mockCliente).subscribe({
      next: () => fail('should have failed'),
      error: (err) => {
        expect(err.status).toBe(400);
      }
    });
    const req = httpMock.expectOne(`${service['API']}/1`);
    req.flush('Bad request', { status: 400, statusText: 'Bad Request' });
  });

  it('should handle error on delete', () => {
    service.delete('1').subscribe({
      next: () => fail('should have failed'),
      error: (err) => {
        expect(err.status).toBe(500);
      }
    });
    const req = httpMock.expectOne(`${service['API']}/1`);
    req.flush('Error', { status: 500, statusText: 'Server Error' });
  });
});
