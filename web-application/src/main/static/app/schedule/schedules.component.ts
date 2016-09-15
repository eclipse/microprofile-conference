import {Component, enableProdMode, OnInit} from "@angular/core";
import {Schedule} from "./schedule";
import {ScheduleService} from "./schedule.service";

enableProdMode();

@Component({
    selector: 'schedules',
    templateUrl: 'app/schedule/schedules.component.jsp'
})

export class SchedulesComponent implements OnInit {
    title = 'Conference Schedules';
    schedules: Schedule[];
    selectedSchedule: Schedule;

    constructor(private scheduleService: ScheduleService) {
    }

    getSchedules(): void {
        //noinspection TypeScriptUnresolvedFunction
        this.scheduleService.getSchedules().then(schedules => this.schedules = schedules);
    }

    ngOnInit(): void {
        this.getSchedules();
    }

    onSelect(schedule: Schedule): void {
        this.selectedSchedule = schedule;
    }
}