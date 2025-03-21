import { Component } from '@angular/core';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgIf} from '@angular/common';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {AuthService} from '../../service/auth.service';
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  isRegistering: boolean = false;
  registerForm = new FormGroup({
    email: new FormControl(),
    password: new FormControl(),
    name: new FormControl(),
    registerAsAdmin: new FormControl()
  });

  constructor(private http: HttpClient, private router: Router,private authService: AuthService,private messageService:MessageService) {
    console.log("logged in",this.authService.isLoggedIn());
    if(this.authService.isLoggedIn()){
      console.log("logged in",this.authService.isLoggedIn());
      this.router.navigate(['/home']).then(r => console.log("successfully redirected to home")); // Redirect to home if already logged in

    }
  }

  onSubmit() {
    if(this.registerForm.valid && !this.isRegistering){
      this.isRegistering = true;
      const data={
        email:this.registerForm.value.email,
        password:this.registerForm.value.password,
        username:this.registerForm.value.name,
        registerAsAdmin:true
      }
      this.authService.register(data).subscribe(
          {
            next: r => {
              console.log(`successfully registered data [${r.data}] success [${r.success}]`);
              this.isRegistering = false;
              this.router.navigate(['/send'],{queryParams:{email:data.email},replaceUrl:true}).then(r =>
              console.log("successfully redirected to send", r)
              );
            },
          error: err => {
              this.isRegistering = false;
              const errorResponse = err.error;
              const success = errorResponse?.success ?? false;
              const message = errorResponse?.data ?? 'An error occurred';
            // Show error message using PrimeNG Toast
            this.messageService.add({
              severity: success ? 'success' : 'error',
              summary: 'Register Failed',
              detail: message
            });
            console.log("Register Failed" + err);
          },
          }
      )
    }
  }


}
