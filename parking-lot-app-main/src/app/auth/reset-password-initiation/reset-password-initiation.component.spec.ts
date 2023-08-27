import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ResetPasswordInitiationComponent} from './reset-password-initiation.component';

describe('ResetPasswordInitiationComponent', () => {
  let component: ResetPasswordInitiationComponent;
  let fixture: ComponentFixture<ResetPasswordInitiationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ResetPasswordInitiationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResetPasswordInitiationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
