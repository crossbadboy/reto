import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cuenta } from '../models/cuenta.model';

@Injectable({ providedIn: 'root' })
export class CuentaService {
  private readonly API = '/api/cuentas';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Cuenta[]> {
    return this.http.get<Cuenta[]>(this.API);
  }

  getById(id: number): Observable<Cuenta> {
    return this.http.get<Cuenta>(`${this.API}/${id}`);
  }

  create(cuenta: Cuenta): Observable<Cuenta> {
    return this.http.post<Cuenta>(this.API, cuenta);
  }

  update(id: number, cuenta: Cuenta): Observable<Cuenta> {
    return this.http.put<Cuenta>(`${this.API}/${id}`, cuenta);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API}/${id}`);
  }
}
