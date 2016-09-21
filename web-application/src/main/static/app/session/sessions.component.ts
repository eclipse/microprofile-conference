import {Component, enableProdMode, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {Session} from "./session";
import {SessionService} from "./session.service";
import {EndpointsService} from "../shared/endpoints.service";
import {Endpoint} from "../shared/endpoint";

enableProdMode();

@Component({
    selector: 'sessions',
    templateUrl: 'app/session/sessions.component.html',
})
export class SessionsComponent implements OnInit {
    title = 'Sessions';
    sessions: Session[];
    selectedSession: Session;
    search: string;
    private endPoint: Endpoint;

    constructor(private router: Router, private sessionService: SessionService, private endpointsService: EndpointsService) {
    }

    getEndpoint(): void {
        this.endpointsService.getEndpoint("session").then(endPoint => this.setEndpoint(endPoint));
    }

    setEndpoint(endPoint: Endpoint): void {
        this.endPoint = endPoint;
        this.getSessions();
    }

    getSessions(): void {
        //noinspection TypeScriptUnresolvedFunction
        this.sessionService.getSessions(this.endPoint).then(sessions => this.sessions = sessions).catch(this.handleError);
    }

    ngOnInit(): void {
        this.getEndpoint();
    }

    onSelect(session: Session): void {
        this.selectedSession = session;
    }

    onSearch(search: string): void {
        this.search = search;
    }

    gotoDetail(): void {
        this.router.navigate(['/detail', this.selectedSession.id]);
    }

    //noinspection TypeScriptUnresolvedVariable
    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // TODO - Display safe error
        //noinspection TypeScriptUnresolvedVariable
        return Promise.reject(error.message || error);
    }
}