import {Injectable, Pipe, PipeTransform} from "@angular/core";
import {Speaker} from "./speaker";

@Pipe({
    name: 'speakerFilter'
})
@Injectable()
export class SpeakerFilter implements PipeTransform {

    transform(speakers: Speaker[], search: string): Speaker[] {

        if (undefined === search || search.trim().length < 1) {
            return speakers;
        }

        var split = search.trim().split(" ");

        console.log('filter: ' + search);
        return speakers.filter(speaker => this.likeSpeaker(speaker, split));
    }

    private likeSpeaker(speaker: Speaker, search: string[]) : boolean {

        if (search.length === 1) {
            return speaker.nameFirst.toLowerCase().includes(search[0].toLowerCase()) || speaker.nameLast.toLowerCase().includes(search[0].toLowerCase());
        } else {
            return speaker.nameFirst.toLowerCase().includes(search[0].toLowerCase()) && speaker.nameLast.toLowerCase().includes(search[1].toLowerCase())
                || speaker.nameFirst.toLowerCase().includes(search[1].toLowerCase()) && speaker.nameLast.toLowerCase().includes(search[0].toLowerCase());
        }
    }
}
