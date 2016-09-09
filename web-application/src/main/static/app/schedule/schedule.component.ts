import {Component, enableProdMode} from "@angular/core";
import { Schedule } from './schedule';

enableProdMode();

@Component({
    selector: 'schedule',
    templateUrl: 'schedule.component.jsp'
})

export class ScheduleComponent {
    schedule: Schedule;
}