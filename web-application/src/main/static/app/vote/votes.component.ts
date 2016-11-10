import {Component, enableProdMode, OnInit, ViewChild} from "@angular/core";
import {Router} from "@angular/router";
import {VoteService} from "./vote.service";
import {Rating} from "./rating";
import {UIChart} from "primeng/components/chart/chart";
import {SessionService} from "../session/session.service";
import {Session} from "../session/session";

enableProdMode();

@Component({
    selector: 'votes',
    templateUrl: 'app/vote/votes.component.html'
})

export class VotesComponent implements OnInit {
    title = 'Votes';
    votes: Rating[];
    selectedVote: Rating;
    data: any;
    options: any

    @ViewChild('chart')
    private chart: UIChart;

    constructor(private router: Router, private voteService: VoteService, private sessionService: SessionService) {
    }

    ngOnInit(): void {
        let _self = this;
        this.voteService.init(function () {
            _self.getVotes();
        });

        this.sessionService.init(function () {
            //no-op
        });

        this.options = {
            title: {
                display: false,
            },
            legend: {
                position: 'right'
            }
        };

        this.data = {
            labels: ['A', 'B', 'C'],
            datasets: [
                {
                    data: [300, 50, 100],
                    backgroundColor: [
                        "#FF6384",
                        "#36A2EB",
                        "#FFCE56"
                    ],
                    hoverBackgroundColor: [
                        "#FF6384",
                        "#36A2EB",
                        "#FFCE56"
                    ]
                }]
        };
    }

    getVotes(): void {
        this.voteService.getVotes().then(votes => {
            this.setVotes(votes);
        }).catch(VotesComponent.handleError);
    }

    setVotes(votes: Rating[]): void {
        this.votes = votes;
        this.data = this.toData(this.votes);
    }

    toData(votes: Rating[]): any[] {

        var data = {
            labels: ['A', 'B', 'C'],
            datasets: [
                {
                    data: [300, 50, 100],
                    backgroundColor: [
                        "#FF6384",
                        "#36A2EB",
                        "#FFCE56"
                    ],
                    hoverBackgroundColor: [
                        "#FF6384",
                        "#36A2EB",
                        "#FFCE56"
                    ]
                }]
        };

        var events: any[] = [];
        var self = this;

        votes.forEach(function (v: Rating) {

            self.sessionService.getSessionsById([v.session]).then(function (sessions: Session[]) {

                events.push({
                    "id": sessions[0].id,
                    "title": sessions[0].title,
                    "start": start,
                    "end": end,
                });
            });

        });

        //Go to the first event
        this.pSchedule.gotoDate(this.defaultDate);

        return events;
    }


    selectData(event: any) {
        //event.dataset = Selected dataset
        //event.element = Selected element
        //event.element._datasetIndex = Index of the dataset in data
        //event.element_index = Index of the data in dataset
    }

    gotoDetail(): void {
        this.router.navigate(['/sessions', {id: this.selectedVote.session}]);
    }

    private static handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // TODO - Display safe error
        return Promise.reject(error.message || error);
    }
}