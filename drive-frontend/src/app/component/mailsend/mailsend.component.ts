import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Routes} from '@angular/router';

@Component({
  selector: 'app-mailsend',
  templateUrl: './mailsend.component.html',
  styleUrl: './mailsend.component.css'
})
export class MailsendComponent  implements OnInit {
  constructor(private route:ActivatedRoute) {
  }
  email:string|null='';

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.email = params['email'];
    })
  }


  openEmailInbox() {
    // Open default email client
    window.location.href = 'mailto:';
  }

  resendEmail() {
    // TODO: Implement resend verification email logic
    console.log('Resending verification email...');
  }
}
