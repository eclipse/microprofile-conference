import {Component, enableProdMode} from "@angular/core";

enableProdMode();

@Component({
    selector: 'conference',
    templateUrl: 'app/app.component.html'
})

export class AppComponent {
    title = 'Microprofile Conference';
}