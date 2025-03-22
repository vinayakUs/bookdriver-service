import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {NgIf} from '@angular/common';
import {AuthService} from '../../service/auth.service';
import {MessageService} from 'primeng/api';

@Component({
  selector: 'app-verify-email',
  templateUrl: './verify-email.component.html',
  imports: [
    NgIf
  ],
  styleUrls: ['./verify-email.component.css']
})
export class VerifyEmailComponent implements OnInit {
  token: string = '';
  isLoading: boolean = false;
  isVerified: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private messageService: MessageService
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.token = params['token'] ;
    });
    if (this.token==null
    || this.token == '') {
      this.router.navigate(['404']).then(r =>{
        console.log("Not Found"+r);
      });
    }
  }

  onVerify(confirmed: boolean) {
    this.isLoading = true;

    this.authService.verifyUserAccount({token:this.token}).subscribe(
      {
        next: (response) => {
          this.isLoading = false;
          this.isVerified=true;
          console.log("Verified");
        },
        error: err => {
          // Extract error details
          const errorResponse = err.error;
          const success = errorResponse?.success ?? false;
          const message = errorResponse?.data ?? 'An unexpected error occurred';

          console.error('Email Verify failed', err);

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


  }
}
