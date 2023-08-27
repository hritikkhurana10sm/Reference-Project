import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {UserProfileService} from './services/user-profile.service';
import {AuthService} from "./services/auth.service";
import {UserProfile} from "./model/userProfile";
import {GlobalLoadingService} from "./services/global-loading.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'Parking Lot Management';
  userProfile?: UserProfile;
  loader?: boolean;

  constructor(private userProfileService: UserProfileService, public authService: AuthService, private globalLoadingSpinner: GlobalLoadingService, private cdr:ChangeDetectorRef) {
  }

  ngOnInit() {
    this.globalLoadingSpinner.isLoading().subscribe(loader => {
      this.loader = loader;
      this.cdr.detectChanges()
    });
    this.userProfileService.getUserProfile().subscribe((userProfile: UserProfile | undefined) => {
        this.userProfile = userProfile;
      }
    );
  }

  signOutClicked($event: any) {
    $event.hide();
    this.userProfile = undefined;
    this.authService.logOut();
  }
}
