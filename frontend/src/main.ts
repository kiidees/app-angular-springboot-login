import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';

/**** 
 * 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 
 * 762559686416-jm4df29i36p9g0boj5d225khdhp0bqj1.apps.googleusercontent.com
 * ****/


/****
 * 
 * 762559686416-2had8klsm955lmnboc8eioa52ul6ke5e.apps.googleusercontent.com
 * 
 * 
 */

import { routes } from './app/app.routes';      

import { App } from './app/app';


bootstrapApplication(App, {
  providers: [
    provideRouter(routes),
    provideAnimationsAsync(),
    provideHttpClient()
  ]
}).catch((err) => console.error(err));
