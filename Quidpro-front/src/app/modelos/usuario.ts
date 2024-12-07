export interface Usuario {
  id: number;
  nombres: string;
  apellidos: string;
  direccion: string;
  correo: string;
  telefono: number[];
  idCiudad: number;
  idRol: number[];
  imagen?: File;
}
