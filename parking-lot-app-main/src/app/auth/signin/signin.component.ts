import {Component} from '@angular/core';
import {SigninDto} from "../../model/signinDto";
import {AuthService} from "../../services/auth.service";
import {MessageService} from "primeng/api";
import {Messages} from "../../model/messages";
import {Router} from "@angular/router";

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss']
})
export class SigninComponent {

  title = 'Parking Lot Management';
  signingInUser: SigninDto = new SigninDto();

  constructor(private signinSingupService: AuthService, private router: Router, private authService: AuthService,
              private messageService: MessageService, private messageObj: Messages) {
  }

  signinUser() {
    this.signinSingupService.signinApi(this.signingInUser).subscribe((response: string) => {
        this.authService.setCookie(response);
        this.messageService.add({
          severity: 'success',
          detail: this.messageObj.messages[0],
        });
        this.signingInUser.username = undefined;
        this.signingInUser.password = undefined;
        this.goToDashBoard();
      },
      (error) => {
        this.messageService.add({
          severity: 'error',
          detail: error.error.message,
        });
      }
    );
  }

  goToDashBoard() {
    this.router.navigate(['']);
  }

}
