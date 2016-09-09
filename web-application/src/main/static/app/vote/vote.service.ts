import {Injectable} from "@angular/core";
import {Vote} from "./vote";

@Injectable()
export class VoteService {

    votes: Vote[];

    getVotes(): void {
    }
}