import {ParkingSpot} from "./parking-spot";

export class ParkingBlock{
    id?:string;
    parkingId?:string;
    parentBlockId?:string;
    sequence: number = 0;
    name?: string;
    blocks?: ParkingBlock[];
    spots?: ParkingSpot[];
    totalSpots:number=0;
    occupiedSpots:number=0;
}
