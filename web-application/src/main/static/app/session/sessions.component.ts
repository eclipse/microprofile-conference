import {Component, enableProdMode, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {Session} from "./session";
import {SessionService} from "./session.service";

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

    constructor(private router: Router, private sessionService: SessionService) {
    }

    getSessions(): void {
        this.sessionService.getSessions().then(sessions => this.sessions = sessions).catch(SessionsComponent.handleError);
    }

    ngOnInit(): void {
        let _self = this;
        this.sessionService.init(function () {
            _self.getSessions();
        });
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
    private static handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // TODO - Display safe error
        //noinspection TypeScriptUnresolvedVariable
        return Promise.reject(error.message || error);
    }
}