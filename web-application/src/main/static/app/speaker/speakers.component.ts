import {Component, enableProdMode, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {Speaker} from "./speaker";
import {SpeakerService} from "./speaker.service";
import {Input} from "@angular/core/src/metadata/directives";

enableProdMode();

@Component({
    selector: 'speakers',
    templateUrl: 'app/speaker/speakers.component.html',
})
export class SpeakersComponent implements OnInit {
    title = 'Speakers';
    speakers: Speaker[];
    selectedSpeaker: Speaker;
    search: string;

    constructor(private router: Router, private speakerService: SpeakerService) {
    }

    getSpeakers(): void {
        this.speakerService.getSpeakers().then(speakers => this.speakers = speakers).catch(SpeakersComponent.handleError);
    }

    ngOnInit(): void {
        let _self = this;
        this.speakerService.init(function () {
            _self.getSpeakers();
        });
    }

    onSelect(speaker: Speaker): void {
        this.selectedSpeaker = speaker;
    }

    onSearch(search: string): void {
        this.search = search;
    }

    gotoDetail(): void {
        this.router.navigate(['/detail', this.selectedSpeaker.id]);
    }

    //noinspection TypeScriptUnresolvedVariable
    private static handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // TODO - Display safe error
        //noinspection TypeScriptUnresolvedVariable
        return Promise.reject(error.message || error);
    }
}