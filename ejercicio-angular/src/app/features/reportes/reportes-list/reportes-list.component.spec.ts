// Mock jsPDF y autoTable globalmente para Jest
const jsPDFMock = jest.fn().mockImplementation(() => ({ save: jest.fn() }));
const autoTableMock = jest.fn();
jest.mock('jspdf', () => ({ __esModule: true, default: jsPDFMock }));
jest.mock('jspdf-autotable', () => ({ __esModule: true, default: autoTableMock }));
// Mock window.URL.createObjectURL para jsPDF/FileSaver en Jest
beforeAll(() => {
  if (!global.URL) {
    global.URL = {} as any;
  }
  if (!global.URL.createObjectURL) {
    global.URL.createObjectURL = jest.fn();
  }
});
// Polyfill for TextEncoder/TextDecoder for Jest (Node)
import { TextEncoder, TextDecoder } from 'util';
if (typeof global.TextEncoder === 'undefined') {
  global.TextEncoder = TextEncoder;
}
if (typeof global.TextDecoder === 'undefined') {
  global.TextDecoder = TextDecoder;
}
import 'zone.js';
import { TestBed } from '@angular/core/testing';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import { ComponentFixture } from '@angular/core/testing';
import { ReportesListComponent } from './reportes-list.component';
import { ReporteService } from '../../../core/services/reporte.service';
import { of } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';

beforeAll(() => {
  TestBed.initTestEnvironment(BrowserDynamicTestingModule, platformBrowserDynamicTesting());
});
describe('ReportesListComponent', () => {
  let component: ReportesListComponent;
  let fixture: ComponentFixture<ReportesListComponent>;
  let reporteServiceSpy: jest.Mocked<ReporteService>;

  beforeEach(async () => {
      const spy: Partial<jest.Mocked<ReporteService>> = {
        getReporte: jest.fn(),
        downloadPDF: jest.fn(),
      };
    await TestBed.configureTestingModule({
      imports: [ReportesListComponent,
        HttpClientTestingModule, RouterTestingModule
      ],
      providers: [
        { provide: ReporteService, useValue: spy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ReportesListComponent);
    component = fixture.componentInstance;
    reporteServiceSpy = TestBed.inject(ReporteService) as jest.Mocked<ReporteService>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('debe llamar a getReporte y actualizar reportes', () => {
    const mockReportes = [
      { fecha: '2024-01-01', cliente: 'Juan', numeroCuenta: '123', tipo: 'Ahorros', saldoInicial: 100, estado: true, movimiento: 100, saldoDisponible: 200 }
    ];
    reporteServiceSpy.getReporte.mockReturnValue(of(mockReportes));
    component.fechaInicio = '2024-01-01';
    component.fechaFin = '2024-01-31';
    component.generarReporte();
    expect(reporteServiceSpy.getReporte).toHaveBeenCalledWith('2024-01-01', '2024-01-31');
    expect(component.reportes).toEqual(mockReportes);
  });

  describe('descargarPDF', () => {
    let jsPDFMock: any;
    let autoTableMock: jest.SpyInstance;
    beforeAll(() => {
      jest.resetModules();
    });
    beforeEach(() => {
      jsPDFMock = {
        save: jest.fn()
      };
      jest.spyOn(require('jspdf'), 'default').mockImplementation(() => jsPDFMock);
      autoTableMock = jest.spyOn(require('jspdf-autotable'), 'default').mockImplementation(() => undefined);
    });
    afterEach(() => {
      jest.restoreAllMocks();
    });

    it('no hace nada si reportes está vacío', () => {
      component.reportes = [];
      component.descargarPDF();
      expect(jsPDFMock.save).not.toHaveBeenCalled();
      expect(autoTableMock).not.toHaveBeenCalled();
    });

    it('genera y descarga PDF si hay reportes', () => {
      component.reportes = [
        { fecha: '2024-01-01', cliente: 'Juan', numeroCuenta: '123', tipo: 'Ahorros', saldoInicial: 100, estado: true, movimiento: 100, saldoDisponible: 200 }
      ];
      component.descargarPDF();
      expect(autoTableMock).toHaveBeenCalled();
      expect(jsPDFMock.save).toHaveBeenCalledWith('reportes.pdf');
    });
  });
});
