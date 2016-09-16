import {Component, enableProdMode, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {Vote} from "./vote";
import {VoteService} from "./vote.service";
import {EndpointsService} from "../shared/endpoints.service";
import {Endpoint} from "../shared/endpoint";

enableProdMode();

@Component({
    selector: 'votes',
    templateUrl: 'app/vote/votes.component.html'
})

export class VotesComponent implements OnInit {
    title = 'Votes';
    votes: Vote[];
    selectedVote: Vote;
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
        this.voteService.getVotes(this.endPoint).then(votes => this.votes = votes).catch(this.handleError);
    }

    ngOnInit(): void {
        this.getEndpoint();
    }

    onSelect(vote: Vote): void {
        this.selectedVote = vote;
    }

    gotoDetail(): void {
        this.router.navigate(['/detail', this.selectedVote.nextAttendeeId]);
    }

    //noinspection TypeScriptUnresolvedVariable
    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // TODO - Display safe error
        //noinspection TypeScriptUnresolvedVariable
        return Promise.reject(error.message || error);
    }
}