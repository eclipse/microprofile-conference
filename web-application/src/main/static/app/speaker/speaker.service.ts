import { Injectable } from '@angular/core';
import { Speaker } from './speaker';

@Injectable()
export class SpeakerService {

    //noinspection TypeScriptUnresolvedVariable
    getSpeakers(): Promise<Speaker[]> {
        //noinspection TypeScriptUnresolvedVariable
        return Promise.resolve(SPEAKERS);
    }
}

const SPEAKERS: Speaker[] = [
    {id: 'a', nameLast: 'Mr. A', nameFirst: '', organization: '', twitterHandle: '', picture: '', biography: ''},
    {id: 'b', nameLast: 'Mr. B', nameFirst: '', organization: '', twitterHandle: '', picture: '', biography: ''},
    {id: 'c', nameLast: 'Mr. C', nameFirst: '', organization: '', twitterHandle: '', picture: '', biography: ''},
];