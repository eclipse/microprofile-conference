import {Component, enableProdMode, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {Speaker} from "./speaker";
import {SpeakerService} from "./speaker.service";
import {EndpointsService} from "../shared/endpoints.service";
import {Endpoint} from "../shared/endpoint";

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
    private endPoint: Endpoint;

    constructor(private router: Router, private speakerService: SpeakerService, private endpointsService: EndpointsService) {
    }

    getEndpoint(): void {
        this.endpointsService.getEndpoint("speaker").then(endPoint => this.setEndpoint(endPoint));
    }

    setEndpoint(endPoint: Endpoint): void {
        this.endPoint = endPoint;
        this.getSpeakers();
    }

    getSpeakers(): void {
        //noinspection TypeScriptUnresolvedFunction
        this.speakerService.getSpeakers(this.endPoint).then(speakers => this.speakers = speakers).catch(this.handleError);
    }

    ngOnInit(): void {
        this.getEndpoint();
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
    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // TODO - Display safe error
        //noinspection TypeScriptUnresolvedVariable
        return Promise.reject(error.message || error);
    }
}