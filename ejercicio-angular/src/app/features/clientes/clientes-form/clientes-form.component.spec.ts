import 'zone.js';
import { TestBed, fakeAsync, tick } from '@angular/core/testing';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import { ComponentFixture } from '@angular/core/testing';
import { ClientesFormComponent } from './clientes-form.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ClienteService } from '../../../core/services/cliente.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { Cliente } from '../../../core/models/cliente.model';

beforeAll(() => {
  TestBed.initTestEnvironment(BrowserDynamicTestingModule, platformBrowserDynamicTesting());
});
describe('ClientesFormComponent', () => {
  let component: ClientesFormComponent;
  let fixture: ComponentFixture<ClientesFormComponent>;
  let clienteService: jest.Mocked<ClienteService>;
  let router: Router;
  let route: ActivatedRoute;
  let paramMapSpy: any;
  const mockCliente: Cliente = {
    clienteId: '1', nombre: 'Juan', genero: 'M', edad: 30, identificacion: '123', direccion: 'Calle 1', telefono: '555', contrasena: 'x', estado: true
  };

  beforeEach(async () => {
    const clienteSpy = {
      getById: jest.fn(),
      create: jest.fn(),
      update: jest.fn(),
      getAll: jest.fn(),
      delete: jest.fn(),
    } as Partial<jest.Mocked<ClienteService>>;
    paramMapSpy = { get: jest.fn() };
    await TestBed.configureTestingModule({
      imports: [ClientesFormComponent, HttpClientTestingModule, RouterTestingModule],
      providers: [
        { provide: ClienteService, useValue: clienteSpy },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: paramMapSpy } } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ClientesFormComponent);
    component = fixture.componentInstance;
    clienteService = TestBed.inject(ClienteService) as jest.Mocked<ClienteService>;
    router = TestBed.inject(Router);
    route = TestBed.inject(ActivatedRoute);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize in create mode if no id', () => {
    expect(component.isEdit).toBe(false);
    expect(component.cliente).toBeDefined();
  });

  it('should initialize in edit mode if id present', () => {
    paramMapSpy.get.mockReturnValue('1');
    clienteService.getById.mockReturnValue(of(mockCliente));
    component.ngOnInit();
    expect(component.isEdit).toBe(true);
    expect(clienteService.getById).toHaveBeenCalledWith('1');
  });

  it('should call create on save if not edit', () => {
    jest.spyOn(window, 'alert').mockImplementation(() => {});
    jest.spyOn(router, 'navigate').mockImplementation(() => Promise.resolve(true));
    clienteService.create.mockReturnValue(of(mockCliente));
    component.isEdit = false;
    component.cliente = { ...mockCliente, clienteId: '' };
    component.save();
    expect(clienteService.create).toHaveBeenCalled();
    expect(window.alert).toHaveBeenCalledWith('Cliente creado correctamente');
    expect(router.navigate).toHaveBeenCalledWith(['/clientes']);
  });

  it('should call update on save if edit', () => {
    jest.spyOn(window, 'alert').mockImplementation(() => {});
    jest.spyOn(router, 'navigate').mockImplementation(() => Promise.resolve(true));
    clienteService.update.mockReturnValue(of(mockCliente));
    component.isEdit = true;
    component.cliente = { ...mockCliente };
    component.save();
    expect(clienteService.update).toHaveBeenCalledWith('1', mockCliente);
    expect(window.alert).toHaveBeenCalledWith('Cliente actualizado correctamente');
    expect(router.navigate).toHaveBeenCalledWith(['/clientes']);
  });

  it('should show error alert on create error', () => {
    jest.spyOn(window, 'alert').mockImplementation(() => {});
    clienteService.create.mockReturnValue(throwError(() => ({ error: { message: 'fail' } })));
    component.isEdit = false;
    component.cliente = { ...mockCliente, clienteId: '' };
    component.save();
    expect(window.alert).toHaveBeenCalledWith('Error al crear cliente: fail');
  });

  it('should show error alert on update error', () => {
    jest.spyOn(window, 'alert').mockImplementation(() => {});
    clienteService.update.mockReturnValue(throwError(() => ({ error: { message: 'fail' } })));
    component.isEdit = true;
    component.cliente = { ...mockCliente };
    component.save();
    expect(window.alert).toHaveBeenCalledWith('Error al actualizar cliente: fail');
  });
});
