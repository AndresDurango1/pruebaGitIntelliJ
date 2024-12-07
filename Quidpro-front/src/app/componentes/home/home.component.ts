import { Component, OnInit, OnDestroy } from '@angular/core';
import { LoginService } from '../../services/auth/login.service';
import { Usuario } from '../../modelos/usuario';

@Component({
  selector: 'app-home',
  standalone: false,

  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit, OnDestroy {
  userLoginOn: boolean = false;
  userData?:Usuario;

  constructor(private loginService: LoginService){}

  ngOnInit(){
    this.loginService.currentUserLoginOn.subscribe({
      next:(userLoginOn) => {
        this.userLoginOn = userLoginOn;
      }
    });
    this.loginService.currentUserData.subscribe({
      next:(userData) => {
        this.userData = userData;
      }
    })
  }
  ngOnDestroy(){
    // Desuscribirse al observable para liberar recursos.
    // No olvidemos liberar los observables para evitar errores y mejorar la memoria.
    // Esto es importante para aplicaciones más largas y para evitar el consumo de memoria innecesaria.
    // Los observables son un tipo de objeto que emite valores y notificaciones en tiempo
    this.loginService.currentUserLoginOn.unsubscribe();
    this.loginService.currentUserData.unsubscribe();
  }
}
