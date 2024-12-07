import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PublicacionService {
  private baseUrl = 'http://localhost:8080/api/publicaciones'; // Cambia el puerto si es necesario

  constructor(private http: HttpClient) {}
  // Consultar todas las publicaciones
  getPublicaciones(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl);
  }
  // Consultar publicación por ID
  getPublicacionById(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/${id}`);
  }
  // Crear una nueva publicación
  crearPublicacion(
    titulo: string,
    descripcion: string,
    fecha_creacion: string,
    tag: string,
    idUsuario: number,
    imagenes?: File[]
  ): Observable<any> {
    const formData = new FormData();
    formData.append('titulo', titulo);
    formData.append('descripcion', descripcion);
    formData.append('fecha_creacion', fecha_creacion);
    formData.append('tag', tag);
    formData.append('idUsuario', idUsuario.toString());
    if (imagenes) {
      imagenes.forEach((imagen, index) => {
        formData.append(`imagenes`, imagen, imagen.name);
      });
    }

    return this.http.post<any>(this.baseUrl, formData);
  }
  // Actualizar una publicación existente
  actualizarPublicacion(
    id: number,
    titulo: string,
    descripcion: string,
    fecha_actualizacion: string,
    tag: string,
    idUsuario: number,
    imagenes?: File[]
  ): Observable<any> {
    const formData = new FormData();
    formData.append('titulo', titulo);
    formData.append('descripcion', descripcion);
    formData.append('fecha_actualizacion', fecha_actualizacion);
    formData.append('tag', tag);
    formData.append('idUsuario', idUsuario.toString());
    if (imagenes) {
      imagenes.forEach((imagen, index) => {
        formData.append(`imagenes`, imagen, imagen.name);
      });
    }
    return this.http.put<any>(`${this.baseUrl}/${id}`, formData);
  }
  // Eliminar una publicación
  eliminarPublicacion(id: number): Observable<string> {
    return this.http.delete<string>(`${this.baseUrl}/${id}`);
  }
}
