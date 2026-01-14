import 'zone.js';
import { TestBed } from '@angular/core/testing';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import { ComponentFixture } from '@angular/core/testing';
import { CuentasListComponent } from './cuentas-list.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { CuentaService } from '../../../core/services/cuenta.service';
import { of } from 'rxjs';
import { Cuenta } from '../../../core/models/cuenta.model';

beforeAll(() => {
  TestBed.initTestEnvironment(BrowserDynamicTestingModule, platformBrowserDynamicTesting());
});
describe('CuentasListComponent', () => {
  let component: CuentasListComponent;
  let fixture: ComponentFixture<CuentasListComponent>;
  let cuentaService: jest.Mocked<CuentaService>;
  const mockCuentas: Cuenta[] = [
    { id: 1, numeroCuenta: '123', tipoCuenta: 'Ahorro', saldoInicial: 1000, estado: true },
    { id: 2, numeroCuenta: '456', tipoCuenta: 'Corriente', saldoInicial: 2000, estado: false }
  ];

  beforeEach(async () => {
      const spy: Partial<jest.Mocked<CuentaService>> = {
        getAll: jest.fn(),
        getById: jest.fn(),
        create: jest.fn(),
        update: jest.fn(),
        delete: jest.fn(),
      };
    await TestBed.configureTestingModule({
      imports: [CuentasListComponent, HttpClientTestingModule, RouterTestingModule],
      providers: [{ provide: CuentaService, useValue: spy }]
    }).compileComponents();

    fixture = TestBed.createComponent(CuentasListComponent);
    component = fixture.componentInstance;
    cuentaService = TestBed.inject(CuentaService) as jest.Mocked<CuentaService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load cuentas on init', () => {
    cuentaService.getAll.mockReturnValue(of(mockCuentas));
    component.ngOnInit();
    expect(cuentaService.getAll).toHaveBeenCalled();
    expect(component.cuentas.length).toBe(2);
  });

  it('should filter cuentas by numeroCuenta', () => {
    component.cuentas = mockCuentas;
    component.searchTerm = '123';
    const filtered = component.filteredCuentas;
    expect(filtered.length).toBe(1);
    expect(filtered[0].numeroCuenta).toBe('123');
  });

  it('should filter cuentas by tipoCuenta', () => {
    component.cuentas = mockCuentas;
    component.searchTerm = 'corriente';
    const filtered = component.filteredCuentas;
    expect(filtered.length).toBe(1);
    expect(filtered[0].tipoCuenta).toBe('Corriente');
  });

  it('should return all cuentas if searchTerm is empty', () => {
    component.cuentas = mockCuentas;
    component.searchTerm = '';
    const filtered = component.filteredCuentas;
    expect(filtered.length).toBe(2);
  });

  it('should call deleteCuenta and reload cuentas if confirmed', () => {
    jest.spyOn(window, 'confirm').mockReturnValue(true);
    cuentaService.delete.mockReturnValue(of(undefined));
    cuentaService.getAll.mockReturnValue(of(mockCuentas));
    const loadCuentasSpy = jest.spyOn(component, 'loadCuentas');
    component.deleteCuenta(1);
    expect(cuentaService.delete).toHaveBeenCalledWith(1);
    expect(loadCuentasSpy).toHaveBeenCalled();
  });

  it('should not call deleteCuenta if not confirmed', () => {
    jest.spyOn(window, 'confirm').mockReturnValue(false);
    component.cuentas = mockCuentas;
    component.deleteCuenta(1);
    expect(cuentaService.delete).not.toHaveBeenCalled();
  });

  it('should call loadCuentas and set cuentas', () => {
    cuentaService.getAll.mockReturnValue(of(mockCuentas));
    component.loadCuentas();
    expect(cuentaService.getAll).toHaveBeenCalled();
    expect(component.cuentas).toEqual(mockCuentas);
  });
});
