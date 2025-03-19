import { Component } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: [],
  standalone: true,
  imports: [FormsModule, CommonModule, ReactiveFormsModule]
})
export class LoginComponent {
  loginForm = new FormGroup({
    email: new FormControl('sa@gmail.com'),
    password: new FormControl('sa')
  });


  user = {
    email: '',
    password: ''
  };

  constructor(private router: Router) {}

  onSubmit() {
    console.log('Login form submitted:', this.user);
    // TODO: Implement actual login logic
  }

  goToRegister() {
    this.router.navigate(['/register']);
  }
}
