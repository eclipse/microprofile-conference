import {Injectable} from "@angular/core";
import {Session} from "./session";
import {Endpoint} from "../shared/endpoint";
import {Http} from "@angular/http";
import "../rxjs-operators";
import {EndpointsService} from "../shared/endpoints.service";

@Injectable()
export class SessionService {

    private sessions: Session[];
    private endPoint: Endpoint;

    constructor(private http: Http, private endpointsService: EndpointsService) {
    }

    init(callback: () => void): void {

        if (undefined != this.endPoint) {
            callback();
        } else {
            this.endpointsService.getEndpoint("session").then(endPoint => this.setEndpoint(endPoint)).then(callback).catch(this.handleError);
        }
    }

    setEndpoint(endPoint: Endpoint): void {
        this.endPoint = endPoint;
    }

    getSessions(): Promise<Session[]> {

        if (undefined != this.sessions) {
            return Promise.resolve(this.sessions);
        }

        return this.http.get(this.endPoint.url)
            .toPromise()
            .then(response => this.setSessions(response.json()))
            .catch(this.handleError);
    }

    private setSessions(any: any): Session[] {
        this.sessions = any as Session[];
        return this.sessions;
    }

    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // TODO - Display safe error
        return Promise.reject(error.message || error);
    }

    getSessionsById(ids: string[]): Promise<Session[]> {

        if (undefined == this.endPoint) {
            console.error("init must be called at least once");
        }

        return this.getSessions().then(sessions => this.setSessions(sessions).filter(session => this.isIn(session, ids)));
    }

    private isIn(session: Session, ids: string[]): boolean {
        for (var id of ids) {
            if (session.id == id) {
                return true;
            }
        }

        return false;
    }
}