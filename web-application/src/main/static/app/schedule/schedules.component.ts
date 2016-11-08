import {Component, enableProdMode, OnInit, ViewChild} from "@angular/core";
import {Router} from "@angular/router";
import {Schedule} from "./schedule";
import {ScheduleService} from "./schedule.service";
import {ScheduleModule} from "primeng/primeng";
import * as moment from "moment";

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
    maxTime: any = moment.duration(20, "hours");

    @ViewChild('schedule')
    private pSchedule: ScheduleModule;

    constructor(private router: Router, private scheduleService: ScheduleService) {
    }

    ngOnInit(): void {
        let _self = this;
        this.scheduleService.init(function () {
            _self.getSchedules();
        });

        this.header = {left: '',
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

        schedules.forEach(function (s: Schedule) {

            //date,duration,venue,venueId,startTime,id,sessionId
            //dayOfWeek,month,dayOfMonth,dayOfYear,era,year,monthValue,chronology,leapYear

            let d = new Date(s.date.monthValue + '/' + s.date.dayOfMonth + '/' + s.date.year);
            // console.log('date: ' + d.toISOString().slice(0, 10));
            var start = momentConstructor(d.toISOString().slice(0, 10)).add(s.startTime.hour, 'hours');
            var end = start.add(1, 'hours');
            // console.log("schedule: " + start.format());

            events.push({
                "title": s.venue,
                "start": start,
                "end": end,
            });
        });

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