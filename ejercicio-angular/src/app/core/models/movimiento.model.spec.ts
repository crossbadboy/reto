import { Movimiento } from './movimiento.model';

describe('Movimiento Model', () => {
  it('should create a Movimiento object with all properties', () => {
    const movimiento: Movimiento = {
      id: 1,
      fecha: new Date(),
      tipoMovimiento: 'Depósito',
      valor: 100,
      saldo: 1100,
      numeroCuenta: '123'
    };
    expect(movimiento).toBeTruthy();
    expect(movimiento.tipoMovimiento).toBe('Depósito');
    expect(movimiento.valor).toBe(100);
    expect(movimiento.saldo).toBe(1100);
  });

  it('should allow string as fecha', () => {
    const movimiento: Movimiento = {
      fecha: '2026-01-12',
      tipoMovimiento: 'Retiro',
      valor: 50,
      saldo: 1050,
      numeroCuenta: '123'
    };
    expect(typeof movimiento.fecha).toBe('string');
    expect(movimiento.tipoMovimiento).toBe('Retiro');
  });
});