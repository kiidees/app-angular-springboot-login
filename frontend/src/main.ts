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
