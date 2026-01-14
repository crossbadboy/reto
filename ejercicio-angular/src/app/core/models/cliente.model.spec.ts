import { Cliente } from './cliente.model';

describe('Cliente Model', () => {
  it('should create a Cliente object with all properties', () => {
    const cliente: Cliente = {
      clienteId: '1',
      nombre: 'Juan',
      genero: 'M',
      edad: 30,
      identificacion: '123456',
      direccion: 'Calle 1',
      telefono: '555-1234',
      contrasena: 'pass',
      estado: true
    };
    expect(cliente).toBeTruthy();
    expect(cliente.nombre).toBe('Juan');
    expect(cliente.genero).toBe('M');
    expect(cliente.edad).toBe(30);
    expect(cliente.estado).toBe(true);
  });

  it('should allow optional clienteId', () => {
    const cliente: Cliente = {
      nombre: 'Ana',
      genero: 'F',
      edad: 25,
      identificacion: '654321',
      direccion: 'Calle 2',
      telefono: '555-5678',
      contrasena: 'pass2',
      estado: false
    };
    expect(cliente.clienteId).toBeUndefined();
    expect(cliente.nombre).toBe('Ana');
    expect(cliente.genero).toBe('F');
    expect(cliente.estado).toBe(false);
  });
});