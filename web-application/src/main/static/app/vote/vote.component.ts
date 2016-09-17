import {Component, Input, enableProdMode} from "@angular/core";
import {Vote} from "./vote";

enableProdMode();

@Component({
    selector: 'vote',
    templateUrl: 'app/vote/vote.component.html'
})

export class VoteComponent {
    title = 'Conference Vote';
    @Input()
    vote: Vote;
}