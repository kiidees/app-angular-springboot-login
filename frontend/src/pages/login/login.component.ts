import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-login.component',
  standalone: true, // Si usas Angular moderno
  imports: [CommonModule, RouterOutlet],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {

  // Eliminamos title, auth2 y @ViewChild que pertenecían al SDK de Google

  constructor(
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    // Únicamente validamos si ya existe una sesión en el sistema corporativo local
    this.verificarSesionExistente();

    
  }

  private verificarSesionExistente() {
    const tokenExistente = localStorage.getItem('app_token');
    if (tokenExistente) {
      this.router.navigate(['/Task']);
    }
  }

  redirectToMicrosoft() {
    // Rompe el contexto de Angular y fuerza la navegación externa nativa
    // Esto evita que se disparen peticiones intermitentes tipo OPTIONS
    window.location.href = 'http://localhost:8081/oauth2/authorization/azure';
  }
}