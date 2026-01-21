import 'zone.js';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import { MovimientosListComponent } from './movimientos-list.component';
import { MovimientoService } from '../../../core/services/movimiento.service';
import { of, throwError } from 'rxjs';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

beforeAll(() => {
  TestBed.initTestEnvironment(
    BrowserDynamicTestingModule,
    platformBrowserDynamicTesting()
  );
});

describe('MovimientosListComponent', () => {
  let component: MovimientosListComponent;
  let fixture: ComponentFixture<MovimientosListComponent>;
  let mockMovimientoService: jest.Mocked<MovimientoService>;
  const movimientosMock = [
    { id: 1, tipoMovimiento: 'DEPOSITO', valor: 100, saldo: 1000, numeroCuenta: '123', fecha: '2024-01-01' },
    { id: 2, tipoMovimiento: 'RETIRO', valor: 50, saldo: 950, numeroCuenta: '123', fecha: '2024-01-02' }
  ];

  beforeEach(async () => {
    const spy: Partial<jest.Mocked<MovimientoService>> = {
      getAll: jest.fn().mockReturnValue(of(movimientosMock)),
      delete: jest.fn().mockReturnValue(of({})),
    };
    await TestBed.configureTestingModule({
      imports: [MovimientosListComponent, CommonModule, FormsModule, RouterLink],
      providers: [{ provide: MovimientoService, useValue: spy }]
    }).compileComponents();

    fixture = TestBed.createComponent(MovimientosListComponent);
    component = fixture.componentInstance;
    mockMovimientoService = TestBed.inject(MovimientoService) as jest.Mocked<MovimientoService>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load movimientos on init', () => {
    expect(mockMovimientoService.getAll).toHaveBeenCalled();
    expect(component.movimientos.length).toBe(2);
  });

  it('should filter movimientos by tipoMovimiento', () => {
    component.searchTerm = 'retiro';
    expect(component.filteredMovimientos.length).toBe(1);
    expect(component.filteredMovimientos[0].tipoMovimiento).toBe('RETIRO');
  });

  it('should return all movimientos if searchTerm is empty', () => {
    component.searchTerm = '';
    expect(component.filteredMovimientos.length).toBe(2);
  });

  it('should call delete and reload movimientos on eliminarMovimiento', () => {
    jest.spyOn(window, 'confirm').mockReturnValue(true);
    mockMovimientoService.delete.mockReturnValue(of(undefined));
    mockMovimientoService.getAll.mockReturnValue(of(movimientosMock));
    const loadMovimientosSpy = jest.spyOn(component, 'loadMovimientos');
    component.eliminarMovimiento(1);
    expect(mockMovimientoService.delete).toHaveBeenCalledWith(1);
    expect(loadMovimientosSpy).toHaveBeenCalled();
  });

  it('should not call delete if confirm is false', () => {
    jest.spyOn(window, 'confirm').mockReturnValue(false);
    component.eliminarMovimiento(1);
    expect(mockMovimientoService.delete).not.toHaveBeenCalled();
  });

  it('should handle error on delete', () => {
    jest.spyOn(window, 'confirm').mockReturnValue(true);
    mockMovimientoService.delete.mockImplementation(() => throwError(() => new Error('delete error')));
    jest.spyOn(window, 'alert').mockImplementation(() => {});
    component.eliminarMovimiento(1);
    expect(window.alert).toHaveBeenCalledWith('Error al eliminar movimiento');
  });
});
