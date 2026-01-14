import 'zone.js';
import { TestBed } from '@angular/core/testing';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import { ComponentFixture } from '@angular/core/testing';

import { MovimientosFormComponent } from './movimientos-form.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { CuentaService } from '../../../core/services/cuenta.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MovimientoService } from '../../../core/services/movimiento.service';


beforeAll(() => {
  TestBed.initTestEnvironment(BrowserDynamicTestingModule, platformBrowserDynamicTesting());
});
describe('MovimientosFormComponent', () => {
  let component: MovimientosFormComponent;
  let fixture: ComponentFixture<MovimientosFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MovimientosFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('debe cargar cuentas y setear isEdit=false si no hay id', () => {
      const cuentaService = TestBed.inject(CuentaService);
      jest.spyOn(cuentaService, 'getAll').mockReturnValue(of([
        { id: 1, numeroCuenta: '001', tipoCuenta: 'Ahorro', saldoInicial: 1000, estado: true }
      ]));
      const route = TestBed.inject(ActivatedRoute);
      route.params = of({});
      component.ngOnInit();
      expect(component.cuentas).toEqual([
        { id: 1, numeroCuenta: '001', tipoCuenta: 'Ahorro', saldoInicial: 1000, estado: true }
      ]);
      expect(component.isEdit).toBe(false);
    });

    it('debe setear isEdit=true y cargar movimiento si hay id', () => {
      const cuentaService = TestBed.inject(CuentaService);
      jest.spyOn(cuentaService, 'getAll').mockReturnValue(of([]));
      const route = TestBed.inject(ActivatedRoute);
      route.params = of({ id: 5 });
      const loadMovimientoSpy = jest.spyOn(component, 'loadMovimiento').mockImplementation();
      component.ngOnInit();
      expect(component.isEdit).toBe(true);
      expect(component.movimientoId).toBe(5);
      expect(loadMovimientoSpy).toHaveBeenCalledWith(5);
    });
  });

  describe('loadMovimiento', () => {
    it('debe cargar el movimiento por id', () => {
      const movimientoService = TestBed.inject(MovimientoService);
      jest.spyOn(movimientoService, 'getById').mockReturnValue(of({
        id: 7,
        fecha: '',
        tipoMovimiento: '',
        valor: 0,
        saldo: 0,
        numeroCuenta: ''
      }));
      component.loadMovimiento(7);
      expect(component.movimiento).toEqual({
        id: 7,
        fecha: '',
        tipoMovimiento: '',
        valor: 0,
        saldo: 0,
        numeroCuenta: ''
      });
    });
  });

  describe('save', () => {
    it('debe actualizar movimiento si isEdit=true', () => {
      component.isEdit = true;
      component.movimientoId = 2;
      const movimientoService = TestBed.inject(MovimientoService);
      const router = TestBed.inject(Router);
      jest.spyOn(movimientoService, 'update').mockReturnValue({
        subscribe: (handlers: any) => { handlers.next && handlers.next(); return undefined; }
      } as any);
      const navigateSpy = jest.spyOn(router, 'navigate').mockImplementation();
      global.alert = jest.fn();
      component.save();
      expect(global.alert).toHaveBeenCalledWith('Movimiento actualizado correctamente');
      expect(navigateSpy).toHaveBeenCalledWith(['/movimientos']);
    });

    it('debe mostrar error si update falla', () => {
      component.isEdit = true;
      component.movimientoId = 2;
      const movimientoService = TestBed.inject(MovimientoService);
      jest.spyOn(movimientoService, 'update').mockReturnValue({
        subscribe: (handlers: any) => { handlers.error && handlers.error({ error: { message: 'fail' } }); return undefined; }
      } as any);
      global.alert = jest.fn();
      component.save();
      expect(global.alert).toHaveBeenCalledWith('Error al actualizar movimiento: fail');
    });

    it('debe crear movimiento si isEdit=false', () => {
      component.isEdit = false;
      const movimientoService = TestBed.inject(MovimientoService);
      const router = TestBed.inject(Router);
      jest.spyOn(movimientoService, 'create').mockReturnValue({
        subscribe: (handlers: any) => { handlers.next && handlers.next(); return undefined; }
      } as any);
      const navigateSpy = jest.spyOn(router, 'navigate').mockImplementation();
      global.alert = jest.fn();
      component.save();
      expect(global.alert).toHaveBeenCalledWith('Movimiento creado correctamente');
      expect(navigateSpy).toHaveBeenCalledWith(['/movimientos']);
    });

    it('debe mostrar error si create falla', () => {
      component.isEdit = false;
      const movimientoService = TestBed.inject(MovimientoService);
      jest.spyOn(movimientoService, 'create').mockReturnValue({
        subscribe: (handlers: any) => { handlers.error && handlers.error({ error: { message: 'fail' } }); return undefined; }
      } as any);
      global.alert = jest.fn();
      component.save();
      expect(global.alert).toHaveBeenCalledWith('Error al crear movimiento: fail');
    });
  });
});
