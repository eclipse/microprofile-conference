import {ModuleWithProviders} from "@angular/core";
import {Routes, RouterModule} from "@angular/router";
import {SpeakersComponent} from "./speaker/speakers.component";
import {SessionsComponent} from "./session/sessions.component";
import {SchedulesComponent} from "./schedule/schedules.component";
import {VotesComponent} from "./vote/votes.component";

const appRoutes: Routes = [
    {
        path: '',
        redirectTo: '/speakers',
        pathMatch: 'full'
    },
    {
        path: 'speakers',
        component: SpeakersComponent
    },
    {
        path: 'sessions',
        component: SessionsComponent
    },
    {
        path: 'schedules',
        component: SchedulesComponent
    },
    {
        path: 'votes',
        component: VotesComponent
    }
];

export const AppRouting: ModuleWithProviders = RouterModule.forRoot(appRoutes);