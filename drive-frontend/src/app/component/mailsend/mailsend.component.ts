import { Component } from '@angular/core';

@Component({
  selector: 'app-mailsend',
  templateUrl: './mailsend.component.html',
  styleUrl: './mailsend.component.css'
//   styleUrls: ['./mailsend.component.css']
})
export class MailsendComponent {
  constructor() {}

  openEmailInbox() {
    // Open default email client
    window.location.href = 'mailto:';
  }

  resendEmail() {
    // TODO: Implement resend verification email logic
    console.log('Resending verification email...');
  }
}