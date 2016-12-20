import {Injectable} from "@angular/core";
import {Endpoint} from "../shared/endpoint";
import {Http, Response} from "@angular/http";
import "../rxjs-operators";
import {EndpointsService} from "../shared/endpoints.service";
import {Rating} from "./rating";
import {Session} from "../session/session";
import {Attendee} from "./attendee";

@Injectable()
export class VoteService {

    private votes: Rating[];
    private endPoint: Endpoint;

    constructor(private http: Http, private endpointsService: EndpointsService) {
    }

    init(callback: () => void): void {
        if (undefined != this.endPoint) {
            callback();
        } else {
            this.endpointsService.getEndpoint("vote").then(endPoint => this.setEndpoint(endPoint)).then(callback).catch(this.handleError);
        }
    }

    setEndpoint(endPoint: Endpoint): void {
        this.endPoint = endPoint;
    }

    getVotes(): Promise<Rating[]> {

        if (undefined != this.votes) {
            return Promise.resolve(this.votes);
        }

        console.info('Loading votes...');
        return this.refreshVoteData();
    }

    rateSession(session: Session, ratingNumber: number) {
      var attendee: any = { "name" : "Test User"};
      this.http.post(this.endPoint.url + "/attendee", attendee ).toPromise().then(
        response => this.makeVoteCall(session, response, ratingNumber)
      ).catch(error => this.handleError(error));
    }

    private makeVoteCall(session: Session, attendeeResponse: Response , rating: number) {
      var sessionRating : Rating = {"id": "null", "session": session.title, "attendeeId" : attendeeResponse.json().id, "rating": rating};
      this.http.post(this.endPoint.url + "/rate", sessionRating ).toPromise().then(
        response => this.refreshVoteData()
      ).catch(error => this.handleError(error));
    }

    private refreshVoteData(): Promise<Rating[]> {
      return this.http.get(this.endPoint.url + '/rate')
          .toPromise()
          .then(response => this.setVotes(response.json()))
          .catch(this.handleError);
    }

    private setVotes(any: any): Rating[] {
        this.votes = any as Rating[];
        return this.votes;
    }

    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // TODO - Display safe error
        return Promise.reject(error.message || error);
    }
}
