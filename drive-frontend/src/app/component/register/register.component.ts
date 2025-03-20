import { Component } from '@angular/core';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgIf} from '@angular/common';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {AuthService} from '../../service/auth.service';

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
    name: new FormControl(),
    email: new FormControl(),
    password: new FormControl()
  });

  constructor(private http: HttpClient, private router: Router,private authService: AuthService) {
    console.log("logged in",this.authService.isLoggedIn());
    if(this.authService.isLoggedIn()){
      console.log("logged in",this.authService.isLoggedIn());
      this.router.navigate(['/home']).then(r => console.log("successfully redirected to home")); // Redirect to home if already logged in

    }
  }

  onSubmit(){
      if(this.registerForm.valid && !this.isRegistering){
        this.isRegistering = true;
        const data = {

          username: this.registerForm.value.email.toString().split("@")[0]!,
            email: this.registerForm.value.email!,
            password: this.registerForm.value.password!,
            registerAsAdmin: true
        };
        this.authService.register(data).subscribe(
            {
              next: (result) => {
                this.isRegistering = false;

                console.log("successfully registered" + result);
              },
              error:err => {
                console.log(err.getmessa);
                if(err.status === 200 ){
                  console.log("error", err.success);
                  console.log('---------------------------');
                }
                this.isRegistering = false;
                console.log(err);
              }
            }
        )
      }
  }

}
