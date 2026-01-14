import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Cuenta } from '../../../core/models/cuenta.model';
import { CuentaService } from '../../../core/services/cuenta.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-cuentas-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './cuentas-list.component.html',
  styleUrls: ['./cuentas-list.component.css']
})
export class CuentasListComponent implements OnInit {
  cuentas: Cuenta[] = [];
  searchTerm = '';

  constructor(private cuentaService: CuentaService) {}

  ngOnInit(): void {
    this.loadCuentas();
  }

  loadCuentas(): void {
    this.cuentaService.getAll().subscribe(data => this.cuentas = data);
  }

  get filteredCuentas(): Cuenta[] {
    return this.cuentas.filter(c =>
      c.numeroCuenta.includes(this.searchTerm) ||
      c.tipoCuenta.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
  }

  deleteCuenta(id: number): void {
    if (confirm('¿Eliminar esta cuenta?')) {
      this.cuentaService.delete(id).subscribe(() => this.loadCuentas());
    }
  }
}
