import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Movimiento } from '../models/movimiento.model';

@Injectable({ providedIn: 'root' })
export class MovimientoService {
  private readonly API = '/api/movimientos';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Movimiento[]> {
    return this.http.get<Movimiento[]>(this.API);
  }

  getById(id: number): Observable<Movimiento> {
    return this.http.get<Movimiento>(`${this.API}/${id}`);
  }

  create(movimiento: Movimiento): Observable<Movimiento> {
    return this.http.post<Movimiento>(this.API, movimiento);
  }

  update(id: number, movimiento: Movimiento): Observable<Movimiento> {
    return this.http.put<Movimiento>(`${this.API}/${id}`, movimiento);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API}/${id}`);
  }
}
