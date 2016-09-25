import {Injectable} from "@angular/core";
import {Schedule} from "./schedule";
import {Endpoint} from "../shared/endpoint";
import {Http} from "@angular/http";
import "../rxjs-operators";
import {EndpointsService} from "../shared/endpoints.service";

@Injectable()
export class ScheduleService {

    private schedules: Schedule[];
    private endPoint: Endpoint;

    constructor(private http: Http, private endpointsService: EndpointsService) {
    }

    init(callback: () => void): void {

        if (undefined != this.endPoint) {
            callback();
        } else {
            this.endpointsService.getEndpoint("schedule").then(endPoint => this.setEndpoint(endPoint)).then(callback).catch(this.handleError);
        }
    }

    setEndpoint(endPoint: Endpoint): void {
        this.endPoint = endPoint;
    }

    //noinspection TypeScriptUnresolvedVariable
    getSchedules(): Promise<Schedule[]> {

        if (undefined != this.schedules) {
            return Promise.resolve(this.schedules);
        }

        return this.http.get(this.endPoint.url + '/all')
            .toPromise()
            .then(response => this.setSchedules(response.json()))
            .catch(this.handleError);
    }

    private setSchedules(any: any): Schedule[] {
        this.schedules = any as Schedule[];
        return this.schedules;
    }

    //noinspection TypeScriptUnresolvedVariable
    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // TODO - Display safe error
        //noinspection TypeScriptUnresolvedVariable
        return Promise.reject(error.message || error);
    }
}