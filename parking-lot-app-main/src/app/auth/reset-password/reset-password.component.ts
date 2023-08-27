import {Component} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {MessageService} from "primeng/api";
import {Messages} from "../../model/messages";
import {ActivatedRoute, Router} from "@angular/router";
import {ResetPasswordDto} from "../../model/reset-passwordDto";

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent {

  title: string = 'Parking Lot Management';
  resetPasswordDto: ResetPasswordDto=new ResetPasswordDto();
  userId?: string;
  resetId?: string;

  constructor(private authService: AuthService, private messageService: MessageService, private messageObj: Messages, private router: Router, private route: ActivatedRoute) {
    this.userId = this.route.snapshot.params['userId'];
    this.resetId = this.route.snapshot.params['resetId'];
  }

  resetPassword() {
    this.authService.resetPassword(this.userId!, this.resetId!, this.resetPasswordDto!).subscribe((response) => {
        this.messageService.add({
          severity: 'success',
          detail: this.messageObj.messages[0]
        });
        this.goToSignInPage();
      },
      (error) => {
        console.error(error.error.messages);
        for(let u=0;u<error.error.messages.length;u++) {
          this.messageService.add({
            severity: 'error',
            detail: error.error.messages[u],
          });
        }
      }
    );
  }

  goToSignInPage() {
    this.router.navigate(['user', 'signin']);
  }
}
