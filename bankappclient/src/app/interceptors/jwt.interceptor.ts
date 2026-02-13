import { HttpInterceptorFn } from '@angular/common/http';
import { AuthenticationService } from '../services/authentication.service';
import { inject } from '@angular/core';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthenticationService);
  const isAuthRequest = req.url.includes('/authenticate');
  const token = authService.getToken();

  if (token && !isAuthRequest) {
    const cloned = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(cloned);
  }

  return next(req);
};
