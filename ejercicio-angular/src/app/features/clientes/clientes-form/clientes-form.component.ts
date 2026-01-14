import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClienteService } from '../../../core/services/cliente.service';
import { Cliente } from '../../../core/models/cliente.model';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-clientes-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './clientes-form.component.html',
  styleUrls: ['./clientes-form.component.css']
})
export class ClientesFormComponent implements OnInit {
  cliente: Cliente = { 
    clienteId: '', 
    nombre: '', 
    identificacion: '', 
    telefono: '', 
    estado: true, 
    genero: 'F',
    edad: 0,
    direccion: '',
    contrasena: '' };

    isEdit = false;

  constructor(
    private clienteService: ClienteService,
    private route: ActivatedRoute,
    public router: Router
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.clienteService.getById(id).subscribe(data => this.cliente = data);
    }
  }

  save(): void {
    if (this.isEdit) {
      this.clienteService.update(this.cliente.clienteId!, this.cliente).subscribe({
        next: () => {
          alert('Cliente actualizado correctamente');
          this.router.navigate(['/clientes']);
        },
        error: (err) => {
          alert('Error al actualizar cliente: ' + (err?.error?.message || err.message || err));
        }
      });
    } else {
      this.clienteService.create(this.cliente).subscribe({
        next: () => {
          alert('Cliente creado correctamente');
          this.router.navigate(['/clientes']);
        },
        error: (err) => {
          alert('Error al crear cliente: ' + (err?.error?.message || err.message || err));
        }
      });
    }
  }
}
