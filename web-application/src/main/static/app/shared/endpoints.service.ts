import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {Endpoints} from "./endpoints";
import {Endpoint} from "./endpoint";
import "../rxjs-operators";

@Injectable()
export class EndpointsService {

    map: {[name: string]: Endpoint} = {};
    error: string;
    endpoints: Endpoints;

    constructor(private http: Http) {
    }

    //noinspection TypeScriptUnresolvedVariable
    private getEndpoints(): Promise<Endpoints> {

        return this.http.get('/service/endpoints/conference')
            .toPromise()
            .then(response => response.json() as Endpoints)
            .catch(EndpointsService.handleError);
    }

    //noinspection TypeScriptUnresolvedVariable
    getEndpoint(name: string): Promise<Endpoint> {

        var ep = this.map[name];

        if (undefined == ep) {
            return this.getEndpoints().then(endpoints => this.cacheEndpoint(endpoints, name)).catch(EndpointsService.handleError);
        }

        //noinspection TypeScriptUnresolvedVariable
        return Promise.resolve(ep);
    }

    private cacheEndpoint(endpoints: Endpoints, name: string): Endpoint {
        this.endpoints = endpoints;

        for (let ep of this.endpoints.endpoints) {
            this.map[ep.name] = ep;
            console.log('Loaded endpoint: ' + ep.name + " : " + ep.url);
        }

        return this.map[name];
    }

    private static handleError(error: any) {
        // TODO - Global error service
        let errMsg = (error.message) ? error.message :
            error.status ? `${error.status} - ${error.statusText}` : 'Server error';
        console.error(errMsg); // log to console instead
        return Observable.throw(errMsg);
    }
}