import {HttpClient} from '@angular/common/http';
import {EventEmitter, Injectable} from '@angular/core';
import {ParkingSpot} from '../model/parking-spot';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ParkingSpotService {

  private baseURL = '/api/parking';
  private spotAllocate: EventEmitter<ParkingSpot> = new EventEmitter<ParkingSpot>();

  constructor(private httpClient: HttpClient) {
  }

  spotAllocation(id: string, carNum: string): Observable<ParkingSpot> {
    return this.httpClient.put<ParkingSpot>(`${this.baseURL}/spot/${id}/car-enter`, carNum);
  }

  automatedSpotAllocation(parkingId: string, carNum: string): Observable<ParkingSpot> {
    return this.httpClient.post<ParkingSpot>(`${this.baseURL}/${parkingId}/spot/car-enter`, carNum);
  }

  spotDeallocation(carNum: string): Observable<ParkingSpot> {
    return this.httpClient.put<ParkingSpot>(`${this.baseURL}/spot/car-exit`, carNum);
  }

  spotAllocateEvent(allottedSpot: ParkingSpot) {
    this.spotAllocate.emit(allottedSpot);
  }

  getSpotAllocateEmitter() {
    return this.spotAllocate;
  }
}
