import {Component, enableProdMode} from "@angular/core";
import { Session } from './session';

enableProdMode();

@Component({
    selector: 'session',
    templateUrl: 'session.component.jsp'
})

export class SessionComponent {
    session: Session;
}