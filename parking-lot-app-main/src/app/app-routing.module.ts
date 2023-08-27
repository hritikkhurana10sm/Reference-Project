import {inject, NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthService} from "./services/auth.service";

const routes: Routes = [
  { path: '', redirectTo: 'parking/dashboard', pathMatch: 'full' },
  {
    path: 'parking',
    canActivate: [() => inject(AuthService).isLoggedInOrElseNavigateToSignin()],
    loadChildren: () => import('./modules/parking/parking.module').then(m => m.ParkingModule),
  },
  {
    path: 'user',
    canActivate: [() => inject(AuthService).isNotLoggedInOrElseNavigateToHome()],
    loadChildren: () => import('./modules/auth/auth.module').then(m => m.AuthModule),
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
