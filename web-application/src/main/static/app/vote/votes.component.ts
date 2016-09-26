import {Component, enableProdMode, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {VoteService} from "./vote.service";
import {Rating} from "./rating";

enableProdMode();

@Component({
    selector: 'votes',
    templateUrl: 'app/vote/votes.component.html'
})

export class VotesComponent implements OnInit {
    title = 'Votes';
    votes: Rating[];
    selectedVote: Rating;

    constructor(private router: Router, private voteService: VoteService) {
    }

    ngOnInit(): void {
        let _self = this;
        this.voteService.init(function () {
            _self.getVotes();
        });
    }

    getVotes(): void {
        this.voteService.getVotes().then(votes => this.votes = votes).catch(VotesComponent.handleError);
    }

    onSelect(vote: Rating): void {
        this.selectedVote = vote;
    }

    gotoDetail(): void {
        this.router.navigate(['/sessions', {id: this.selectedVote.session}]);
    }

    private static handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // TODO - Display safe error
        return Promise.reject(error.message || error);
    }
}