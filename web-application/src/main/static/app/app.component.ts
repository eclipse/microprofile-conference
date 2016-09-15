import {Component} from "@angular/core";

@Component({
    selector: 'microprofile-conference',
    template: `
  <nav>
      <a routerLink="/speakers" routerLinkActive="active">Speakers</a>
      <a routerLink="/sessions">Sessions</a>
      <a routerLink="/schedules">Schedules</a>
      <a routerLink="/votes">Votes</a>
    </nav>
    <router-outlet></router-outlet>
`,
})

export class AppComponent {
    title = 'Microprofile Conference';
}

