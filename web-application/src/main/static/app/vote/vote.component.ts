import {Component, enableProdMode} from "@angular/core";
import { Vote } from './vote';

enableProdMode();

@Component({
    selector: 'vote',
    templateUrl: 'app/vote/vote.component.jsp'
})


export class VoteComponent {
    vote: Vote;
}