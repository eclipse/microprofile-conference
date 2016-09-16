import {Component, enableProdMode, OnInit} from "@angular/core";
import {Vote} from "./vote";
import {VoteService} from "./vote.service";

enableProdMode();

@Component({
    selector: 'votes',
    templateUrl: 'app/vote/votes.component.html'
})

export class VotesComponent implements OnInit{
    title = 'Conference Votes';
    votes: Vote[];
    selectedVote: Vote;

    constructor(private voteService: VoteService) {
    }

    getVotes(): void {
        //noinspection TypeScriptUnresolvedFunction
        this.voteService.getVotes().then(votes => this.votes = votes);
    }

    ngOnInit(): void {
        this.getVotes();
    }

    onSelect(vote: Vote): void {
        this.selectedVote = vote;
    }
}