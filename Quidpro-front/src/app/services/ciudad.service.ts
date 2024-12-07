import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Ciudad } from '../modelos/ciudad'; // Ajusta la ruta según tu estructura de proyecto

@Injectable({
  providedIn: 'root'
})
export class CiudadService {
  private apiUrl = 'http://localhost:8080/api/ciudades'; // Cambia el puerto si es necesario
  constructor(private http: HttpClient) {}
  // Obtener todas las ciudades
  getCiudades(): Observable<Ciudad[]> {
    return this.http.get<Ciudad[]>(this.apiUrl);
  }
  // Obtener ciudades por idDepartamento
  getCiudadesPorDepartamento(idDepartamento: number): Observable<Ciudad[]> {
    return this.http.get<Ciudad[]>(`${this.apiUrl}/por-departamento/${idDepartamento}`);
  }
  // Obtener una ciudad por ID
  getCiudadById(id: number): Observable<Ciudad> {
    return this.http.get<Ciudad>(`${this.apiUrl}/${id}`);
  }
  // Crear una nueva ciudad
  crearCiudad(ciudad: Ciudad, idDepartamento: number): Observable<Ciudad> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<Ciudad>(`${this.apiUrl}?idDepartamento=${idDepartamento}`, ciudad, { headers });
  }
  // Actualizar una ciudad existente
  actualizarCiudad(
    id: number,
    ciudad: Ciudad,
    idDepartamento?: number
  ): Observable<Ciudad> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const url = idDepartamento
      ? `${this.apiUrl}/${id}?idDepartamento=${idDepartamento}`
      : `${this.apiUrl}/${id}`;
    return this.http.put<Ciudad>(url, ciudad, { headers });
  }
  // Eliminar una ciudad
  eliminarCiudad(id: number): Observable<string> {
    return this.http.delete<string>(`${this.apiUrl}/${id}`);
  }
}
