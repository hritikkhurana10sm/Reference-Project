import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class GlobalLoadingService {
  private showSpinner:BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  show(): void {
    this.showSpinner.next(true);
  }

  hide(): void {
    this.showSpinner.next(false);
  }

  isLoading():Observable<boolean>{
    return this.showSpinner.asObservable();
  }
}
