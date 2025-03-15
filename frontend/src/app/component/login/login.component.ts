// login.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
  // template: `
  //   <form [formGroup]="loginForm" (ngSubmit)="onSubmit()" class="simple-form">
  //     <h2>Login</h2>
  //
  //     <div class="form-group">
  //       <label for="email">Email</label>
  //       <input
  //         type="email"
  //         id="email"
  //         formControlName="email"
  //         class="form-control"
  //         placeholder="Enter email"
  //       >
  //       <div *ngIf="loginForm.get('email')?.errors?.['required'] && loginForm.get('email')?.touched"
  //            class="error-message">
  //         Email is required
  //       </div>
  //       <div *ngIf="loginForm.get('email')?.errors?.['email'] && loginForm.get('email')?.touched"
  //            class="error-message">
  //         Please enter a valid email
  //       </div>
  //     </div>
  //
  //     <div class="form-group">
  //       <label for="password">Password</label>
  //       <input
  //         type="password"
  //         id="password"
  //         formControlName="password"
  //         class="form-control"
  //         placeholder="Enter password"
  //       >
  //       <div *ngIf="loginForm.get('password')?.errors?.['required'] && loginForm.get('password')?.touched"
  //            class="error-message">
  //         Password is required
  //       </div>
  //     </div>
  //
  //     <button
  //       type="submit"
  //       class="submit-button"
  //       [disabled]="!loginForm.valid"
  //     >
  //       Sign In
  //     </button>
  //   </form>
  // `,
  // styles: [
  //   `
  //   .simple-form {
  //     max-width: 300px;
  //     margin: 2rem auto;
  //     padding: 1rem;
  //     border: 1px solid #ddd;
  //     border-radius: 4px;
  //   }
  //
  //   .form-group {
  //     margin-bottom: 1rem;
  //   }
  //
  //   label {
  //     display: block;
  //     margin-bottom: 0.5rem;
  //   }
  //
  //   .form-control {
  //     width: 100%;
  //     padding: 0.5rem;
  //     border: 1px solid #ccc;
  //     border-radius: 4px;
  //     box-sizing: border-box;
  //   }
  //
  //   .error-message {
  //     color: #dc3545;
  //     font-size: 0.875rem;
  //     margin-top: 0.25rem;
  //   }
  //
  //   .submit-button {
  //     width: 100%;
  //     padding: 0.5rem;
  //     background-color: #007bff;
  //     color: white;
  //     border: none;
  //     border-radius: 4px;
  //     cursor: pointer;
  //   }
  //
  //   .submit-button:disabled {
  //     background-color: #6c757d;
  //     cursor: not-allowed;
  //   }
  //   `
  // ]
})
export class LoginComponent {
  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required])
  });

  onSubmit() {
    if (this.loginForm.valid) {
      console.log('Form submitted:', this.loginForm.value);
      // Add your authentication logic here
    }
  }
}
