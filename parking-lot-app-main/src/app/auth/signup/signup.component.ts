import {Component} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {SignupDto} from "../../model/signupDto";
import {MessageService} from "primeng/api";
import {Messages} from "../../model/messages";
import {Router} from "@angular/router";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent {

  title: string = 'Parking Lot Management';
  signupDto: SignupDto = new SignupDto();

  constructor(private authService: AuthService, private messageService: MessageService, private messageObj: Messages, private router: Router) {
  }

  signupUser() {
    this.authService.signupApi(this.signupDto).subscribe((response) => {
        this.messageService.add({
          severity: 'success',
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
