import { Component } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
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

  constructor(private http: HttpClient, private router: Router,private authService: AuthService) {
    console.log("logged in",this.authService.isLoggedIn());
    if(this.authService.isLoggedIn()){
      console.log("logged in",this.authService.isLoggedIn());
      this.router.navigate(['/home']).then(r => console.log("successfully redirected to home")); // Redirect to home if already logged in

    }
  }

}
