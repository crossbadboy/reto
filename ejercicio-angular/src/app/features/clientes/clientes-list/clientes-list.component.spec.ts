import 'zone.js';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';

beforeAll(() => {
  TestBed.initTestEnvironment(
    BrowserDynamicTestingModule,
    platformBrowserDynamicTesting()
  );
});
import { ClientesListComponent } from './clientes-list.component';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ClienteService } from '../../../core/services/cliente.service';
import { of } from 'rxjs';
import { Cliente } from '../../../core/models/cliente.model';

describe('ClientesListComponent', () => {
  let component: ClientesListComponent;
  let fixture: ComponentFixture<ClientesListComponent>;
  let clienteService: jest.Mocked<ClienteService>;
  const mockClientes: Cliente[] = [
    {
      clienteId: '1', nombre: 'Juan', genero: 'M', edad: 30, identificacion: '123', direccion: 'Calle 1', telefono: '555', contrasena: 'x', estado: true
    },
    {
      clienteId: '2', nombre: 'Ana', genero: 'F', edad: 25, identificacion: '456', direccion: 'Calle 2', telefono: '666', contrasena: 'y', estado: false
    }
  ];

  beforeEach(async () => {
    const spy: Partial<jest.Mocked<ClienteService>> = {
      getAll: jest.fn(),
      getById: jest.fn(),
      create: jest.fn(),
      update: jest.fn(),
      delete: jest.fn(),
    };
    await TestBed.configureTestingModule({
      imports: [ClientesListComponent, HttpClientTestingModule, RouterTestingModule],
      providers: [{ provide: ClienteService, useValue: spy }]
    }).compileComponents();

    fixture = TestBed.createComponent(ClientesListComponent);
    component = fixture.componentInstance;
    clienteService = TestBed.inject(ClienteService) as jest.Mocked<ClienteService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load clientes on init', () => {
    (clienteService.getAll as jest.Mock).mockImplementation(() => of(mockClientes));
    component.ngOnInit();
    expect(clienteService.getAll).toHaveBeenCalled();
    expect(component.clientes.length).toBe(2);
  });

  it('should filter clientes by nombre', () => {
    component.clientes = mockClientes;
    component.searchTerm = 'juan';
    const filtered = component.filteredClientes;
    expect(filtered.length).toBe(1);
    expect(filtered[0].nombre).toBe('Juan');
  });

  it('should filter clientes by identificacion', () => {
    component.clientes = mockClientes;
    component.searchTerm = '456';
    const filtered = component.filteredClientes;
    expect(filtered.length).toBe(1);
    expect(filtered[0].identificacion).toBe('456');
  });

  it('should filter clientes by telefono', () => {
    component.clientes = mockClientes;
    component.searchTerm = '555';
    const filtered = component.filteredClientes;
    expect(filtered.length).toBe(1);
    expect(filtered[0].telefono).toBe('555');
  });

  it('should filter clientes by direccion', () => {
    component.clientes = mockClientes;
    component.searchTerm = 'calle 2';
    const filtered = component.filteredClientes;
    expect(filtered.length).toBe(1);
    expect(filtered[0].direccion).toBe('Calle 2');
  });

  it('should return all clientes if searchTerm is empty', () => {
    component.clientes = mockClientes;
    component.searchTerm = '';
    const filtered = component.filteredClientes;
    expect(filtered.length).toBe(2);
  });

  it('should call deleteCliente and reload clientes if confirmed', () => {
    jest.spyOn(window, 'confirm').mockReturnValue(true);
    clienteService.delete.mockReturnValue(of(undefined));
    clienteService.getAll.mockReturnValue(of(mockClientes));
    const loadClientesSpy = jest.spyOn(component, 'loadClientes');
    component.deleteCliente('1');
    expect(clienteService.delete).toHaveBeenCalledWith('1');
    expect(loadClientesSpy).toHaveBeenCalled();
  });

  it('should not call deleteCliente if not confirmed', () => {
    jest.spyOn(window, 'confirm').mockReturnValue(false);
    component.clientes = mockClientes;
    component.deleteCliente('1');
    expect(clienteService.delete).not.toHaveBeenCalled();
  });

  it('should call loadClientes and set clientes', () => {
    (clienteService.getAll as jest.Mock).mockImplementation(() => of(mockClientes));
    component.loadClientes();
    expect(clienteService.getAll).toHaveBeenCalled();
    expect(component.clientes).toEqual(mockClientes);
  });
});
