import {Component, Input} from '@angular/core';
import {ParkingSpot} from 'src/app/model/parking-spot';
import {ConfirmationService, ConfirmEventType, MessageService} from "primeng/api";
import {ParkingService} from "../../services/parking.service";

@Component({
  selector: 'parking-spot-config-edit',
  templateUrl: './parking-spot-config-edit.component.html',
  styleUrls: ['./parking-spot-config-edit.component.scss'],
  providers: [ConfirmationService]
})
export class ParkingSpotConfigEditComponent {
  @Input() inputSpots!: ParkingSpot[];
  selectedSpot: ParkingSpot = new ParkingSpot();

  constructor(private confirmationService: ConfirmationService, private messageService: MessageService, private parkingService: ParkingService) {
  }

  selectSpot(spot: ParkingSpot) {
    setTimeout(() => {
      this.selectedSpot = spot;
    }, 100);
  }

  deleteSpot(deletingSpot: ParkingSpot, index: number) {
    delete this.inputSpots[index];
    this.inputSpots.splice(index, 1);
  }

  deleteConfirmation(deletingSpot: ParkingSpot, index: number) {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete the Spot: ' + deletingSpot.name,
      header: 'Delete Confirmation',
      icon: 'pi pi-info-circle',
      accept: () => this.deleteSpot(deletingSpot, index),
      reject: (type: any) => {
        switch (type) {
          case ConfirmEventType.REJECT:
            this.messageService.add({severity: 'error', detail: 'You have rejected'});
            break;
          case ConfirmEventType.CANCEL:
            this.messageService.add({severity: 'warn', detail: 'You have cancelled'});
            break;
        }
      },
      key: 'confirmDialog'
    });
  }
}
