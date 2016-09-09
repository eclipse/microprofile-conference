import { Injectable } from '@angular/core';
import { Schedule } from './schedule';

@Injectable()
export class ScheduleService {

    schedules: Schedule[];

    getSchedules(): void {
    }
}