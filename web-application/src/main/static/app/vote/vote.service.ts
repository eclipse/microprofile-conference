import {Injectable} from "@angular/core";
import {Vote} from "./vote";
import {Endpoint} from "../shared/endpoint";
import {Http} from "@angular/http";
import "../rxjs-operators";

@Injectable()
export class VoteService {

    constructor(private http: Http) {
    }

    //noinspection TypeScriptUnresolvedVariable
    getVotes(endPoint: Endpoint): Promise<Vote[]> {

        return this.http.get(endPoint.url)
            .toPromise()
            .then(response => response.json() as Vote[])
            .catch(this.handleError);
    }

    //noinspection TypeScriptUnresolvedVariable
    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // TODO - Display safe error
        //noinspection TypeScriptUnresolvedVariable
        return Promise.reject(error.message || error);
    }
}