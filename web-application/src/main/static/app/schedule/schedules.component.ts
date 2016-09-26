import {Component, enableProdMode, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {Schedule} from "./schedule";
import {ScheduleService} from "./schedule.service";

enableProdMode();

@Component({
    selector: 'schedules',
    templateUrl: 'app/schedule/schedules.component.html'
})

export class SchedulesComponent implements OnInit {
    title = 'Schedules';
    schedules: Schedule[];
    selectedSchedule: Schedule;

    constructor(private router: Router, private scheduleService: ScheduleService) {
    }

    ngOnInit(): void {
        let _self = this;
        this.scheduleService.init(function () {
            _self.getSchedules();
        });
    }

    getSchedules(): void {
        this.scheduleService.getSchedules().then(schedules => this.schedules = schedules).catch(SchedulesComponent.handleError);
    }

    onSelect(schedule: Schedule): void {
        this.selectedSchedule = schedule;
    }

    gotoDetail(): void {
        this.router.navigate(['/detail', this.selectedSchedule.id]);
    }

    private static handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // TODO - Display safe error
        return Promise.reject(error.message || error);
    }
}