import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { Movimiento } from '../../../core/models/movimiento.model';
import { MovimientoService } from '../../../core/services/movimiento.service';

@Component({
  selector: 'app-movimientos-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './movimientos-list.component.html',
  styleUrls: ['./movimientos-list.component.css']
})
export class MovimientosListComponent implements OnInit {
  movimientos: Movimiento[] = [];
  searchTerm = '';

  constructor(private movimientoService: MovimientoService) {}

  ngOnInit(): void {
    this.loadMovimientos();
  }

  loadMovimientos(): void {
    this.movimientoService.getAll().subscribe(data => this.movimientos = data);
  }

  get filteredMovimientos(): Movimiento[] {
    if (!this.searchTerm.trim()) {
      return this.movimientos;
    }
    return this.movimientos.filter(m =>
      m.tipoMovimiento.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
  }

  eliminarMovimiento(id: number): void {
    if (confirm('¿Estás seguro de eliminar este movimiento?')) {
      this.movimientoService.delete(id).subscribe(() => {
        alert('Movimiento eliminado correctamente');
        this.loadMovimientos();
      }, error => {
        alert('Error al eliminar movimiento');
        console.error(error);
      });
    }
  }
}
