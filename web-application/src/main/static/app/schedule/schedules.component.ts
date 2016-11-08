import {Component, enableProdMode, OnInit, ViewChild} from "@angular/core";
import {Router} from "@angular/router";
import {Schedule} from "./schedule";
import {ScheduleService} from "./schedule.service";
import {ScheduleModule} from "primeng/primeng";
import moment = require('moment');

enableProdMode();

@Component({
    selector: 'schedules',
    templateUrl: 'app/schedule/schedules.component.html'
})

export class SchedulesComponent implements OnInit {

    title = 'Schedules';
    schedules: Schedule[];
    selectedSchedule: Schedule;
    events: any[];
    header: any;

    @ViewChild('schedule')
    private pSchedule: ScheduleModule;

    constructor(private router: Router, private scheduleService: ScheduleService) {
    }

    ngOnInit(): void {
        let _self = this;
        this.scheduleService.init(function () {
            _self.getSchedules();
        });

        //No header
        this.header = false;

        var d = new Date();
        var year = d.getFullYear();
        var month = d.getMonth();
        var day = d.getDay();

        this.events = [
            {
                "title": "All Day Event",
                "start": new Date(year, month, day).toISOString().substring(0, 10)
            },
            {
                "title": "Long Event",
                "start": "2016-01-07",
                "end": new Date(year, month, day++).toISOString().substring(0, 10)
            },
            {
                "title": "Repeating Event",
                "start": new Date(year, month, day++).toISOString().substring(0, 10) + "2016-11-03T16:00:00"
            },
            {
                "title": "Repeating Event",
                "start": new Date(year, month, day++).toISOString().substring(0, 10) + "2016-11-14T16:00:00"
            },
            {
                "title": "Conference",
                "start": new Date(year, month, day++).toISOString().substring(0, 10),
                "end": new Date(year, month, day++).toISOString().substring(0, 10)
            }
        ];
    }

    getSchedules(): void {
        this.scheduleService.getSchedules().then(schedules => {
            this.setSchedules(schedules);
        }).catch(SchedulesComponent.handleError);
    }

    setSchedules(schedules: Schedule[]): void {
        this.schedules = schedules;
        this.updateEventsFromSchedules(this.schedules)
    }

    updateEventsFromSchedules(schedules: Schedule[]): any[] {
        var events = [];
        // for (let s of schedules) {
        //     var startTime = moment(s.date + "T" + s.startTime + ":00");
        //     var endTime = moment(startTime).add(1, "hour");
        //     events.push({
        //         "title": s.venue,
        //         "start": startTime,
        //         "end": endTime
        //     });
        // }
        return events;
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