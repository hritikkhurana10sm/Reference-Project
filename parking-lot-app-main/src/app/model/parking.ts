import {ParkingBlock} from "./parking-block";
import {ParkingSpot} from "./parking-spot";

export class Parking{
    id?:string;
    name?: string;
    status?:string;
    blocks?: ParkingBlock[];
    spots?: ParkingSpot[];
}
