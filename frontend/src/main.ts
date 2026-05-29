// ==================================================================
// INTERCEPTOR DE TOKEN DE ENTRA ID ANTES DE QUE ARRANQUE ANGULAR
// ==================================================================
(function() {
  const urlActual = window.location.href;
  console.log('URL de entrada detectada en el navegador:', urlActual);

  if (urlActual.includes('token=')) {
    // Dividimos la URL para aislar la cadena del token
    const partes = urlActual.split('token=');
    if (partes.length > 1) {
      // Limpiamos cualquier parámetro extra que pudiera venir acoplado con un '&'
      const tokenLimpio = partes[1].split('&')[0];
      
      if (tokenLimpio && tokenLimpio !== '') {
        // Almacenamos el token bajo el nombre exacto que espera tu arquitectura
        localStorage.setItem('app_token', tokenLimpio);
        console.log('--- ¡TOKEN GUARDADO EN LOCALSTORAGE DESDE EL MOTOR DE ARRANQUE! ---');
        
        // Limpiamos la URL visualmente removiendo el token para que quede estética y segura
        const urlLimpia = urlActual.split('?')[0];
        window.history.replaceState(null, '', urlLimpia);
      }
    }
  }
})();
// ==================================================================


import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';

import { routes } from './app/app.routes';      

import { App } from './app/app';


bootstrapApplication(App, {
  providers: [
    provideRouter(routes),
    provideAnimationsAsync(),
    provideHttpClient()
  ]
}).catch((err) => console.error(err));
