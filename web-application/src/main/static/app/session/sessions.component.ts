import {Component, enableProdMode, OnInit} from "@angular/core";
import {Session} from "./session";
import {SessionService} from "./session.service";

enableProdMode();

@Component({
    selector: 'sessions',
    templateUrl: 'app/session/sessions.component.jsp'
})

export class SessionsComponent implements OnInit {
    title = 'Conference Sessions';
    sessions: Session[];
    selectedSession: Session;

    constructor(private sessionService: SessionService) {
    }

    getSessions(): void {
        //noinspection TypeScriptUnresolvedFunction
        this.sessionService.getSessions().then(sessions => this.sessions = sessions);
    }

    ngOnInit(): void {
        this.getSessions();
    }

    onSelect(session: Session): void {
        this.selectedSession = session;
    }
}