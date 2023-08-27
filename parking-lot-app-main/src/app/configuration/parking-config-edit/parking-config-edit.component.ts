import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {MessageService} from 'primeng/api';
import {Messages} from 'src/app/model/messages';
import {Parking} from 'src/app/model/parking';
import {ParkingBlock} from 'src/app/model/parking-block';
import {ParkingService} from 'src/app/services/parking.service';

@Component({
  selector: 'parking-config-edit',
  templateUrl: './parking-config-edit.component.html',
  styleUrls: ['./parking-config-edit.component.scss'],
})
export class ParkingConfigEditComponent implements OnInit {
  parking!: Parking;
  rootBlock!: ParkingBlock;
  isNewParking?: boolean;
  id?: string;

  constructor(
    public parkingService: ParkingService,
    private messageService: MessageService,
    private messagesObj: Messages,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.id = this.route.snapshot.params['id'];
  }

  ngOnInit() {
    if (this.id) {
      this.parkingService.getParking(this.id).subscribe(
        (parking) => {
          this.parking = parking;
          let tempBlock: ParkingBlock = new ParkingBlock();
          tempBlock.name = this.parking.name;
          tempBlock.blocks = this.parking.blocks;
          tempBlock.spots = this.parking.spots;
          this.rootBlock = tempBlock;
        }
      );
      this.isNewParking = false;
    } else {
      this.parking = new Parking();
      this.parking.status = 'Draft';
      this.parking.blocks = [];
      this.parking.spots = [];
      this.rootBlock = new ParkingBlock();
      this.isNewParking = true;
    }
  }

  saveParking() {
    this.parking.name = this.rootBlock.name;
    this.parking.blocks = this.rootBlock.blocks;
    this.parking.spots = this.rootBlock.spots;
    if (this.isNewParking) {
      this.parkingService.registerParking(this.parking).subscribe(
        () => {
          this.messageService.add({
            severity: 'success',
            detail: this.messagesObj.messages[1],
          });
        },
        (error) => {
          this.messageService.add({
            severity: 'error',
            summary: "Invalid Input",
            detail: error.error.message,
          });
        }
      );
    } else {
      this.parkingService.updateParking(this.id!, this.parking).subscribe(
        (responseParking:Parking) => {
          this.parking = responseParking;
          this.messageService.add({
            severity: 'info',
            detail: this.messagesObj.messages[1],
          });
        });
    }
    this.goToDashBoard();
  }

  goToDashBoard() {
    this.router.navigate(['parking', 'dashboard']);
  }
}
