import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Emprendimiento } from '../modelos/emprendimiento';

@Injectable({
  providedIn: 'root'
})
export class EmprendimientoService {
  private apiUrl = 'http://localhost:8080/api/emprendimientos';
  constructor(private http: HttpClient) {}
  // Consultar todos los emprendimientos
  getEmprendimientos(): Observable<Emprendimiento[]> {
    return this.http.get<Emprendimiento[]>(this.apiUrl);
  }
  // Consultar un emprendimiento por ID
  getEmprendimientoById(id: number): Observable<Emprendimiento> {
    return this.http.get<Emprendimiento>(`${this.apiUrl}/${id}`);
  }
  // Crear un emprendimiento
  createEmprendimiento(formData: FormData): Observable<any> {
    return this.http.post(this.apiUrl, formData);
  }
  // Actualizar un emprendimiento
  updateEmprendimiento(id: number, emprendimiento: Emprendimiento): Observable<Emprendimiento> {
    const params = this.buildHttpParams(emprendimiento);
    return this.http.put<Emprendimiento>(`${this.apiUrl}/${id}`, null, { params });
  }
  // Eliminar un emprendimiento
  deleteEmprendimiento(id: number): Observable<string> {
    return this.http.delete<string>(`${this.apiUrl}/${id}`);
  }
  // Construir parámetros HTTP
  private buildHttpParams(data: Emprendimiento): HttpParams {
    let params = new HttpParams()
      .set('nombre', data.nombre)
      .set('descripcion', data.descripcion)
      .set('idCiudad', data.idCiudad.toString())
      .set('idEstado', data.idEstado.toString());
    if (data.fecha_creacion) {
      params = params.set('fecha_creacion', data.fecha_creacion);
    }
    if (data.fecha_actualizacion) {
      params = params.set('fecha_actualizacion', data.fecha_actualizacion);
    }
    data.idUsuarios.forEach((id, index) => {
      params = params.append(`idUsuarios[${index}]`, id.toString());
    });
    data.idSectores.forEach((id, index) => {
      params = params.append(`idSectores[${index}]`, id.toString());
    });
    return params;
  }
}
