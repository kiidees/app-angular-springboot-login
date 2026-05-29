import { Routes } from '@angular/router';
import { HomeComponent } from '../pages/home.component/home.component';
import { LoginComponent } from '../pages/login/login.component';
//import { Oauth2RedirectComponent } from '../app/oauth2-redirect/oauth2-redirect';
import { authGuard } from './auth.guard'; 

export const routes: Routes = [
  // 1. Rutas Públicas (Sin Guard)
  { path: 'login', component: LoginComponent },
  //{ path: 'oauth2/redirect', component: Oauth2RedirectComponent }, // <-- Subida aquí antes del comodín

  // 2. Ruta raíz (Redirección inicial)
  { path: '', redirectTo: '/login', pathMatch: 'full' },

  // 3. Rutas Protegidas (Con Guard)
  { path: 'Home', component: HomeComponent, canActivate: [authGuard] },  
  { path: 'Assignee', loadChildren: () => import('../pages/assignee-enum/assignee-enum.routes').then(m => m.ASSIGNEE_ENUM_ROUTES), canActivate: [authGuard] },
  { path: 'Status', loadChildren: () => import('../pages/status-enum/status-enum.routes').then(m => m.STATUS_ENUM_ROUTES), canActivate: [authGuard] },
  { path: 'Task Fields', loadChildren: () => import('../pages/task-fields-enum/task-fields-enum.routes').then(m => m.TASK_FIELDS_ENUM_ROUTES), canActivate: [authGuard] },
  { path: 'Task', loadChildren: () => import('../pages/task/task.routes').then(m => m.TASK_ROUTES), canActivate: [authGuard] },

  // 4. Comodín para manejo de errores de rutas (SIEMPRE AL FINAL)
  { path: '**', redirectTo: '/login' }
];