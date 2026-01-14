import { Routes } from '@angular/router';
import { ReportesListComponent } from './features/reportes/reportes-list/reportes-list.component';
import { MovimientosListComponent } from './features/movimientos/movimientos-list/movimientos-list.component';
import { CuentasListComponent } from './features/cuentas/cuentas-list/cuentas-list.component';
import { ClientesListComponent } from './features/clientes/clientes-list/clientes-list.component';

export const routes: Routes = [
  { path: '', redirectTo: 'clientes', pathMatch: 'full' },
  {
    path: 'clientes',
    loadChildren: () =>
      import('./features/clientes/clientes.routes').then(m => m.CLIENTES_ROUTES)
  },
  {
    path: 'cuentas',
    loadChildren: () =>
      import('./features/cuentas/cuentas.routes').then(m => m.CUENTAS_ROUTES)
  },
  {
    path: 'movimientos',
    loadChildren: () =>
      import('./features/movimientos/movimientos.routes').then(m => m.MOVIMIENTOS_ROUTES)
  },
  { path: 'reportes', component: ReportesListComponent },
  { path: '**', redirectTo: 'clientes' }
];
