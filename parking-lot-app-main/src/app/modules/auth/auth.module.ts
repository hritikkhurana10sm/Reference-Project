import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {AuthRoutingModule} from './auth-routing.module';
import {SigninComponent} from "../../auth/signin/signin.component";
import {SignupComponent} from "../../auth/signup/signup.component";
import {FormsModule} from "@angular/forms";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {InputNumberModule} from "primeng/inputnumber";
import {RippleModule} from "primeng/ripple";
import {HttpClientModule} from "@angular/common/http";
import {ResetPasswordComponent} from '../../auth/reset-password/reset-password.component';
import {
  ResetPasswordInitiationComponent
} from '../../auth/reset-password-initiation/reset-password-initiation.component';


@NgModule({
  declarations: [
    SigninComponent,
    SignupComponent,
    ResetPasswordComponent,
    ResetPasswordInitiationComponent,
  ],
  imports: [
    CommonModule,
    AuthRoutingModule,
    HttpClientModule,
    FormsModule,
    ButtonModule,
    InputTextModule,
    InputNumberModule,
    RippleModule,
  ]
})
export class AuthModule { }
