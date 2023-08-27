import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {ParkingBlock} from "../../model/parking-block";

@Component({
  selector: 'parking-block-config-view',
  templateUrl: './parking-block-config-view.component.html',
  styleUrls: ['./parking-block-config-view.component.scss']
})
export class ParkingBlockConfigViewComponent implements OnChanges {

  @Input() inputBlock!: ParkingBlock;
  @Input() status!: string;
  selectedBlock?: ParkingBlock;

  ngOnChanges(change: SimpleChanges) {
    if (this.inputBlock)
      this.selectBlock(this.inputBlock.blocks?.at(0));
  }

  selectBlock(block?: ParkingBlock) {
    this.selectedBlock = undefined;
    setTimeout(() => {
      this.selectedBlock = block;
    }, 100);
  }
}
