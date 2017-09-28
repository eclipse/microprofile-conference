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
            console.log("Loaded schedules");
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
                "title": "Loading Events...",
                "start": new Date(year, month, day).toISOString().substring(0, 10)
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
        console.log("schedules.length: %d", this.schedules.length);
        this.events = this.toEvents(this.schedules);
    }

    toEvents(schedules: Schedule[]): any[] {
        var events: any[] = [];
        var self = this;

        let begin = momentConstructor();

        schedules.forEach(function (s: Schedule) {

            //date,duration,venue,venueId,startTime,id,sessionId
            //dayOfWeek,month,dayOfMonth,dayOfYear,era,year,monthValue,chronology,leapYear

            console.log(s);
            var datetime = s.date + " " + s.startTime;
            let d = new Date(datetime);
            let start = momentConstructor(d.toISOString());
            let end = start.add(1, 'hours');

            if (self.defaultDate.isAfter(start)) {
                self.defaultDate = start;
            }

            self.sessionService.getSessionsById([s.sessionId]).then(function (sessions: Session[]) {

                events.push({
                    "id": sessions[0].id,
                    "title": sessions[0].title,
                    "start": start,
                    "end": end,
                });
            });

        });

        console.log("First event date: %s", self.defaultDate.toISOString());
        //Go to the first event
        this.pSchedule.gotoDate(this.defaultDate.toISOString());

        return events;
    }

    handleEventClick(e: any) {
        //e.calEvent = Selected event
        //e.jsEvent = Browser click event
        //e.view = Current view object

        var self = this;

        this.schedules.forEach(function (s: Schedule) {

            if (s.sessionId === e.calEvent.id) {
                self.onSelect(s);
                self.gotoSession();
            }
        })
    }

    getType(o: any) {
        return ({}).toString.call(o).match(/\s([a-zA-Z]+)/)[1].toLowerCase();
    }

    onSelect(schedule: Schedule): void {
        this.selectedSchedule = schedule;
    }

    gotoSession(): void {
        this.router.navigate(['/sessions', {id: this.selectedSchedule.sessionId}]);
    }

    private static handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // TODO - Display safe error
        return Promise.reject(error.message || error);
    }
}
