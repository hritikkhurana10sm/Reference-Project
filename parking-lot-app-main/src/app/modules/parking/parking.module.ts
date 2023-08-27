import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {ParkingRoutingModule} from './parking-routing.module';
import {UserDashboardComponent} from "../../user-dashboard/user-dashboard.component";
import {ParkingConfigEditComponent} from "../../configuration/parking-config-edit/parking-config-edit.component";
import {
  ParkingBlockConfigEditComponent
} from "../../configuration/parking-block-config-edit/parking-block-config-edit.component";
import {
  ParkingSpotConfigEditComponent
} from "../../configuration/parking-spot-config-edit/parking-spot-config-edit.component";
import {
  ParkingSpotConfigViewComponent
} from "../../configuration/parking-spot-config-view/parking-spot-config-view.component";
import {
  ParkingBlockConfigViewComponent
} from "../../configuration/parking-block-config-view/parking-block-config-view.component";
import {FormsModule} from "@angular/forms";
import {ButtonModule} from "primeng/button";
import {OverlayPanelModule} from "primeng/overlaypanel";
import {DynamicDialogModule} from "primeng/dynamicdialog";
import {InputTextModule} from "primeng/inputtext";
import {InputNumberModule} from "primeng/inputnumber";
import {ProgressSpinnerModule} from "primeng/progressspinner";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {RippleModule} from "primeng/ripple";
import {HttpClientModule} from "@angular/common/http";


@NgModule({
  declarations: [
    UserDashboardComponent,
    ParkingConfigEditComponent,
    ParkingBlockConfigEditComponent,
    ParkingSpotConfigEditComponent,
    ParkingSpotConfigViewComponent,
    ParkingBlockConfigViewComponent,
  ],
  imports: [
    CommonModule,
    ParkingRoutingModule,
    HttpClientModule,
    FormsModule,
    ButtonModule,
    OverlayPanelModule,
    DynamicDialogModule,
    InputTextModule,
    InputNumberModule,
    ProgressSpinnerModule,
    ConfirmDialogModule,
    RippleModule,
  ]
})
export class ParkingModule { }
