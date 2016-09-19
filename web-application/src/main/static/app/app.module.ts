import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {FormsModule} from "@angular/forms";
import {HttpModule, JsonpModule } from "@angular/http";
import {AppComponent} from "./app.component";
import {AppRouting} from "./app.routing";
import {SpeakersComponent} from "./speaker/speakers.component";
import {SpeakerComponent} from "./speaker/speaker.component";
import {SpeakerService} from "./speaker/speaker.service";
import {SessionsComponent} from "./session/sessions.component";
import {SessionComponent} from "./session/session.component";
import {SessionService} from "./session/session.service";
import {SchedulesComponent} from "./schedule/schedules.component";
import {ScheduleComponent} from "./schedule/schedule.component";
import {ScheduleService} from "./schedule/schedule.service";
import {VotesComponent} from "./vote/votes.component";
import {VoteComponent} from "./vote/vote.component";
import {VoteService} from "./vote/vote.service";
import {EndpointsService} from "./shared/endpoints.service";
import {SpeakerFilter} from "./speaker/speaker.filter";

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        HttpModule,
        JsonpModule,
        AppRouting
    ],
    declarations: [
        AppComponent,
        SpeakersComponent,
        SpeakerComponent,
        SessionsComponent,
        SessionComponent,
        SchedulesComponent,
        ScheduleComponent,
        VotesComponent,
        VoteComponent,
        SpeakerFilter
    ],
    providers: [
        EndpointsService,
        SpeakerService,
        SessionService,
        ScheduleService,
        VoteService
    ],
    bootstrap: [
        AppComponent
    ]
})

export class AppModule {
}