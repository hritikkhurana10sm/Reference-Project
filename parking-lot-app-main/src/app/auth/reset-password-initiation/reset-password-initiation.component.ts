import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {MessageService} from "primeng/api";
import {Messages} from "../../model/messages";

@Component({
  selector: 'app-reset-password-initiation',
  templateUrl: './reset-password-initiation.component.html',
  styleUrls: ['./reset-password-initiation.component.scss']
})
export class ResetPasswordInitiationComponent {

  title: string = 'Parking Lot Management';
  usernameOrEmail?: string;

  constructor(private authService: AuthService, private messageService: MessageService, private messageObj: Messages, private router: Router) {
  }

  initiateResetPassword() {
    this.authService.initiateResetPassword(this.usernameOrEmail!).subscribe(() => {
        this.messageService.add({
          severity: 'info',
          detail: this.messageObj.messages[0]
        });
        this.goToSignInPage();
      },
      (error) => {
        console.error(error.error.message);
        this.messageService.add({
          severity: 'error',
          detail: error.error.message,
        });
      }
    );
  }

  goToSignInPage() {
    this.router.navigate(['user', 'signin']);
  }
}
