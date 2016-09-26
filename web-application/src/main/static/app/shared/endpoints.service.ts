import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {Endpoints} from "./endpoints";
import {Endpoint} from "./endpoint";
import "../rxjs-operators";

/**
 * This service is the doorway to endpoint discovery.
 * It contacts the 'only' known relative endpoint and retrieves a list of 'unknown' mapped endpoints.
 */
@Injectable()
export class EndpointsService {

    map: {[name: string]: Endpoint} = {};
    error: string;
    endpoints: Endpoints;

    constructor(private http: Http) {
    }

    private getEndpoints(): Promise<Endpoints> {

        //TODO - Microservices 1.x - Define this endpoint as a standard/expected service discovery endpoint relative to the origin
        return this.http.get('/service/endpoints/conference')
            .toPromise()
            .then(response => response.json() as Endpoints)
            .catch(EndpointsService.handleError);
    }

    getEndpoint(name: string): Promise<Endpoint> {

        var ep = this.map[name];

        if (undefined == ep) {
            return this.getEndpoints().then(endpoints => this.cacheEndpoint(endpoints, name)).catch(EndpointsService.handleError);
        }

        return Promise.resolve(ep);
    }

    private cacheEndpoint(endpoints: Endpoints, name: string): Endpoint {
        this.endpoints = endpoints;

        console.log("Endpoints : " + this.endpoints.links['self']);

        for (let ep of this.endpoints.endpoints) {
            this.map[ep.name] = ep;
            console.log('Endpoint: ' + ep.name + ' : ' + ep.url);
        }

        return this.map[name];
    }

    private static handleError(error: any) {
        // TODO - Global error service
        let errMsg = (error.message) ? error.message :
            error.status ? `${error.status} - ${error.statusText}` : 'EndpointsService error';
        console.error(errMsg); // log to console instead
        return Observable.throw(errMsg);
    }
}