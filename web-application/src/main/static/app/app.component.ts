import {Component, enableProdMode} from "@angular/core";
import {Speaker} from "./speaker/speaker";

enableProdMode();

@Component({
    selector: 'microprofile-conference',
    template: `
  <h1>{{title}}</h1>
  <h2>Speakers</h2>
  <ul class="speakers">
    <li *ngFor="let speaker of speakers"
      [class.selected]="speaker === selectedSpeaker"
      (click)="onSelect(speaker)">
      <span class="badge">{{speaker.id}}</span> {{speaker.nameLast}}
    </li>
  </ul>
  <speaker [speaker]="selectedSpeaker"></speaker>
`,
})

export class AppComponent {
    title = 'Microprofile Conference';
    speakers = SPEAKERS;
}

const SPEAKERS: Speaker[] = [
    { id: 'a', nameLast: 'Mr. A', nameFirst: '', organization: '', twitterHandle: '', picture: '', biography: ''},
    { id: 'b', nameLast: 'Mr. B', nameFirst: '', organization: '', twitterHandle: '', picture: '', biography: ''},
    { id: 'c', nameLast: 'Mr. C', nameFirst: '', organization: '', twitterHandle: '', picture: '', biography: ''},
];