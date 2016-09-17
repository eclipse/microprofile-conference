import {Component, enableProdMode, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {Schedule} from "./schedule";
import {ScheduleService} from "./schedule.service";
import {EndpointsService} from "../shared/endpoints.service";
import {Endpoint} from "../shared/endpoint";

enableProdMode();

@Component({
    selector: 'schedules',
    templateUrl: 'app/schedule/schedules.component.html'
})

export class SchedulesComponent implements OnInit {
    title = 'Schedules';
    schedules: Schedule[];
    selectedSchedule: Schedule;
    endPoint: Endpoint;

    constructor(private router: Router, private scheduleService: ScheduleService, private endpointsService: EndpointsService) {
    }

    getEndpoint(): void {
        this.endpointsService.getEndpoint("schedule").then(endPoint => this.setEndpoint(endPoint));
    }

    setEndpoint(endPoint: Endpoint): void {
        this.endPoint = endPoint;
        this.getSchedules();
    }

    getSchedules(): void {
        //noinspection TypeScriptUnresolvedFunction
        this.scheduleService.getSchedules(this.endPoint).then(schedules => this.schedules = schedules).catch(this.handleError);
    }

    ngOnInit(): void {
        this.getEndpoint();
    }

    onSelect(schedule: Schedule): void {
        this.selectedSchedule = schedule;
    }

    gotoDetail(): void {
        this.router.navigate(['/detail', this.selectedSchedule.id]);
    }

    //noinspection TypeScriptUnresolvedVariable
    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // TODO - Display safe error
        //noinspection TypeScriptUnresolvedVariable
        return Promise.reject(error.message || error);
    }
}