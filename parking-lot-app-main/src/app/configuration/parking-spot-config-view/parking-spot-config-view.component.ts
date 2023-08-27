import {Component, Input, OnInit} from '@angular/core';
import {ParkingSpot} from 'src/app/model/parking-spot';
import {ParkingSpotService} from "../../services/parking-spot.service";
import {MessageService} from "primeng/api";
import {Messages} from "../../model/messages";
import {ParkingService} from "../../services/parking.service";

@Component({
  selector: 'parking-spot-config-view',
  templateUrl: './parking-spot-config-view.component.html',
  styleUrls: ['./parking-spot-config-view.component.scss']
})
export class ParkingSpotConfigViewComponent implements OnInit {

  @Input() inputSpots!: ParkingSpot[];
  @Input() parkingStatus!: string;
  selectedSpot: ParkingSpot = new ParkingSpot();
  carNumber?: string;

  constructor(private parkingService: ParkingService, private spotService: ParkingSpotService, private messageService: MessageService, private messagesObj: Messages) {
  }

  ngOnInit() {
  }

  selectSpot(spot: ParkingSpot, element: any, event: any) {
    this.carNumber = undefined;
    setTimeout(() => {
      element.toggle(event);
      this.selectedSpot = spot;
    }, 100);
  }

  spotAllocation() {
    this.spotService.spotAllocation(this.selectedSpot.id!, this.carNumber!).subscribe(
      (allottedSpot) => {
        this.spotService.spotAllocateEvent(allottedSpot);
        this.messageService.add({
          severity: 'success',
          detail: this.messagesObj.messages[0],
        });
      },
      (error) => {
        this.messageService.add({
          severity: 'error',
          detail: error.error.message,
        });
      }
    );
  }
}
