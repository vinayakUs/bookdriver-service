// app/login/login.component.ts
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
 import { CommonModule } from '@angular/common';
import {AuthService} from '../../service/auth.service';

@Component({
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  loginForm: FormGroup;
  loginError: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['radhikaandrew12@gmail.com', Validators.required],
      // username: ['sa', Validators.required],
      // type: ['', Validators.required],
      password: ['sa', Validators.required],
      driver: [false]
    });
  }

  onSubmit(): void {

    // console.log(typeof ("sd".strip()));
    if (this.loginForm.valid) {
      const { email,driver, password } = this.loginForm.value;
      this.authService.login(email,email.split('@',1)[0], driver?'DRIVER':'USER', password).subscribe({
        next: () =>{
          console.log("next login");
          this.router.navigateByUrl('/dashboard').then(success => {
            if (success) {
              console.log("✅ Navigation successful!");
            } else {
              console.error("❌ Navigation failed!");
            }
          });
          // return this.router.navigate(['/dashboard'])
        },
        error: (err: HttpErrorResponse) => {
          this.loginError = err.error?.message || 'Login failed';
          console.error('Login error:', err);
        }
      });
    }
  }
}
