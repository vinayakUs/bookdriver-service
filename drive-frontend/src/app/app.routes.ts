// app/app.routes.ts
import { Routes } from '@angular/router';
import {HomeComponent} from './component/home/home.component';
import {LoginComponent} from './component/login/login.component';
import {authGuard} from './auth.guard';
import {AppComponent} from './app.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: 'dashboard',
    component: HomeComponent,
    canActivate: [authGuard]
  },
  {
    path: 'register',component:AppComponent
  },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];
