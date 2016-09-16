import {Injectable} from "@angular/core";
import {Speaker} from "./speaker";
import {Endpoint} from "../shared/endpoint";
import {Http} from "@angular/http";
import "../rxjs-operators";

@Injectable()
export class SpeakerService {

    constructor(private http: Http) {
    }

    //noinspection TypeScriptUnresolvedVariable
    getSpeakers(endPoint: Endpoint): Promise<Speaker[]> {

        return this.http.get(endPoint.url)
            .toPromise()
            .then(response => response.json() as Speaker[])
            .catch(this.handleError);
    }

    //noinspection TypeScriptUnresolvedVariable
    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // TODO - Display safe error
        //noinspection TypeScriptUnresolvedVariable
        return Promise.reject(error.message || error);
    }
}