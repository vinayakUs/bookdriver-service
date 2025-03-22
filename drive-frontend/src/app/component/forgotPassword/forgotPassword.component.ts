import { Component } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { MessageService } from 'primeng/api';
import { Toast } from 'primeng/toast';

@Component({
  selector: 'app-resetpassword',
  templateUrl: './forgotPassword.component.html',
  styleUrl: './forgotPassword.component.css',
  standalone: true,
  imports: [FormsModule, CommonModule, ReactiveFormsModule, Toast]
})
export class ForgotPasswordComponent {
  isLoading: boolean = false;
  resetForm = new FormGroup({
    email: new FormControl()
  });

  constructor(
    private router: Router,
    private authService: AuthService,
    private messageService: MessageService
  ) {}

  onSubmit() {
    if (this.resetForm.valid && !this.isLoading) {
      this.isLoading = true;
      const email = this.resetForm.value.email!;

      this.authService.sendPasswordResetMail({email:email}).subscribe({
        next: (response) => {
          console.log(`Reset password successfully data[${response.data}]`);
          this.isLoading = false;
          this.router.navigate(['forgot-password/send'], {queryParams: {email} , replaceUrl:true}).then(r =>{
            console.log("successfully navigate to send");
          });
         },
        error: (err) => {
          const errorResponse = err.error;
          const success = errorResponse?.success ?? false;
          const message = errorResponse?.data ?? 'An unexpected error occurred';
          this.isLoading = false;

          this.messageService.add({
            severity: success ? 'success' : 'error',
            summary: 'Unable to send Reset Mail',
            detail: message
          });

        }
      });
    }
  }
}
