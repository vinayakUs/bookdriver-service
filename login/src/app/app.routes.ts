
import { Routes } from '@angular/router';
import {LoginComponent} from './comp/login/login.component';
import {HomeComponent} from './comp/home/home.component';
import {authGuard} from './auth.guard';

export const routes: Routes = [
  { path: 'home', component: HomeComponent, canActivate: [authGuard] }, // Protected
  { path: 'login', component: LoginComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];
