import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Estado } from '../modelos/estado';

@Injectable({
  providedIn: 'root'
})
export class EstadoService {
  private apiEstados = 'http://localhost:8080/api/estados'
  constructor(private http: HttpClient) { }
  // Obtener todos los estados
  getEstados(): Observable<Estado[]> {
    return this.http.get<Estado[]>(`${this.apiEstados}`)
  }
}
