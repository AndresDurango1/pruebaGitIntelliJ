import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Departamento } from '../modelos/departamento';

@Injectable({
  providedIn: 'root'
})
export class DepartamentoService {
  private apiUrl = 'http://localhost:8080/api/departamento';
  constructor(private http: HttpClient) {}
  // Obtener todos los departamentos
  getDepartamentos(): Observable<Departamento[]> {
    return this.http.get<Departamento[]>(this.apiUrl);
  }
  // Obtener un departamento por ID
  getDepartamentoById(id: number): Observable<Departamento> {
    return this.http.get<Departamento>(`${this.apiUrl}/${id}`);
  }
  // Crear un nuevo departamento
  crearDepartamento(departamento: Departamento): Observable<Departamento> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<Departamento>(this.apiUrl, departamento, { headers });
  }
  // Actualizar un departamento existente
  editarDepartamento(id: number, departamento: Departamento): Observable<Departamento> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<Departamento>(`${this.apiUrl}/${id}`, departamento, { headers });
  }
  // Eliminar un departamento
  eliminarDepartamento(id: number): Observable<string> {
    return this.http.delete<string>(`${this.apiUrl}/${id}`);
  }
}
