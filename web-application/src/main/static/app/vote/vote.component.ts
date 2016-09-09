import {Component, enableProdMode} from "@angular/core";
import { Vote } from './vote';

enableProdMode();

@Component({
    selector: 'vote',
    templateUrl: 'vote.component.jsp'
})


export class VoteComponent {
    vote: Vote;
}