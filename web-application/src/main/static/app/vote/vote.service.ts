import {Injectable} from "@angular/core";
import {Endpoint} from "../shared/endpoint";
import {Http} from "@angular/http";
import "../rxjs-operators";
import {EndpointsService} from "../shared/endpoints.service";
import {Rating} from "./rating";

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
