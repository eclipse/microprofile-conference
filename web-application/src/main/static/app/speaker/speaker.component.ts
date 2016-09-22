import {Component, Input, enableProdMode} from "@angular/core";
import {Speaker} from "./speaker";

enableProdMode();

@Component({
    selector: 'speaker',
    templateUrl: 'app/speaker/speaker.component.html',
})

export class SpeakerComponent {
    title = 'Conference Speaker';
    @Input() speaker: Speaker;
}