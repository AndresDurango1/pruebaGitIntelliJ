import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { LoginService } from '../../services/auth/login.service';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm : any;
  loginError:string = '';
  
  constructor(private formBuilder:FormBuilder, private router: Router, private loginService: LoginService ) { }
  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }
  get email(){ return this.loginForm.controls.email; }
  get password(){ return this.loginForm.controls.password; }
  login(){
    if(this.loginForm.valid){
      this.loginService.login(this.loginForm.value).subscribe({
        next: (userData) => {
          console.log(userData);
        },
        error: (errorData) => {
          console.log(errorData);
          this.loginError = errorData;
        },
        complete: () => {
          console.log('Login completado');
          this.router.navigateByUrl('/');
          this.loginForm.reset();
        }
      });
    }
    else{
      this.loginForm.markAllAsTouched();
      alert('Error al ingresar los datos. Formulario inválido');
    }
  }
}
