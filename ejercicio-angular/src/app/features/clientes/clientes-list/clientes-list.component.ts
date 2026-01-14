import { Component, OnInit } from '@angular/core';
import { Cliente } from '../../../core/models/cliente.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClienteService } from '../../../core/services/cliente.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-clientes-list',
  standalone: true,  
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './clientes-list.component.html',
  styleUrls: ['./clientes-list.component.css']
})
export class ClientesListComponent implements OnInit {
  clientes: Cliente[] = [];
  searchTerm = '';

  constructor(private clienteService: ClienteService) {
  }

  ngOnInit(): void {
    this.loadClientes();
  }

  loadClientes(): void {
    this.clienteService.getAll().subscribe(data => this.clientes = data);
  }

  get filteredClientes(): Cliente[] {
    return this.clientes.filter(c =>
      c.nombre.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      c.identificacion.includes(this.searchTerm) ||
      c.telefono.includes(this.searchTerm) ||
      c.direccion.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
  }

  deleteCliente(id: string): void {
    if (confirm('¿Eliminar este cliente?')) {
      this.clienteService.delete(id).subscribe(() => this.loadClientes());
    }
  }
}
