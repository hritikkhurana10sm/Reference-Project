import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {delay} from 'rxjs';
import {Parking} from '../model/parking';
import {ParkingService} from '../services/parking.service';
import {ParkingBlock} from "../model/parking-block";
import {ConfirmationService, ConfirmEventType, MessageService} from "primeng/api";
import {Messages} from "../model/messages";
import {ParkingSpotService} from "../services/parking-spot.service";
import {UserProfile} from "../model/userProfile";
import {UserProfileService} from "../services/user-profile.service";
import {ParkingSpot} from "../model/parking-spot";

@Component({
  selector: 'user-dashboard',
  templateUrl: './user-dashboard.component.html',
  styleUrls: ['./user-dashboard.component.scss'],
  providers: [ConfirmationService]
})
export class UserDashboardComponent implements OnInit {

  parking?: Parking;
  parkingList: Array<Parking> = [];
  selectedParking?: Parking;
  rootBlock!: ParkingBlock;
  statusType?: string;
  vacateCarNum?: string;
  entryCarNum?: string;

  constructor(public parkingService: ParkingService, private router: Router, private messageService: MessageService, private messagesObj: Messages,
              private confirmationService: ConfirmationService, private spotService: ParkingSpotService, private userProfileService: UserProfileService) {
  }

  ngOnInit(): void {
    this.userProfileService.getProfileApi().subscribe((userProfile: UserProfile) => {  //TODO: hit this api with time interval := cookie exp
        this.userProfileService.setUserProfile(userProfile);
      }
    );
    this.parkingService.getParkingsByUser().subscribe((parkings) => {
      this.parkingList = parkings;
      if (this.parkingList.length != 0) {
        this.selectParking(this.parkingList.at(0));
      }
    });
    this.spotService.getSpotAllocateEmitter().subscribe(
      (allottedSpot) => {
        if(this.parking?.spots){
          for(let spot of this.parking?.spots){
            if(spot.id===allottedSpot.id){
              spot.carNum=allottedSpot.carNum;
              break;
            }
          }
        }
        if (this.parking?.blocks) {
          for (let block of this.parking?.blocks!) {
            if (this.parkingService.changeColorScheme(block, allottedSpot))
              break;
          }
        }
      });
  }

  selectParking(parking?: Parking) {
    this.selectedParking = undefined;
    this.parkingService.parkingActivationNotAllowed = false;
    setTimeout(() => {
      this.selectedParking = parking;
    }, 100);
    this.loadParking(parking?.id);
  }

  loadParking(id?: string) {
    this.parking = undefined;
    this.parkingService.getParking(id).pipe(delay(1000)).subscribe(
      (parkingData) => {
        this.parking = parkingData;
        if (this.parking.blocks != null) {
          for (let block of this.parking.blocks) {
            this.parkingService.traverseParkingBlock(block);
          }
        } else if (this.parking.spots == null || this.parking.spots.length === 0) {
          this.parkingService.parkingActivationNotAllowed = true;
        }
        let tempBlock: ParkingBlock = new ParkingBlock();
        tempBlock.name = this.parking.name;
        tempBlock.blocks = this.parking.blocks;
        tempBlock.spots = this.parking.spots;
        this.rootBlock = tempBlock;
        this.changeStatusBtn(this.parking.status!);
      });
  }

  registerParking() {
    this.router.navigate(['parking', 'config']);
  }

  editParking(pId?: string) {
    this.router.navigate(['parking', pId, 'config']);
  }

  updateStatus() {
    this.parkingService.updateStatus(this.selectedParking?.id!, this.statusType!).subscribe(
      (response: Parking) => {
        this.messageService.add({
          severity: 'info',
          detail: this.messagesObj.messages[1],
        });
        for (let parking of this.parkingList) {
          if (parking.id === response.id)
            parking.status = response.status;
        }
        this.changeStatusBtn(response.status!);
        this.loadParking(response.id);
      }
    );
  }

  automatedSpotAllocation() {
    if (this.parking) {
      this.spotService.automatedSpotAllocation(this.parking.id!, this.entryCarNum!).subscribe(
        (allottedSpot: ParkingSpot) => {
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
    this.entryCarNum = undefined;
  }

  spotDeallocation() {
    if (this.vacateCarNum) {
      this.spotService.spotDeallocation(this.vacateCarNum).subscribe(
        (vacatedSpot) => {
          if (this.parking?.spots != null) {
            for (let spot of this.parking.spots) {
              if (spot.id === vacatedSpot.id) {
                spot.carNum = undefined;
              }
            }
          }
          if (this.parking?.blocks != null) {
            for (let block of this.parking.blocks) {
              if (this.parkingService.changeColorScheme(block, vacatedSpot, true)) {
                break;
              }
            }
          }
          this.messageService.add({
            severity: 'info',
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
      this.vacateCarNum = undefined;
    }
  }

  changeStatusBtn(currentStatus: string) {
    if (currentStatus === "Draft")
      this.statusType = "Activate";
    else if (currentStatus === "Active")
      this.statusType = "Deactivate";
    else
      this.statusType = "Activate";
  }

  statusConfirmation() {
    this.confirmationService.confirm({
      message: 'Are you sure you want to ' + this.statusType + ' the Parking',
      header: 'Status Confirmation',
      icon: 'pi pi-info-triangle',
      accept: () => this.updateStatus(),
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
