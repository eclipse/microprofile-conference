import {Component, enableProdMode} from "@angular/core";
import {Speaker} from "./speaker/speaker";
import { SpeakerService } from './speaker/speaker.service';
import {ScheduleService} from "./schedule/schedule.service";
import {SessionService} from "./session/session.service";
import {VoteService} from "./vote/vote.service";

enableProdMode();

@Component({
    selector: 'microprofile-conference',
    providers: [SpeakerService, ScheduleService, SessionService, VoteService],
    template: `  
  <h3>{{title}}</h3>
  <div class="list-group">
    <a href="#" class="list-group-item list-group-item-action" 
      *ngFor="let speaker of speakers"
      [class.selected]="speaker === selectedSpeaker"
      (click)="onSelect(speaker)">
      <span class="badge">{{speaker.id}}</span> {{speaker.nameLast}}
    </a>
  </div>
  <speaker [speaker]="selectedSpeaker"></speaker>
`,
})

export class AppComponent {
    title = 'Speakers';
    speakers: Speaker[];
    selectedSpeaker: Speaker;

    constructor(private speakerService: SpeakerService) { }

    getHeroes(): void {
        this.speakerService.getSpeakers().then(speakers => this.speakers = speakers);
    }

    ngOnInit(): void {
        this.getHeroes();
    }

    onSelect(speaker: Speaker): void {
        this.selectedSpeaker = speaker;
    }
}

