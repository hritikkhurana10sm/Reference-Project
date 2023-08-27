import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {SignupDto} from "../model/signupDto";
import {SigninDto} from "../model/signinDto";
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";
import {ResetPasswordDto} from "../model/reset-passwordDto";

export const TOKEN_COOKIE_NAME = 'parking-lot-auth-token';
export const HEADERS = new HttpHeaders().set('Content-Type', 'application/json');

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseURL = '/api/auth';
  public isUserLoggedIn: boolean = false;

  constructor(private httpClient: HttpClient, private cookieService: CookieService, private router: Router) {
  }

  signupApi(signupDto: SignupDto): Observable<Object> {
    return this.httpClient.post<Object>(`${this.baseURL}/signup`, signupDto);
  }

  signinApi(signinDto: SigninDto): Observable<string> {
    const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded');
    const body = new URLSearchParams();
    body.set('username', signinDto.username!);
    body.set('password', signinDto.password!);
    return this.httpClient.post<string>(`${this.baseURL}/signin`, body.toString(), {headers});
  }

  initiateResetPassword(usernameOrEmail: string): Observable<Object> {
    return this.httpClient.post<Object>(`${this.baseURL}/reset-password/initiate`, usernameOrEmail,{headers:HEADERS});
  }

  resetPassword(userId: string, resetId: string, resetPasswordDto: ResetPasswordDto): Observable<Object> {
    return this.httpClient.post<Object>(`${this.baseURL}/reset-password/user/{userId}/reset/{resetId}`, resetPasswordDto,{headers:HEADERS});
  }

  setCookie(token: string) {
    const cookieOptions: any = {
      expires: 1,
      path: '/',                  //TODO: specific domain
      sameSite: 'Strict'
    };
    this.cookieService.set(TOKEN_COOKIE_NAME, token, cookieOptions);
  }

  isLoggedIn(): boolean {
    let jwtToken = this.getToken();
    this.isUserLoggedIn = !!(jwtToken);
    return this.isUserLoggedIn;
  }

  isLoggedInOrElseNavigateToSignin(): boolean {
    if (this.isLoggedIn()) {
      return true;
    }
    this.router.navigate(['user', 'signin']);
    return false;
  }

  isNotLoggedInOrElseNavigateToHome() {
    if (!this.isLoggedIn()) {
      return true;
    }
    this.router.navigate(['']);
    return false;
  }

  getToken(): string | undefined {
    return this.cookieService.get(TOKEN_COOKIE_NAME);
  }

  logOut() {
    this.cookieService.delete(TOKEN_COOKIE_NAME, '/');
    this.router.navigate(['user', 'signin']);
  }
}
