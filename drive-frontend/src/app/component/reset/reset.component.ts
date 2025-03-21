import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormControl} from '@angular/forms';
import { CommonModule } from '@angular/common';
  import {ActivatedRoute} from '@angular/router';
import {AuthService} from '../../service/auth.service';
import {MessageService} from 'primeng/api';

@Component({
  selector: 'app-reset',
  templateUrl: './reset.component.html',
  styleUrls: ['./reset.component.css'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule]
})
export class ResetComponent implements OnInit {
  resetForm: FormGroup;
  isLoading = false;

  constructor(private fb: FormBuilder, private route:ActivatedRoute, private authService:AuthService, private messageService:MessageService) {
    this.resetForm = this.fb.group({

      email:  new FormControl(),
      password:  new FormControl()
    });
  }

  onSubmit() {
    if (this.resetForm.valid) {
      this.isLoading = true;

     const data={
        email: this.resetForm.value.email,
        password: this.resetForm.value.password,
        confirmPassword: this.resetForm.value.password,
        token: this.token
      }
      this.authService.resetPassword(data).subscribe(
        {
          next: () => {
            this.isLoading = false;
          },
          error: err => {

            // Extract error details
            const errorResponse = err.error;
            const success = errorResponse?.success ?? false;
            const message = errorResponse?.data ?? 'An unexpected error occurred';

            console.error('Login failed', err);

            // Show error message using PrimeNG Toast
            this.messageService.add({
              severity: success ? 'success' : 'error',
              summary: 'Login Failed',
              detail: message
            });

            this.isLoading = false;
            console.error('Login failed', err);
          }
        }
      )
      // console.log('Reset password for:', this.resetForm.value.email);
      // setTimeout(() => this.isLoading = false, 2000); // Simulated API call
    }
  }

  token:string='';

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      console.log(params['token']+'token..............');
      this.token = params['token'];
    })
  }
}
