import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Cuenta } from '../../../core/models/cuenta.model';
import { CuentaService } from '../../../core/services/cuenta.service';

@Component({
  selector: 'app-cuentas-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cuentas-form.component.html',
  styleUrls: ['./cuentas-form.component.css']
})
export class CuentasFormComponent implements OnInit {
  cuenta: Cuenta = {
    id: 0,
    numeroCuenta: '',
    tipoCuenta: 'Ahorro',
    saldoInicial: 0,
    estado: true
  };

  isEdit = false;

  constructor(
    private cuentaService: CuentaService,
    private route: ActivatedRoute,
    public router: Router
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.cuentaService.getById(+id).subscribe(data => this.cuenta = data);
    }
  }

  save(): void {
    if (this.isEdit) {
      this.cuentaService.update(this.cuenta.id!, this.cuenta).subscribe(() => {
        alert('Cuenta actualizada correctamente');
        this.router.navigate(['/cuentas']);
      });
    } else {
      this.cuentaService.create(this.cuenta).subscribe(() => {
        alert('Cuenta creada correctamente');
        this.router.navigate(['/cuentas']);
      });
    }
  }
}
