import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SignupComponent} from "../../auth/signup/signup.component";
import {SigninComponent} from "../../auth/signin/signin.component";
import {ResetPasswordComponent} from "../../auth/reset-password/reset-password.component";
import {
  ResetPasswordInitiationComponent
} from "../../auth/reset-password-initiation/reset-password-initiation.component";

const routes: Routes = [
  {path: '', redirectTo: 'signin', pathMatch: 'full'},
  {path: 'signin', component: SigninComponent},
  {path: 'signup', component: SignupComponent},
  {path: 'reset-password/initiate', component: ResetPasswordInitiationComponent},
  {path: ':userId/reset-password/reset/:restId', component: ResetPasswordComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule {
}
