import { Component } from '@angular/core';
import { UsuarioService } from '../../services/usuario.service';
import { Usuario } from '../../modelos/usuario';

@Component({
  selector: 'app-usuario',
  standalone: false,

  templateUrl: './usuario.component.html',
  styleUrl: './usuario.component.css'
})
export class UsuarioComponent {
  usuarios: Usuario[] = [];
  constructor(private usuarioService: UsuarioService) {}
  ngOnInit():void {
    this.usuarioService.getUsuarios().subscribe((data) => (this.usuarios = data));
    console.log(this.usuarios);
  }
}
