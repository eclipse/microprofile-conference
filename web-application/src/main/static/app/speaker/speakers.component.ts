import {Component, enableProdMode, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {Speaker} from "./speaker";
import {SpeakerService} from "./speaker.service";

enableProdMode();

@Component({
    selector: 'speakers',
    templateUrl: 'app/speaker/speakers.component.jsp'
})

export class SpeakersComponent implements OnInit{
    title = 'Speakers';
    speakers: Speaker[];
    selectedSpeaker: Speaker;

    constructor(private router: Router, private speakerService: SpeakerService) {
    }

    getSpeakers(): void {
        //noinspection TypeScriptUnresolvedFunction
        this.speakerService.getSpeakers().then(speakers => this.speakers = speakers);
    }

    ngOnInit(): void {
        this.getSpeakers();
    }

    onSelect(speaker: Speaker): void {
        this.selectedSpeaker = speaker;
    }

    gotoDetail(): void {
        this.router.navigate(['/detail', this.selectedSpeaker.id]);
    }
}