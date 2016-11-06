import {Component, enableProdMode, OnInit, ViewChild} from "@angular/core";
import {Router} from "@angular/router";
import {Schedule} from "./schedule";
import {ScheduleService} from "./schedule.service";
import {ScheduleModule} from 'primeng/primeng';

enableProdMode();

@Component({
    selector: 'schedules',
    templateUrl: 'app/schedule/schedules.component.html',
    directives: [ScheduleModule]
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
    private pSchedule : ScheduleModule;

    constructor(private router: Router, private scheduleService: ScheduleService) {
    }

    ngOnInit(): void {
        let _self = this;
        this.scheduleService.init(function () {
            _self.getSchedules();
        });

        //No header
        this.header = {left: '',
            center: '',
            right: 'agendaWeek, agendaDay, prev, next '
            };

        var today = moment().startOf('day');
        var thisWeek = moment().startOf('week').subtract(7, "days");

        this.events = [
            {
                "title": "All Day Event",
                "start": moment().startOf('week').add(9, "hours"),
                "end": moment().startOf('week').add(18, "hours")
            },
            {
                "title": "Long Event",
                "allDay": true,
                "start": "2016-01-07",
                "end": moment().startOf('day').add(1, "days")
            },
            {
                "title": "Repeating Event",
                "id": "repeating"
                "start": moment().startOf('week').add(12, "hours"),
                "start": moment().startOf('week').add(13, "hours")
            },
            {
                "title": "Repeating Event",
                "id": "repeating"
                "start": moment().startOf('week').add(12, "hours").add(1, "days"),
                "start": moment().startOf('week').add(13, "hours").add(1, "days")
            },
            {
                "title": "Repeating Event",
                "id": "repeating"
                "start": moment().startOf('week').add(12, "hours").add(2, "days"),
                "start": moment().startOf('week').add(13, "hours").add(2, "days")
            },
            {
                "title": "Conference",
                "allDay": true,
                "start": moment().startOf('week'),
                "end": moment().startOf('week').add(3, "days")
            }
        ];
    }

    getSchedules(): void {
        this.scheduleService.getSchedules().then(schedules => {
            this.schedules = schedules;
            this.events = this.updateEventsFromSchedules(this.schedules);
            if (this.events.length > 0) {
                this.defaultDate = this.events[0].start;
                this.pSchedule.gotoDate(this.defaultDate);
            }
        }).catch(SchedulesComponent.handleError);
    }

    updateEventsFromSchedules(schedules: Schedule[]) : any[] {
        var events = [];
        for (let s of schedules) {
            var startTime = moment(s.date + "T" + s.startTime + ":00");
            var endTime = moment(startTime).add(1, "hour");
            events.push({
                "title" : s.venue,
                "start" : startTime,
                "end" : endTime
            });
        }
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