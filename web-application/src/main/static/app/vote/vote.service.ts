import {Injectable} from "@angular/core";
import {Endpoint} from "../shared/endpoint";
import {Http} from "@angular/http";
import "../rxjs-operators";
import {Rating} from "./rating";

@Injectable()
export class VoteService {

    constructor(private http: Http) {
    }

    //noinspection TypeScriptUnresolvedVariable
    getRatings(endPoint: Endpoint, session: string): Promise<Rating[]> {

        return this.http.post(endPoint.url + '/ratingsBySession', session)
            .toPromise()
            .then(response => response.json() as Rating[])
            .catch(VoteService.handleError);
    }

    //noinspection TypeScriptUnresolvedVariable
    private static handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // TODO - Display safe error
        //noinspection TypeScriptUnresolvedVariable
        return Promise.reject(error.message || error);
    }
}