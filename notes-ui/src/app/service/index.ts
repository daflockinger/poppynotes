import { CryptoInterceptor } from './api/crypto-interceptor';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

export * from './api/api';
export * from './model/models';
export * from './variables';
export * from './configuration';
export * from './api.module';
export const httpInterceptorProviders = [
  { provide: HTTP_INTERCEPTORS, useClass: CryptoInterceptor, multi: true },
];
