import { Component, ViewChild, ElementRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';


import { RouterOutlet } from '@angular/router';


@Component({
  selector: 'app-login.component',
  imports: [CommonModule, RouterOutlet],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})


export class LoginComponent {

  title = 'loginGoogle';

  auth2: any;

  @ViewChild('loginRef', { static: true }) loginElement!: ElementRef;

  constructor(private http: HttpClient, private router: Router) { }

  /*------------------------------------------
  --------------------------------------------
  About 
  --------------------------------------------
  
  ngOnInit() {

    this.googleAuthSDK();
  }
--------------------------------------------*/
  ngOnInit() {

    this.googleAuthSDK();

    const tokenExistente = localStorage.getItem('app_token');
    if (tokenExistente) {
      this.router.navigate(['/Task']);
      return; // Detiene la ejecución para no escuchar a Google Auth innecesariamente
    }
  }
  
  

  /**
   * Write code on Method
   *
   * @return response()
   */
  callLoginButton() {

    this.auth2.attachClickHandler(this.loginElement.nativeElement, {},
      (googleAuthUser: any) => {
        const authResponse = googleAuthUser.getAuthResponse();
        const token = authResponse.id_token;
        let profile = googleAuthUser.getBasicProfile();
        console.log('Token || ' + token);
        console.log('ID: ' + profile.getId());
        console.log('Name: ' + profile.getName());
        console.log('Image URL: ' + profile.getImageUrl());
        console.log('Email: ' + profile.getEmail());



        if (profile) {
          this.http.post<{ token: string }>('http://localhost:8081/api/auth/google', { token: token })
            .subscribe({
              next: (res) => {
                console.log('Respuesta del backend:', res); // Debug log para verificar la respuesta del backend
                // CORRECCIÓN: Guardamos el JWT generado por Spring Boot, no el de Google
                localStorage.setItem('app_token', res.token);
                console.log('Login exitoso en nuestro sistema');
                console.log('JWT recibido: ' + res.token);

                this.router.navigate(['/Task']);
              },
              error: (err) => console.error('Fallo en el backend:', err)
            });
        }



      }, (error: any) => {
        alert(JSON.stringify(error, undefined, 2));
      });

  }

  /**
   * Write code on Method
   *
   * @return response()
   */
  googleAuthSDK() {

    const win = window as any;
    win['googleSDKLoaded'] = () => {
      win['gapi'].load('auth2', () => {
        this.auth2 = win['gapi'].auth2.init({
          client_id: '762559686416-2had8klsm955lmnboc8eioa52ul6ke5e.apps.googleusercontent.com',
          cookiepolicy: 'single_host_origin',
          scope: 'profile email'
        });
        this.callLoginButton();
      });
    }

    (function (d, s, id) {
      var js, fjs = d.getElementsByTagName(s)[0];
      if (d.getElementById(id)) { return; }
      js = d.createElement('script');
      js.id = id;
      js.src = "https://apis.google.com/js/platform.js?onload=googleSDKLoaded";
      fjs?.parentNode?.insertBefore(js, fjs);
    }(document, 'script', 'google-jssdk'));

  }
}