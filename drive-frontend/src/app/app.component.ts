import { Component } from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import {CommonModule} from '@angular/common';
import {provideHttpClient} from '@angular/common/http';
import {Toast} from 'primeng/toast';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Toast],
  templateUrl:'app.component.html',
  styleUrl: './app.component.css'


})

export class AppComponent {
}
