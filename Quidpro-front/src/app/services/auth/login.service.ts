import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, BehaviorSubject, throwError, catchError, tap } from 'rxjs';
import { Login } from '../../modelos/login';
import { Usuario } from '../../modelos/usuario';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  currentUserLoginOn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  currentUserData: BehaviorSubject<Usuario> = new BehaviorSubject<Usuario>(
    {
      id: 0,
      nombres: '',
      apellidos: '',
      direccion: '',
      correo: '',
      telefono: [],
      idCiudad: 0,
      idRol: [],
    }
  );
  constructor(private http: HttpClient) { }
  login(credentials: Login): Observable<Usuario> {
    return this.http.get<Usuario>('assets/data.json').pipe(
      tap((userData:Usuario) =>{
        this.currentUserData.next(userData);
        this.currentUserLoginOn.next(true);
      }),
      catchError(this.handleError)
    )
  }
  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      console.error('Ocurrio un error ', error.error);
    }
    else {
      console.log('El servidor retornó el código de estado', error.status, error.error);
    }
    return throwError(() => new Error('Algo falló intenta nuevamente'));
  }

  get userData(): Observable<Usuario> {
    return this.currentUserData.asObservable();
  }
  get userLoginOn(): Observable<boolean> {
    return this.currentUserLoginOn.asObservable();
  }

}
