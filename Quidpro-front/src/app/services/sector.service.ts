import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Sector } from '../modelos/sector';

@Injectable({
  providedIn: 'root'
})
export class SectorService {
  private apiSectores = 'http://localhost:8080/api/sectores';

  constructor(private http: HttpClient) { }
  //Consultar todos los sectores
  getSectores():Observable<Sector[]> {
  return this.http.get<Sector[]>(`${this.apiSectores}`)
  }

}
