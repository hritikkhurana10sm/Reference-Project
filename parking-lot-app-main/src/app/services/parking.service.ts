import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Parking} from '../model/parking';
import {Observable} from 'rxjs';
import {ParkingBlock} from "../model/parking-block";
import {ParkingSpot} from "../model/parking-spot";

@Injectable({
  providedIn: 'root'
})
export class ParkingService {

  private baseURL = '/api/parking';
  parkingActivationNotAllowed: boolean = false;

  constructor(private httpClient: HttpClient) {
  }

  getParkingsByUser(): Observable<Array<Parking>> {
    return this.httpClient.get<Array<Parking>>(`${this.baseURL}/all`);
  }

  getParking(id?: string): Observable<Parking> {
    return this.httpClient.get<Parking>(`${this.baseURL}/${id}`);
  }

  registerParking(parking: Parking): Observable<Object> {
    return this.httpClient.post(`${this.baseURL}/register`, parking);
  }

  updateParking(id: string, parking: Parking): Observable<Parking> {
    return this.httpClient.put<Parking>(`${this.baseURL}/update/${id}`, parking);
  }

  updateStatus(id: string, status: string): Observable<Object> {
    if (status === "Activate")
      status = "Active";
    else
      status = "Inactive";
    return this.httpClient.put(`${this.baseURL}/update/${id}/status`, status);
  }

  traverseParkingBlock(node: ParkingBlock): Array<number> {
    node.totalSpots = 0;
    node.occupiedSpots = 0;
    if (node.blocks != null) {
      for (let subBlock of node.blocks) {
        node.totalSpots += this.traverseParkingBlock(subBlock)[0];
        node.occupiedSpots += this.traverseParkingBlock(subBlock)[1];
      }
      return [node.totalSpots, node.occupiedSpots];
    }
    if (node.spots != null) {
      node.totalSpots += node.spots.length;
      for (let spot of node.spots) {
        if (spot.carNum)
          node.occupiedSpots++;
      }
      return [node.totalSpots, node.occupiedSpots];
    }
    if (!node.blocks && !node.spots) {
      this.parkingActivationNotAllowed = true;
    }
    return [node.totalSpots, node.occupiedSpots];
  }

  changeColorScheme(node: ParkingBlock, targetSpot:ParkingSpot, vacateFlag: boolean = false): boolean {
    if (vacateFlag) {
      if (node.blocks != null) {
        for (let subBlock of node.blocks) {
          if (this.changeColorScheme(subBlock, targetSpot, true)) {
            node.occupiedSpots--;
            return true;
          }
        }
      }
      if (node.spots != null) {
        for (let spot of node.spots) {
          if (spot.id === targetSpot.id) {
            spot.carNum = undefined;
            node.occupiedSpots--;
            return true;
          }
        }
      }
    } else {
      if (node.blocks != null) {
        for (let subBlock of node.blocks) {
          if (this.changeColorScheme(subBlock, targetSpot)) {
            node.occupiedSpots++;
            return true;
          }
        }
      }
      if (node.spots != null) {
        for (let spot of node.spots) {
          if (spot.id === targetSpot.id) {
            spot.carNum=targetSpot.carNum;
            node.occupiedSpots++;
            return true;
          }
        }
      }
    }
    return false;
  }
}
