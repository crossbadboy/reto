import { Routes } from '@angular/router';
import { CuentasListComponent } from './cuentas-list/cuentas-list.component';
import { CuentasFormComponent } from './cuentas-form/cuentas-form.component';

export const CUENTAS_ROUTES: Routes = [
  { path: '', component: CuentasListComponent },
  { path: 'nuevo', component: CuentasFormComponent },
  { path: ':id/editar', component: CuentasFormComponent }
];