import { Cuenta } from './cuenta.model';

describe('Cuenta Model', () => {
  it('should create a Cuenta object with all properties', () => {
    const cuenta: Cuenta = {
      id: 1,
      numeroCuenta: '123',
      tipoCuenta: 'Ahorro',
      saldoInicial: 1000,
      estado: true
    };
    expect(cuenta).toBeTruthy();
    expect(cuenta.tipoCuenta).toBe('Ahorro');
    expect(cuenta.saldoInicial).toBe(1000);
    expect(cuenta.estado).toBe(true);
  });

  it('should allow any tipoCuenta string', () => {
    const cuenta: Cuenta = {
      id: 2,
      numeroCuenta: '456',
      tipoCuenta: 'Especial',
      saldoInicial: 500,
      estado: false
    };
    expect(cuenta.tipoCuenta).toBe('Especial');
    expect(cuenta.estado).toBe(false);
  });
});