import { Routes } from '@angular/router';
import {LoginComponent} from './component/login/login.component';


export const APP_ROUTES: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'home'
  },
  {
    path: 'home',
    component: LoginComponent
  },
  {
    path: 'products',
    loadComponent: () => import('./component/login/login.component').then(m => m.LoginComponent)
  }
];
