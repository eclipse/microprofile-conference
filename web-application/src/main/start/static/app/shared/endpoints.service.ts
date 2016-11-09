import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {Endpoints} from "./endpoints";
import {Endpoint} from "./endpoint";
import "../rxjs-operators";

@Injectable()
export class EndpointsService {

    map: {[name: string]: Endpoint} =
    {
        'speaker': <Endpoint> {name: 'speaker', url: 'http://localhost:4040/speaker'},
        'session': <Endpoint> {name: 'session', url: 'http://localhost:5050/sessions'},
        'schedule': <Endpoint> {name: 'schedule', url: 'http://localhost:6060/schedule'},
        'vote': <Endpoint> {name: 'vote', url: 'http://localhost:7070/vote'}
    };
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
            error.status ? `${error.status} - ${error.statusText}` : 'Server error';
        console.error(errMsg); // log to console instead
        return Observable.throw(errMsg);
    }
}
