import { Alarm } from './alarm.model';

export interface AlarmsPage {
    content?: Alarm[];
    totalElements?: number;
}
