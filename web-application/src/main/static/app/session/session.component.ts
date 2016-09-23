import {Component, Input, enableProdMode} from "@angular/core";
import {Session} from "./session";
import {SpeakerService} from "../speaker/speaker.service";
import {Speaker} from "../speaker/speaker";

enableProdMode();

@Component({
    selector: 'session',
    templateUrl: 'app/session/session.component.html'
})

export class SessionComponent {
    title = 'Conference Session';
    @Input() session: Session;
    @Input() speaker: Speaker;
}