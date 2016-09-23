import {Injectable, Pipe, PipeTransform} from "@angular/core";
import {Session} from "./session";
import {Speaker} from "../speaker/speaker";

@Pipe({
    name: 'sessionFilterSpeaker'
})
@Injectable()
export class SessionFilterSpeaker implements PipeTransform {

    transform(sessions: Session[], speaker: Speaker): Session[] {

        if (undefined === sessions) {
            return <Session[]>[];
        }

        if (undefined === speaker) {
            return sessions;
        }

        return sessions.filter(session => this.likeSession(session, speaker));
    }

    private likeSession(session: Session, speaker: Speaker): boolean {

        for (var s of session.speakers) {
            if (speaker.id == s) {
                return true;
            }
        }

        return false;
    }
}
