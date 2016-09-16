//This is the same model our service emits
import {Attendee} from "./attendee";
export class Vote {
    nextAttendeeId: number;
    attendees: {[key: number]: Attendee} = {};

    nextSessionId: number;
    allRatings: {[key: number]: Attendee} = {};

    ratingIdsBySession: {[key: string]: number[]} = {};
    ratingIdsByAttendee: {[key: number]: number[]} = {};
}