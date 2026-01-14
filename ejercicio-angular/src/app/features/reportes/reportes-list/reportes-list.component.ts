import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReporteService } from '../../../core/services/reporte.service';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import { Reporte } from '../../../core/models/reporte.model';

@Component({
  selector: 'app-reportes-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reportes-list.component.html',
  styleUrls: ['./reportes-list.component.css']
})
export class ReportesListComponent {
  fechaInicio = '';
  fechaFin = '';
  reportes: Reporte[] = [];

  constructor(private reporteService: ReporteService) {}

  generarReporte(): void {
    this.reporteService.getReporte(this.fechaInicio, this.fechaFin)
      .subscribe(data => {
        this.reportes = data;
      });
  }

  descargarPDF(): void {
    if (!this.reportes || this.reportes.length === 0) return;
    const doc = new jsPDF();
    const columns = [ 
      { header: 'Fecha', dataKey: 'fecha' },
      { header: 'Número Cuenta', dataKey: 'numeroCuenta' },
      { header: 'Tipo', dataKey: 'tipo' },
      { header: 'Saldo inicial', dataKey: 'saldoInicial' },
      { header: 'Estado', dataKey: 'estado' },
      { header: 'Movimiento', dataKey: 'movimiento' },
      { header: 'Saldo Disponible', dataKey: 'saldoDisponible' }
    ];
    const rows = this.reportes.map(r => ({
      fecha: r.fecha,
      numeroCuenta: r.numeroCuenta,
      tipo: r.tipo,
      saldoInicial: r.saldoInicial,
      estado: r.estado ? 'Activo' : 'Inactivo',
      movimiento: r.movimiento,
      saldoDisponible: r.saldoDisponible
    }));
    autoTable(doc, {
      columns,
      body: rows,
      styles: { fontSize: 8 },
      headStyles: { fillColor: [41, 128, 185] }
    });
    doc.save('reportes.pdf');
  }
}
