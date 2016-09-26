import {Component, Input, enableProdMode, OnInit, OnDestroy} from "@angular/core";
import {Router, ActivatedRoute, Params} from "@angular/router";
import {Speaker} from "./speaker";
import {SpeakerService} from "./speaker.service";

enableProdMode();

@Component({
    selector: 'speakers',
    templateUrl: 'app/speaker/speakers.component.html',
})
export class SpeakersComponent implements OnInit, OnDestroy {

    title = 'Speakers';
    speakers: Speaker[];
    @Input() selectedSpeaker: Speaker;
    search: string;

    constructor(private router: Router, private route: ActivatedRoute, private speakerService: SpeakerService) {
    }

    ngOnInit(): void {
        let _self = this;

        this.speakerService.init(function () {
            _self.getSpeakers();

            _self.route.params.forEach((params: Params) => {
                _self.onSelectId(params['id']);
            });
        });
    }

    ngOnDestroy(): void {

    }

    getSpeakers(): void {
        this.speakerService.getSpeakers().then(speakers => this.speakers = speakers).catch(SpeakersComponent.handleError);
    }

    onSelectId(any: any): void {
        this.speakerService.getSpeakersById([any as string]).then(speaker => this.onSelect(speaker[0]));
    }

    onSelect(speaker: Speaker): void {

        if (undefined != speaker && null != speaker) {
            this.selectedSpeaker = speaker;
        }
    }

    onSearch(search: string): void {
        this.search = search;
    }

    private static handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // TODO - Display safe error
        return Promise.reject(error.message || error);
    }
}