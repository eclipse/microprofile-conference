import {Component, enableProdMode} from "@angular/core";
import { Schedule } from './schedule';

enableProdMode();

@Component({
    selector: 'schedule',
    templateUrl: 'app/schedule/schedule.component.html'
})

export class ScheduleComponent {
    schedule: Schedule;
}