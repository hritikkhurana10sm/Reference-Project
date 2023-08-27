import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ParkingConfigEditComponent} from 'src/app/configuration/parking-config-edit/parking-config-edit.component';
import {UserDashboardComponent} from "../../user-dashboard/user-dashboard.component";

const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: UserDashboardComponent },
  { path: 'config', component: ParkingConfigEditComponent },
  { path: ':id/config', component: ParkingConfigEditComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ParkingRoutingModule { }
