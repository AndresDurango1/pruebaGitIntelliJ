import { Component } from '@angular/core';
import { Publicacion } from '../../modelos/publicacion';
import { PublicacionService } from '../../services/publicacion.service';

@Component({
  selector: 'app-publicaciones',
  standalone: false,

  templateUrl: './publicaciones.component.html',
  styleUrl: './publicaciones.component.css'
})
export class PublicacionesComponent {
  publicaciones: Publicacion[] = [];

  constructor(private publicacionService: PublicacionService) {}
  ngOnInit(): void {
    this.publicacionService.getPublicaciones().subscribe((data) => (this.publicaciones = data));
    console.log(this.publicaciones);
  }

}
