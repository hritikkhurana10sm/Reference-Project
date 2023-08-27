import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ParkingBlockConfigEditComponent} from './parking-block-config-edit.component';

describe('ParkingBlockConfigEditComponent', () => {
  let component: ParkingBlockConfigEditComponent;
  let fixture: ComponentFixture<ParkingBlockConfigEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ParkingBlockConfigEditComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ParkingBlockConfigEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
