import {Component, enableProdMode, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {VoteService} from "./vote.service";
import {EndpointsService} from "../shared/endpoints.service";
import {Endpoint} from "../shared/endpoint";
import {Rating} from "./rating";
import {Session} from "../session/session";

enableProdMode();

@Component({
    selector: 'votes',
    templateUrl: 'app/vote/votes.component.html'
})

export class VotesComponent implements OnInit {
    title = 'Votes';
    votes: Rating[];
    selectedSession: Session;
    endPoint: Endpoint;

    constructor(private router: Router, private voteService: VoteService, private endpointsService: EndpointsService) {
    }

    getEndpoint(): void {
        this.endpointsService.getEndpoint("vote").then(endPoint => this.setEndpoint(endPoint));
    }

    setEndpoint(endPoint: Endpoint): void {
        this.endPoint = endPoint;
        this.getVotes();
    }

    getVotes(): void {
        //noinspection TypeScriptUnresolvedFunction
        this.voteService.getRatings(this.endPoint, this.selectedSession.id).then(votes => this.votes = votes).catch(this.handleError);
    }

    ngOnInit(): void {
        this.getEndpoint();
    }

    onSelect(session: Session): void {
        this.selectedSession = session;
    }

    gotoDetail(): void {
        //this.router.navigate(['/detail', this.selectedSession.nextAttendeeId]);
    }

    //noinspection TypeScriptUnresolvedVariable
    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // TODO - Display safe error
        //noinspection TypeScriptUnresolvedVariable
        return Promise.reject(error.message || error);
    }
}