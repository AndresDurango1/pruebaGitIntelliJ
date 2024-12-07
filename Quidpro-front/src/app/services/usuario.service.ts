import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Usuario } from '../modelos/usuario';

@Injectable({
  providedIn: 'root',
})
export class UsuarioService {
  private apiUsuarios = 'http://localhost:8080/api/usuarios';
  private apiCiudades = 'http://localhost:8080/api/ciudades';
  private apiDepartamentos = 'http://localhost:8080/api/departamentos';
  private apiRoles = 'http://localhost:8080/api/roles';

  constructor(private http: HttpClient) {}
  // Consultar todos los usuarios
  getUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.apiUsuarios}`);
  }
  // Consultar un usuario por ID
  getUsuarioById(id: number): Observable<any> {
    return this.http.get(`${this.apiUsuarios}/${id}`);
  }
  // Crear un nuevo usuario
  createUsuario(formData: FormData): Observable<any> {
    return this.http.post(this.apiUsuarios, formData);
  }
  // Actualizar un usuario existente
  updateUsuario(id: number, formData: FormData): Observable<any> {
    return this.http.put(`${this.apiUsuarios}/${id}`, formData);
  }
  // Eliminar un usuario
  deleteUsuario(id: number): Observable<any> {
    return this.http.delete(`${this.apiUsuarios}/${id}`);
  }
  // Consultar todas las ciudades por departamento
  getDepartamentos(): Observable<any> {
    return this.http.get(`${this.apiDepartamentos}`);
  }
  // Consultar todas las ciudades
  getCiudades(): Observable<any> {
    return this.http.get(`${this.apiCiudades}`);
  }
  // Consultar todos los roles
  getRoles(): Observable<any> {
    return this.http.get(`${this.apiRoles}`);
  }
}
