export interface Emprendimiento {
  id?: number;
  nombre: string;
  descripcion: string;
  fecha_creacion?: string; // Formato (YYYY-MM-DD)
  fecha_actualizacion?: string; // Formato (YYYY-MM-DD)
  idUsuarios: number[];
  idCiudad: number;
  idSectores: number[];
  idEstado: number;
}
