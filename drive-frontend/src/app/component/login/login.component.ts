import { Component } from '@angular/core';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import {AuthService} from '../../service/auth.service';
import {MessageService} from 'primeng/api';
import {Toast} from 'primeng/toast';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  // styleUrls: [],
  styleUrl:'login.component.css',
  standalone: true,
  imports: [FormsModule, CommonModule, ReactiveFormsModule ]
})
export class LoginComponent {
  isLoading: boolean = false;
  loginForm = new FormGroup({
    email: new FormControl(),
    password: new FormControl()
  });


  constructor(private router: Router,private authService: AuthService,private messageService:MessageService) {
    if(this.authService.isLoggedIn()){
      this.router.navigate(['/home']).then(r => console.log("successfully redirected to home")); // Redirect to home if already logged in
    }
  }


  onSubmit() {
    if (this.loginForm.valid && !this.isLoading) {
      this.isLoading = true;
      const credentials = {
        email: this.loginForm.value.email!,
        password: this.loginForm.value.password!,
      };

      this.authService.login(credentials).subscribe({
        next: nex => {
          console.log('Login successful');
          this.isLoading = false;
          this.router.navigate(['/home']);
        },
        error: err => {
          console.error('Login failed', err);

          // Extract error details
          const errorResponse = err.error;
          const success = errorResponse?.success ?? false;
          const message = errorResponse?.data ?? 'An unexpected error occurred';
          this.messageService.add({
            severity: success ? 'success' : 'error',
            summary: 'Login Failed',
            detail: message
          });
          this.isLoading = false;
        }
      });
    }
  }










  // onSubmit() {
  //   console.log('Login form submitted:', this.user);
  //   // TODO: Implement actual login logic
  // }

  goToRegister() {
    this.router.navigate(['/register']);
  }
}
