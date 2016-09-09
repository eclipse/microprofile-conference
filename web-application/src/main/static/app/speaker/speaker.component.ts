import {Component, Input, enableProdMode} from "@angular/core";
import {Speaker} from "./speaker";

enableProdMode();

@Component({
    selector: 'speaker',
    template: `
<div *ngIf="speaker">
  <h1>{{title}}</h1>
  <h2>{{speaker.nameLast}} details!</h2>
  <div>
    <label>ID: </label>{{speaker.id}}
  </div>
  <div>
    <label>Surname: </label>
    <input type="text" [(ngModel)]="speaker.nameLast" placeholder="nameLast">
  </div>
</div>  
  `
})

export class SpeakerComponent {
    title = 'Conference Speaker';
    @Input()
    speaker: Speaker;
}