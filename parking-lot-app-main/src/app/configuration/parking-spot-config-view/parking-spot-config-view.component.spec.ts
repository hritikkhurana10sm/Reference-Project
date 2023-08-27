import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ParkingSpotConfigViewComponent} from './parking-spot-config-view.component';

describe('ParkingSpotConfigViewComponent', () => {
  let component: ParkingSpotConfigViewComponent;
  let fixture: ComponentFixture<ParkingSpotConfigViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ParkingSpotConfigViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ParkingSpotConfigViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
