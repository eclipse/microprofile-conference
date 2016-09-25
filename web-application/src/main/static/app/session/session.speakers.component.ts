import {Component, Input, enableProdMode, OnChanges, SimpleChanges} from "@angular/core";
import {Speaker} from "../speaker/speaker";
import {Router} from "@angular/router";
import {SpeakerService} from "../speaker/speaker.service";

enableProdMode();

@Component({
    selector: 'session-speakers',
    templateUrl: 'app/session/session.speakers.component.html'

})

export class SessionSpeakersComponent implements OnChanges {
    title = 'Conference Session Speakers';
    speakers: Speaker[];
    @Input() speakerIds: string[];

    constructor(private router: Router, private speakerService: SpeakerService) {
    }

    ngOnChanges(changes: SimpleChanges) {
        if (undefined == changes['speakerIds'].currentValue
            || undefined == changes['speakerIds'].previousValue
            || changes['speakerIds'].currentValue.toString() != changes['speakerIds'].previousValue.toString()) {

            if (undefined != this.speakerIds) {

                var _self = this;

                this.speakerService.init(function () {
                    _self.speakerService.getSpeakersById(_self.speakerIds).then(speakers => _self.speakers = speakers)
                });
            }

        }
    }

    onSelect(speaker: Speaker): void {
        this.router.navigate(['/speakers', {id: speaker.id}]);
    }
}