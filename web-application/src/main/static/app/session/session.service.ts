import {Injectable} from "@angular/core";
import {Session} from "./session";
import {Endpoint} from "../shared/endpoint";
import {Http} from "@angular/http";
import "../rxjs-operators";

@Injectable()
export class SessionService {

    constructor(private http: Http) {
    }

    //noinspection TypeScriptUnresolvedVariable
    getSessions(endPoint: Endpoint): Promise<Session[]> {

        return this.http.get(endPoint.url)
            .toPromise()
            .then(response => response.json() as Session[])
            .catch(this.handleError);
    }

    //noinspection TypeScriptUnresolvedVariable
    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // TODO - Display safe error
        //noinspection TypeScriptUnresolvedVariable
        return Promise.reject(error.message || error);
    }
}