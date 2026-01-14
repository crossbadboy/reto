import 'zone.js';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import { CuentasFormComponent } from './cuentas-form.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { CuentaService } from '../../../core/services/cuenta.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { Cuenta } from '../../../core/models/cuenta.model';

describe('CuentasFormComponent', () => {
  beforeAll(() => {
    TestBed.initTestEnvironment(BrowserDynamicTestingModule, platformBrowserDynamicTesting());
  });
  let component: CuentasFormComponent;
  let fixture: ComponentFixture<CuentasFormComponent>;
  let cuentaService: jest.Mocked<CuentaService>;
  let router: Router;
  let route: ActivatedRoute;
  let paramMapSpy: any;
  const mockCuenta: Cuenta = {
    id: 1,
    numeroCuenta: '123',
    tipoCuenta: 'Ahorro',
    saldoInicial: 1000,
    estado: true
  };

  beforeEach(async () => {
    const cuentaSpy: Partial<jest.Mocked<CuentaService>> = {
      getById: jest.fn(),
      create: jest.fn(),
      update: jest.fn(),
      getAll: jest.fn(),
      delete: jest.fn(),
    };
    paramMapSpy = { get: jest.fn() };
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule],
      providers: [
        { provide: CuentaService, useValue: cuentaSpy },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: paramMapSpy } } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CuentasFormComponent);
    component = fixture.componentInstance;
    cuentaService = TestBed.inject(CuentaService) as jest.Mocked<CuentaService>;
    router = TestBed.inject(Router);
    route = TestBed.inject(ActivatedRoute);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize in create mode if no id', () => {
    expect(component.isEdit).toBe(false);
    expect(component.cuenta).toBeDefined();
  });

  it('should initialize in edit mode if id present', () => {
    paramMapSpy.get.mockReturnValue('1');
    cuentaService.getById.mockReturnValue(of(mockCuenta));
    component.ngOnInit();
    expect(component.isEdit).toBe(true);
    expect(cuentaService.getById).toHaveBeenCalledWith(1);
  });

  it('should call create on save if not edit', () => {
    jest.spyOn(window, 'alert').mockImplementation(() => {});
    jest.spyOn(router, 'navigate').mockImplementation(() => Promise.resolve(true));
    cuentaService.create.mockReturnValue(of(mockCuenta));
    component.isEdit = false;
    component.cuenta = { ...mockCuenta, id: 0 };
    component.save();
    expect(cuentaService.create).toHaveBeenCalled();
    expect(window.alert).toHaveBeenCalledWith('Cuenta creada correctamente');
    expect(router.navigate).toHaveBeenCalledWith(['/cuentas']);
  });

  it('should call update on save if edit', () => {
    jest.spyOn(window, 'alert').mockImplementation(() => {});
    jest.spyOn(router, 'navigate').mockImplementation(() => Promise.resolve(true));
    cuentaService.update.mockReturnValue(of(mockCuenta));
    component.isEdit = true;
    component.cuenta = { ...mockCuenta };
    component.save();
    expect(cuentaService.update).toHaveBeenCalledWith(1, mockCuenta);
    expect(window.alert).toHaveBeenCalledWith('Cuenta actualizada correctamente');
    expect(router.navigate).toHaveBeenCalledWith(['/cuentas']);
  });
});
