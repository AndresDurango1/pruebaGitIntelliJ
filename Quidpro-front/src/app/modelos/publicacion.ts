export interface Publicacion {
  id?: number;
  titulo: string;
  descripcion: string;
  fecha_creacion?: string;
  fecha_actualizacion?: string;
  tag: string;
  idUsuario: number;
  imagenes?: File[]
}
