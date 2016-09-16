import {Component, enableProdMode} from "@angular/core";
import { Session } from './session';

enableProdMode();

@Component({
    selector: 'session',
    templateUrl: 'app/session/session.component.html'
})

export class SessionComponent {
    session: Session;
}