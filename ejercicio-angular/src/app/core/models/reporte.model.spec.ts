import { Reporte } from './reporte.model';

describe('Reporte Model', () => {
  it('should create a Reporte object with all properties', () => {
    const reporte: Reporte = {
      fecha: '2026-01-12',
      cliente: 'Juan',
      numeroCuenta: '123',
      tipo: 'Ahorro',
      saldoInicial: 1000,
      estado: true,
      movimiento: 100,
      saldoDisponible: 1100
    };
    expect(reporte).toBeTruthy();
    expect(reporte.cliente).toBe('Juan');
    expect(reporte.estado).toBe(true);
    expect(reporte.saldoDisponible).toBe(1100);
  });

  it('should allow estado false', () => {
    const reporte: Reporte = {
      fecha: '2026-01-12',
      cliente: 'Ana',
      numeroCuenta: '456',
      tipo: 'Corriente',
      saldoInicial: 500,
      estado: false,
      movimiento: 50,
      saldoDisponible: 450
    };
    expect(reporte.estado).toBe(false);
    expect(reporte.tipo).toBe('Corriente');
  });
});