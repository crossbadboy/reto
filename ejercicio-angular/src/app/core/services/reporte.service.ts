import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { saveAs } from 'file-saver';
import { Reporte } from '../models/reporte.model';

@Injectable({ providedIn: 'root' })
export class ReporteService {
  private readonly API = '/api/reportes';

  constructor(private http: HttpClient) {}

  getReporte(fechaInicio: string, fechaFin: string): Observable<Reporte[]> {
    return this.http.get<Reporte[]>(`${this.API}/porfecha?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`);
  }

  downloadPDF(base64: string, filename = 'reporte.pdf'): void {
    const byteArray = Uint8Array.from(atob(base64), c => c.charCodeAt(0));
    const blob = new Blob([byteArray], { type: 'application/pdf' });
    saveAs(blob, filename);
  }
}
