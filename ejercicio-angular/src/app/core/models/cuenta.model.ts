import { Cliente } from './cliente.model';

export interface Cuenta {
  id: number;
  numeroCuenta: string;
  tipoCuenta: 'Ahorro' | 'Corriente' | string; 
  saldoInicial: number;
  estado: boolean;
}
