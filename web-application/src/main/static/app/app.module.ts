import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {FormsModule} from "@angular/forms";
import {AppComponent} from "./app.component";
import {SpeakerComponent} from "./speaker/speaker.component";
import {SessionComponent} from "./session/session.component";
import {ScheduleComponent} from "./schedule/schedule.component";
import {VoteComponent} from "./vote/vote.component";
//Conference Components

@NgModule({
    imports: [
        BrowserModule,
        FormsModule
    ],
    declarations: [
        AppComponent,
        SpeakerComponent,
        SessionComponent,
        ScheduleComponent,
        VoteComponent
    ],
    bootstrap: [
        AppComponent
    ]
})

export class AppModule {
}