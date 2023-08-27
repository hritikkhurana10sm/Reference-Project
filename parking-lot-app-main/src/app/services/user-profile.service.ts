import {Injectable} from '@angular/core';
import {AuthService} from "./auth.service";
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, Observable} from "rxjs";
import {UserProfile} from "../model/userProfile";

@Injectable({
  providedIn: 'root',
})

export class UserProfileService {

  private baseURL: string = '/api/auth';
  private userProfileSubject: BehaviorSubject<UserProfile | undefined> = new BehaviorSubject<UserProfile | undefined>(undefined);

  constructor(private httpClient: HttpClient, private authService: AuthService) {
  }

  getProfileApi(): Observable<UserProfile> {
    return this.httpClient.get<UserProfile>(`${this.baseURL}/profile`);
  }

  setUserProfile(profile: UserProfile) {
    this.userProfileSubject.next(profile);
  }

  getUserProfile(): Observable<UserProfile | undefined> {
    return this.userProfileSubject.asObservable();
  }
}
