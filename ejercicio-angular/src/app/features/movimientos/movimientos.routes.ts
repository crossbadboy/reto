import { Routes } from '@angular/router';
import { MovimientosListComponent } from './movimientos-list/movimientos-list.component';
import { MovimientosFormComponent } from './movimientos-form/movimientos-form.component';

export const MOVIMIENTOS_ROUTES: Routes = [
  { path: '', component: MovimientosListComponent },
  { path: 'nuevo', component: MovimientosFormComponent },
  { path: ':id/editar', component: MovimientosFormComponent }
];