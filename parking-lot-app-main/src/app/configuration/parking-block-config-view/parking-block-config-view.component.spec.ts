import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ParkingBlockConfigViewComponent} from './parking-block-config-view.component';

describe('ParkingBlockConfigViewComponent', () => {
  let component: ParkingBlockConfigViewComponent;
  let fixture: ComponentFixture<ParkingBlockConfigViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ParkingBlockConfigViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ParkingBlockConfigViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
