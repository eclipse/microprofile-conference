import {Component, Input, enableProdMode, OnInit, OnDestroy} from "@angular/core";
import {Router, ActivatedRoute} from "@angular/router";
import {Speaker} from "./speaker";
import {SpeakerService} from "./speaker.service";
import {Subscription} from "rxjs";

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
    private subscription: Subscription;

    constructor(private router: Router, private activatedRoute: ActivatedRoute, private speakerService: SpeakerService) {
    }

    ngOnInit(): void {
        let _self = this;

        this.speakerService.init(function () {
            _self.getSpeakers();

            _self.subscription = _self.activatedRoute.params.subscribe(params => {
                var s = params['selectedSpeaker'];
                if (undefined != s) {
                    _self.selectedSpeaker = s;
                }
            });
        });
    }

    ngOnDestroy(): void {
        this.subscription.unsubscribe();
    }

    getSpeakers(): void {
        this.speakerService.getSpeakers().then(speakers => this.speakers = speakers).catch(SpeakersComponent.handleError);
    }

    onSelect(speaker: Speaker): void {
        this.selectedSpeaker = speaker;
    }

    onSearch(search: string): void {
        this.search = search;
    }

    //noinspection TypeScriptUnresolvedVariable
    private static handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // TODO - Display safe error
        //noinspection TypeScriptUnresolvedVariable
        return Promise.reject(error.message || error);
    }
}