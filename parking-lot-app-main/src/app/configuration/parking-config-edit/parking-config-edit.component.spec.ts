import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ParkingConfigEditComponent} from './parking-config-edit.component';

describe('ParkingConfigEditComponent', () => {
  let component: ParkingConfigEditComponent;
  let fixture: ComponentFixture<ParkingConfigEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ParkingConfigEditComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ParkingConfigEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
