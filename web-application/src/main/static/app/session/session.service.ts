import {Injectable} from "@angular/core";
import {Session} from "./session";

@Injectable()
export class SessionService {

    sessions: Session[];

    getSessions(): void {
    }
}