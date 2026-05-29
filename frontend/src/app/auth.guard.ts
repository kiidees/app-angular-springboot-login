import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
    const router = inject(Router);

    // Verificamos si existe el JWT emitido por nuestro Spring Boot en el navegador
    const token = localStorage.getItem('app_token');

    if (token) {
        console.log('Token encontrado en localStorage:', token);
        // Si el token existe, permitimos el acceso a la pantalla
        console.log('Acceso permitido a la ruta:', state.url);
        loadChildren: () => import('../pages/task/task.routes').then(m => m.TASK_ROUTES)
        return true;
    }

    // Si no está autenticado, lo mandamos directo a la pantalla de login
    router.navigate(['/login']);
    console.log('No se encontró token en localStorage Redirigiendo a /login');
    return false;
};