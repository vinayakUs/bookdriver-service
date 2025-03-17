import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import {AuthService} from '../../service/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <h2>Login</h2>
    <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">
      <label for="email">Email:</label>
      <input id="email" formControlName="email" type="email" required>

      <label for="password">Password:</label>
      <input id="password" formControlName="password" type="password" required>

      <button type="submit">Login</button>
    </form>
  `
})
export class LoginComponent {
  loginForm = new FormGroup({
    email: new FormControl('sa@gmail.com'),
    password: new FormControl('sa')
  });

  constructor(private authService: AuthService, private router: Router) {

    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/home']); // Redirect to home if already logged in
    }

  }
  onSubmit() {
    if (this.loginForm.valid) {
      const credentials = {
        email: this.loginForm.value.email!,
        password: this.loginForm.value.password!,
      };

      this.authService.login(credentials).subscribe({
        next: () => {
          console.log('Login successful');
          this.router.navigate(['/home']);
        },
        error: err => {
          console.error('Login failed', err);
        }
      });
    }
  }

  // onSubmit() {
  //   if (this.loginForm.valid) {
  //     console.log('Login successful', this.loginForm.value);
  //     this.router.navigate(['/home']); // Change to your desired route
  //   }
  // }
}
