//This is the same model our service emits
export class Speaker {
    id: string;
    nameFirst: string;
    nameLast: string;
    organization: string;
    biography: string;
    picture: string;
    twitterHandle: string;
    links: {[key: string]: string} = {};
}