import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Publicacion } from '../modelos/publicacion';

@Injectable({
  providedIn: 'root'
})
export class PublicacionService {
  private publicacionUrl = 'http://localhost:8080/api/publicaciones';

  constructor(private http: HttpClient) {}
  // Consultar todas las publicaciones
  getPublicaciones(): Observable<any[]> {
    return this.http.get<any[]>(this.publicacionUrl);
  }
  // Consultar publicación por ID
  getPublicacionById(id: number): Observable<any> {
    return this.http.get<any>(`${this.publicacionUrl}/${id}`);
  }
  // Crear una nueva publicación
  crearPublicacion(formData:FormData): Observable<Publicacion> {
    return this.http.post<Publicacion>(this.publicacionUrl, formData);
  };
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
    return this.http.put<any>(`${this.publicacionUrl}/${id}`, formData);
  }
  // Eliminar una publicación
  eliminarPublicacion(id: number): Observable<string> {
    return this.http.delete<string>(`${this.publicacionUrl}/${id}`);
  }
}
