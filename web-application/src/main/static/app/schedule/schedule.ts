//This is the same model our service emits
import {Timestamp} from "rxjs";
export class Schedule {
    id: string;
    date: Date;
    startTime: string;
    venue: string;
}