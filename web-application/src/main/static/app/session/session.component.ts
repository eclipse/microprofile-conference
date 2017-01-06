import {Component, Input, Inject, OnInit, enableProdMode} from "@angular/core";
import {Session} from "./session";
import {SpeakerService} from "../speaker/speaker.service";
import {VoteService} from "../vote/vote.service";
import {Speaker} from "../speaker/speaker";

enableProdMode();

@Component({
    selector: 'session',
    templateUrl: 'app/session/session.component.html'
})

export class SessionComponent implements OnInit {
    title = 'Conference Session';
    voted = false;
    @Input() session: Session;
    @Input() speaker: Speaker;

    constructor(@Inject(VoteService) private voteService: VoteService) {
    }

    ngOnInit(): void {
        let _self = this;
        this.voteService.init(function () {
            _self.voteService.getVotes();
        });
    }

    rateSession(ratingValue: number): void {
      this.voteService.rateSession(this.session, ratingValue);
      this.voted = true;
    }
}
