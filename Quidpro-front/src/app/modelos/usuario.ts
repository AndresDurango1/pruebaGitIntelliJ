export interface Usuario {
  id: number;
  nombres: string;
  apellidos: string;
  direccion: string;
  correo: string;
  telefono: string[];
  identificacion?: string;
  usuario?: string;
  contrasena?: string;
  idCiudad: number;
  idRol: number[];
  imagenUsuario?: {
    id: number;
    titulo: string;
    url_imagenUsuario: string;
  };
  nombreCompleto?: string;
}
