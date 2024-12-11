import { Component } from '@angular/core';
import { UsuarioService } from '../../services/usuario.service';
import { DepartamentoService } from '../../services/departamento.service';
import { CiudadService } from '../../services/ciudad.service';
import { RolService } from '../../services/rol.service';
import { Usuario } from '../../modelos/usuario';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-usuario',
  standalone: false,
  templateUrl: './usuario.component.html',
  styleUrl: './usuario.component.css'
})
export class UsuarioComponent {
  editarUsuarioForm: FormGroup;
  usuarios: Usuario[] = [];
  selectedUsuario: Usuario = {} as Usuario;
  departamentos: any[] = [];
  ciudades: any[] = [];
  roles: any[] = [];
  imagen: File[] = [];
  previews: string[] = [];
  selectedFile: File | null = null;
  isOpenModal: boolean = false;

  constructor(private fb: FormBuilder, private usuarioService: UsuarioService, private departamentoService: DepartamentoService, private ciudadService: CiudadService, private rolService: RolService) {
    this.editarUsuarioForm = this.fb.group({
      nombres: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50), Validators.pattern('^[A-Za-z]+( [A-Za-z]+)*$')]],
      apellidos: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20), Validators.pattern('^[A-Za-z]+( [A-Za-z]+)*$')]],
      usuario: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20), Validators.pattern('[a-zA-Z0-9]+')]],
      contrasena: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20), Validators.pattern('[A-Za-z0-9\s\W]+')]],
      identificacion: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20), Validators.pattern('[0-9]+')]],
      direccion: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50), Validators.pattern('[A-Za-z0-9\W]+')]],
      correo: ['', [Validators.required, Validators.email, Validators.pattern('[a-zAA-Z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$')]],
      telefono: ['', [Validators.required, Validators.minLength(7), Validators.maxLength(10), Validators.pattern('^[0-9]{7,10}$')]],
      departamento: ['', Validators.required],
      ciudad: ['', Validators.required],
      rol: ['', Validators.required],
      imagen: ['', Validators.required]
    });
    console.log(this.editarUsuarioForm);
  }
  ngOnInit(): void {
    this.usuarioService.getUsuarios().subscribe((data) => (this.usuarios = data));
    console.log(this.usuarios);
    this.cargarDepartamentos();
    this.cargarRoles();
  }
  //Metodos auxiliares para gestionar actualización de usuario
  openModal(usuario: Usuario): void {
    this.selectedUsuario = { ...usuario };
    this.isOpenModal = true;
    this.editarUsuarioForm.patchValue({
      nombres: usuario.nombres,
      apellidos: usuario.apellidos,
      usuario: usuario.usuario,
      direccion: usuario.direccion,
      correo: usuario.correo,
      telefono: usuario.telefono,
    });
    console.log('Usuario editado:', this.selectedUsuario);
  }
  cerrarModal() {
    this.isOpenModal = false;
  }
  cargarDepartamentos() {
    this.departamentoService.getDepartamentos().subscribe((data) => {
      console.log('Departamentos:', data);
      this.departamentos = data;
    });
  }
  onDepartamentoChange() {
    const departamentoId = this.editarUsuarioForm.get('departamento')?.value;
    this.cargarCiudades(departamentoId);
    console.log(this.editarUsuarioForm);
  }
  cargarCiudades(departamentoId: number) {
    console.log('Cargando ciudades para el departamento:', departamentoId);
    this.ciudadService.getCiudadesPorDepartamento(departamentoId).subscribe(
      (data) => {
        console.log('Ciudades recibidas:', data);
        this.ciudades = data;
      },
      (error) => {
        console.error('Error al cargar las ciudades:', error);
      }
    );
  }
  cargarRoles() {
    this.rolService.getRoles().subscribe((data) => {
      console.log('Roles:', data);
      this.roles = data;
    });
  }
  onFileChange(event: any): void {
    this.selectedFile = event.target.files[0];
    const input = event.target as HTMLInputElement;
    if (input.files) {
      const validFiles: File[] = [];
      const previews: string[] = [];

      Array.from(input.files).forEach((file) => {
        if (file.type.startsWith('image/') && file.size <= 2 * 1024 * 1024) {
          validFiles.push(file);
          previews.push(URL.createObjectURL(file));
        } else {
          console.warn(`Archivo no válido: ${file.name}`);
        }
      });
      this.imagen = validFiles;
      this.previews = previews;
      console.log('Imágenes válidas seleccionadas:', this.imagen);
    }
  }
  //Metodo para actualizar usuario
  editarUsuario(): void {
    if (this.selectedUsuario && this.selectedUsuario.id) {
      const formData = new FormData();
      console.log("Ingresando al metodo editar ")
      formData.append('nombres', this.editarUsuarioForm.get('nombres')?.value);
      formData.append('apellidos', this.editarUsuarioForm.get('apellidos')?.value);
      formData.append('usuario', this.editarUsuarioForm.get('usuario')?.value);
      formData.append('contrasena', this.editarUsuarioForm.get('contrasena')?.value);
      formData.append('identificacion', this.editarUsuarioForm.get('identificacion')?.value);
      formData.append('direccion', this.editarUsuarioForm.get('direccion')?.value);
      formData.append('correo', this.editarUsuarioForm.get('correo')?.value);
      formData.append('telefono', this.editarUsuarioForm.get('telefono')?.value);
      formData.append('idCiudad', this.editarUsuarioForm.get('ciudad')?.value);
      formData.append('idRol', this.editarUsuarioForm.get('rol')?.value);
      if (this.selectedFile) {
        formData.append('imagen', this.selectedFile);
      }
      console.log('Form Data:', formData);
      this.usuarioService.updateUsuario(this.selectedUsuario.id, formData).subscribe(
        (response) => {
          console.log('Usuario actualizado:', response);
          this.cerrarModal();
          alert("Usuario actualizado con exito");
        },
        (error) => {
          console.error('Error al actualizar usuario:', error);
          alert("Error al actualizar el usuario");
        }
      );
    }
    else {
      console.log("No hay usuario seleccionado para editar ")
    }
  }
  eliminarUsuario(id: number): void {
    if (confirm("¿Estás seguro de eliminar este usuario?")) {
      this.usuarioService.deleteUsuario(id).subscribe(
        (response) => {
          console.log('Respuesta de eliminación:', response);
          if (response && response.status === 200) { // Verifica el código de estado de la respuesta.
            console.log('Usuario eliminado:', response);
            this.usuarios = this.usuarios.filter((usuario) => usuario.id !== id);
            alert("Usuario eliminado con éxito");
          } else {
            console.error('Error en la respuesta al eliminar usuario:', response);
            alert("Error al eliminar el usuario");
          }
        },
        (error) => {
          console.error('Error al eliminar usuario:', error);
          alert("Error al eliminar el usuario");
        }
      );
    } else {
      console.log("El usuario no fue seleccionado para eliminar");
    }
  }
}
