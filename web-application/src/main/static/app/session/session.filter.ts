import {Injectable, Pipe, PipeTransform} from "@angular/core";
import {Session} from "./session";

@Pipe({
    name: 'sessionFilter'
})
@Injectable()
export class SessionFilter implements PipeTransform {

    transform(sessions: Session[], search: string): Session[] {

        if (undefined === search || search.trim().length < 1) {
            return sessions;
        }

        console.log('filter: ' + search);
        return sessions.filter(session => this.likeSession(session, search));
    }

    private likeSession(session: Session, search: string): boolean {

        return session.title.toLowerCase().includes(search.toLowerCase()) || session.abstract.toLowerCase().includes(search.toLowerCase());
    }
}
