import { Cuenta } from './cuenta.model';

export interface Movimiento {
  id?: number;
  fecha: Date | string;
  tipoMovimiento: 'Depósito' | 'Retiro' | string;
  valor: number;
  saldo: number;
  numeroCuenta: string;
}
