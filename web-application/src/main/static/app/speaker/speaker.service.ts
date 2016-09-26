import {Injectable} from "@angular/core";
import {Speaker} from "./speaker";
import {Endpoint} from "../shared/endpoint";
import {Http} from "@angular/http";
import "../rxjs-operators";
import {EndpointsService} from "../shared/endpoints.service";

@Injectable()
export class SpeakerService {

    private speakers: Speaker[];
    private endPoint: Endpoint;

    constructor(private http: Http, private endpointsService: EndpointsService) {
    }

    /**
     * A service cannot have ngOnInit(), and so needs to be initialized
     * @param callback
     */
    init(callback: () => void): void {

        if (undefined != this.endPoint) {
            callback();
        } else {
            this.endpointsService.getEndpoint("speaker").then(endPoint => this.setEndpoint(endPoint)).then(callback).catch(this.handleError);
        }
    }

    setEndpoint(endPoint: Endpoint): void {
        this.endPoint = endPoint;
    }

    getSpeakers(): Promise<Speaker[]> {

        if (undefined != this.speakers) {
            return Promise.resolve(this.speakers);
        }

        return this.http.get(this.endPoint.url)
            .toPromise()
            .then(response => this.setSpeakers(response.json()))
            .catch(this.handleError);
    }

    private setSpeakers(any: any): Speaker[] {
        this.speakers = any as Speaker[];
        return this.speakers;
    }

    getSpeakersById(ids: string[]): Promise<Speaker[]> {

        if (undefined == this.endPoint) {
            console.error("init must be called at least once");
        }

        return this.getSpeakers().then(speakers => this.setSpeakers(speakers).filter(speaker => this.isIn(speaker, ids)));
    }

    private isIn(speaker: Speaker, ids: string[]): boolean {
        for (var id of ids) {
            if (speaker.id == id) {
                return true;
            }
        }

        return false;
    }

    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // TODO - Display safe error
        return Promise.reject(error.message || error);
    }
}