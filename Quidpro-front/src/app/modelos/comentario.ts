export interface Comentario {
  id?: number;
  texto: string;
  fecha_creacion?: string;
  fecha_actualizacion?: string;
  idPublicacion: number;
  idUsuario: number;
  imagenes?: File[]
}
