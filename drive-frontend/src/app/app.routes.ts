// app/app.routes.ts
import { Routes } from '@angular/router';
import {HomeComponent} from './component/home/home.component';
import {LoginComponent} from './component/login/login.component';
import {authGuard} from './auth.guard';
import {AppComponent} from './app.component';
import {RegisterComponent} from './component/register/register.component';
import { ResetComponent } from './component/reset/reset.component';
import { MailsendComponent } from './component/mailsend/mailsend.component';
import { ResetsuccessComponent } from './component/resetsuccess/resetsuccess.component';
import { ForgotPasswordComponent } from './component/forgotPassword/forgotPassword.component';

// export const routes: Routes = [
//   { path: 'login', component: LoginComponent },
//   {
//     path: 'dashboard',
//     component: HomeComponent,
//     canActivate: [authGuard]
//   },
//   {
//     path: 'register',component:AppComponent
//   },
//   { path: '', redirectTo: '/login', pathMatch: 'full' },
//   { path: '**', redirectTo: '/login' }
// ];
//


export const routes: Routes = [
  { path: 'home', component: HomeComponent, canActivate: [authGuard] }, // Protected
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'reset-password', component: ResetComponent },
  { path: 'forgot-password/send', component: MailsendComponent },
  { path: 'resetSuccess', component: ResetsuccessComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },

  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];
