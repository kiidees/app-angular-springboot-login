import { Routes } from '@angular/router';
import { HomeComponent } from '../pages/home.component/home.component';
import { LoginComponent } from '../pages/login/login.component';
import { authGuard } from './auth.guard'; 


export const routes: Routes = [
      { path: 'login', component: LoginComponent }
    , { path: 'Home', component: HomeComponent, canActivate: [authGuard] }  
    , { path: 'Assignee', loadChildren: () => import('../pages/assignee-enum/assignee-enum.routes').then(m => m.ASSIGNEE_ENUM_ROUTES), canActivate: [authGuard]}
    , { path: 'Status', loadChildren: () => import('../pages/status-enum/status-enum.routes').then(m => m.STATUS_ENUM_ROUTES), canActivate: [authGuard] }
    , { path: 'Task Fields', loadChildren: () => import('../pages/task-fields-enum/task-fields-enum.routes').then(m => m.TASK_FIELDS_ENUM_ROUTES), canActivate: [authGuard]}
    , { path: 'Task', loadChildren: () => import('../pages/task/task.routes').then(m => m.TASK_ROUTES), canActivate: [authGuard] }
    , { path: '', redirectTo: '/login', pathMatch: 'full' }
    , { path: '**', redirectTo: '/login' }

];