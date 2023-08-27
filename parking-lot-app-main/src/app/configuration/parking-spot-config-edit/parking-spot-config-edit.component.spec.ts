import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ParkingSpotConfigEditComponent} from './parking-spot-config-edit.component';

describe('ParkingSpotConfigEditComponent', () => {
  let component: ParkingSpotConfigEditComponent;
  let fixture: ComponentFixture<ParkingSpotConfigEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ParkingSpotConfigEditComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ParkingSpotConfigEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
