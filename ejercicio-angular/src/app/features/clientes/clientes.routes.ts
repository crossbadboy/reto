import { Routes } from '@angular/router';
import { ClientesListComponent } from './clientes-list/clientes-list.component';
import { ClientesFormComponent } from './clientes-form/clientes-form.component';

export const CLIENTES_ROUTES: Routes = [
  { path: '', component: ClientesListComponent },
  { path: 'nuevo', component: ClientesFormComponent },
  { path: ':id/editar', component: ClientesFormComponent }
];
