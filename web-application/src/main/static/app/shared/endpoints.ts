import { Endpoint } from './endpoint';

export class Endpoints {
    application: string;
    endpoints: Endpoint[];
    links: {[key: string]: string} = {};
}