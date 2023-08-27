import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {ParkingBlock} from 'src/app/model/parking-block';
import {ParkingSpot} from 'src/app/model/parking-spot';
import {ConfirmationService, ConfirmEventType, MessageService} from "primeng/api";
import {ParkingService} from "../../services/parking.service";

@Component({
  selector: 'parking-block-config-edit',
  templateUrl: './parking-block-config-edit.component.html',
  styleUrls: ['./parking-block-config-edit.component.scss'],
  providers: [ConfirmationService]
})
export class ParkingBlockConfigEditComponent implements OnInit, OnChanges {

  @Input() inputBlock?: ParkingBlock;
  @Input() level!: number;
  @Output() sendBlockCopies: EventEmitter<Array<ParkingBlock>> = new EventEmitter();
  @Output() deleteBlock: EventEmitter<ParkingBlock> = new EventEmitter();
  selectedBlock?: ParkingBlock;
  numberOfAddOns?: number;
  inputName: string = "";

  constructor(private confirmationService: ConfirmationService, private messageService: MessageService, private parkingService: ParkingService) {
  }

  ngOnInit() {
    if (this.level === 0)
      this.inputName = "Parking Name";
    else
      this.inputName = "Block Name";
  }

  ngOnChanges(change: SimpleChanges) {
    if (this.inputBlock)
      this.selectBlock(this.inputBlock.blocks?.at(0));
  }

  addBlocks(numberOfBlocks: number) {
    this.numberOfAddOns = undefined;
    if (!this.inputBlock!.blocks) {
      this.inputBlock!.blocks = [];
    }
    let max = 0;
    for (let block of this.inputBlock!.blocks) {
      if (block.sequence > max) {
        max = block.sequence;
      }
    }
    for (let i = 0; i < numberOfBlocks; i++) {
      let childBlock = new ParkingBlock();
      childBlock.sequence = max + 1;
      max += 1;
      childBlock.name = childBlock.sequence.toString();
      this.inputBlock!.blocks?.push(childBlock);
    }
  }

  addSpots(numberOfSpots: number) {
    this.numberOfAddOns = undefined;
    if (!this.inputBlock!.spots) {
      this.inputBlock!.spots = [];
    }

    let max = 0;
    for (let spot of this.inputBlock!.spots) {
      if (spot.sequence > max) {
        max = spot.sequence;
      }
    }

    for (let i = 0; i < numberOfSpots; i++) {
      let childSpot = new ParkingSpot();
      childSpot.sequence = max + 1;
      childSpot.name = childSpot.sequence.toString();
      max += 1;
      this.inputBlock!.spots?.push(childSpot);
    }
  }

  selectBlock(block?: ParkingBlock) {
    this.selectedBlock = undefined;
    setTimeout(() => {
      this.selectedBlock = block;
    }, 100);
  }

  copyBlock(numberOfCopies: number) {
    this.numberOfAddOns = undefined;
    let blockCopies: Array<ParkingBlock> = new Array<ParkingBlock>();
    for (let i = 0; i < numberOfCopies; i++) {
      let blockCopy = new ParkingBlock();
      blockCopy.blocks = this.inputBlock!.blocks;
      blockCopy.spots = this.inputBlock!.spots;
      blockCopies.push(blockCopy);
    }
    this.sendBlockCopies.emit(blockCopies);
  }

  addBlockCopies($event: Array<ParkingBlock>) {
    if (!this.inputBlock!.blocks) {
      this.inputBlock!.blocks = [];
    }
    let max = 0;
    for (let block of this.inputBlock!.blocks) {
      if (block.sequence > max) {
        max = block.sequence;
      }
    }
    for (let block of $event) {
      block.sequence = max + 1;
      max += 1;
      block.name = block.sequence.toString();
      this.inputBlock!.blocks?.push(block);
    }
  }

  blockDeletion() {
    this.deleteBlock.emit(this.inputBlock!);
  }

  catchDeleteBlockEvent($event?: ParkingBlock) {
    let tempBlock: Array<ParkingBlock> = new Array<ParkingBlock>();
    for (let block of this.inputBlock!.blocks!) {
      if (block === $event) {
        block.blocks = undefined;
        block.spots = undefined;
        block.name = undefined;
        block.sequence = 0;
      } else {
        tempBlock.push(block);
      }
    }
    this.inputBlock!.blocks = tempBlock;
    this.selectBlock(this.inputBlock!.blocks!.at(0));
  }

  deleteConfirmation(blockName: String) {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete the Block: ' + blockName,
      header: 'Delete Confirmation',
      icon: 'pi pi-info-circle',
      accept: () => this.blockDeletion(),
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
