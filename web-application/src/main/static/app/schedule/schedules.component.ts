import {Component, enableProdMode, OnInit, ViewChild} from "@angular/core";
import {Router} from "@angular/router";
import {Schedule} from "./schedule";
import {ScheduleService} from "./schedule.service";
import {Schedule as NGShedule} from "primeng/primeng";
import * as moment from "moment";
import {SessionService} from "../session/session.service";
import {Session} from "../session/session";

const momentConstructor: (value?: any) => moment.Moment = (<any>moment).default || moment;

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
    defaultView: string = "agendaWeek";
    allDaySlot: boolean = false;
    minTime: any = moment.duration(8, "hours");
    maxTime: any = moment.duration(21, "hours");
    defaultDate: any = momentConstructor();
    aspectRatio: number = 2.1;

    @ViewChild('schedule')
    private pSchedule: NGShedule;

    constructor(private router: Router, private scheduleService: ScheduleService, private sessionService: SessionService) {
    }

    ngOnInit(): void {
        let _self = this;
        this.scheduleService.init(function () {
            _self.getSchedules();
        });
        this.sessionService.init(function () {
            //no-op
        });

        this.header = {
            left: '',
            center: '',
            right: 'agendaWeek, agendaDay, prev, next '
        };

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
        this.events = this.toEvents(this.schedules);

        //this.pSchedule.gotoDate(this.events[0].start);
    }

    toEvents(schedules: Schedule[]): any[] {
        var events = [];
        var self = this;

        let begin = momentConstructor();

        schedules.forEach(function (s: Schedule) {

            //date,duration,venue,venueId,startTime,id,sessionId
            //dayOfWeek,month,dayOfMonth,dayOfYear,era,year,monthValue,chronology,leapYear

            let d = new Date(s.date.monthValue + '/' + s.date.dayOfMonth + '/' + s.date.year);
            let start = momentConstructor(d.toISOString().slice(0, 10)).add(s.startTime.hour, 'hours');
            let end = start.add(1, 'hours');

            if (self.defaultDate.isAfter(start)) {
                self.defaultDate = start;
            }

            self.sessionService.getSessionsById([s.sessionId]).then(function (sessions: Session[]) {

                events.push({
                    "title": sessions[0].title,
                    "start": start,
                    "end": end,
                });
            });

        });

        console.log("schedule: " + Object.keys(this.pSchedule));
        this.pSchedule.gotoDate(this.defaultDate);

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