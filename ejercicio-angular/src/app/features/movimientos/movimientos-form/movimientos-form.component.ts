import { Component, OnInit } from '@angular/core';
import { Movimiento } from '../../../core/models/movimiento.model';
import { MovimientoService } from '../../../core/services/movimiento.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CuentaService } from '../../../core/services/cuenta.service';
import { Cuenta } from '../../../core/models/cuenta.model';

@Component({
  selector: 'app-movimiento-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './movimientos-form.component.html',
  styleUrls: ['./movimientos-form.component.css']
})
export class MovimientosFormComponent implements OnInit {
  movimiento: Movimiento = {
    fecha: '',
    tipoMovimiento: '',
    valor: 0,
    saldo: 0,
    numeroCuenta: '',
  };
  isEdit = false;
  movimientoId?: number;
  cuentas: Cuenta[] = [];

  constructor(
    private movimientoService: MovimientoService,
    private router: Router,
    private route: ActivatedRoute,
    private cuentaService: CuentaService
  ) {}

  ngOnInit(): void {
    this.cuentaService.getAll().subscribe(data => this.cuentas = data);
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEdit = true;
        this.movimientoId = +params['id'];
        this.loadMovimiento(this.movimientoId);
      }
    });
  }

  loadMovimiento(id: number): void {
    this.movimientoService.getById(id).subscribe(mov => {
      this.movimiento = mov;
    });
  }

  save(): void {
    if (this.isEdit && this.movimientoId) {
      this.movimientoService.update(this.movimientoId, this.movimiento).subscribe({
        next: () => {
          alert('Movimiento actualizado correctamente');
        this.router.navigate(['/movimientos']);
        },
        error: (err) => {
          alert('Error al actualizar movimiento: ' + (err?.error?.message || err.message || err));
        }
      });
    } else {
      this.movimientoService.create(this.movimiento).subscribe({
        next: () => {
          alert('Movimiento creado correctamente');
          this.router.navigate(['/movimientos']);
        },
        error: (err) => {
          alert('Error al crear movimiento: ' + (err?.error?.message || err.message || err));
        }
      });
    }
  }

  cancelar(): void {
    this.router.navigate(['/movimientos']);
  }
}
