import {Injectable} from "@angular/core";
import {Schedule} from "./schedule";
import {Endpoint} from "../shared/endpoint";
import {Http} from "@angular/http";
import "../rxjs-operators";

@Injectable()
export class ScheduleService {

    constructor(private http: Http) {
    }

    //noinspection TypeScriptUnresolvedVariable
    getSchedules(endPoint: Endpoint): Promise<Schedule[]> {

        return this.http.get(endPoint.url)
            .toPromise()
            .then(response => response.json() as Schedule[])
            .catch(this.handleError);
    }

    //noinspection TypeScriptUnresolvedVariable
    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // TODO - Display safe error
        //noinspection TypeScriptUnresolvedVariable
        return Promise.reject(error.message || error);
    }
}