import {Component, enableProdMode, OnInit, ViewChild} from "@angular/core";
import {Router} from "@angular/router";
import {VoteService} from "./vote.service";
import {Rating} from "./rating";
import {UIChart} from "primeng/components/chart/chart";

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

    constructor(private router: Router, private voteService: VoteService) {
    }

    ngOnInit(): void {
        let _self = this;
        this.voteService.init(function () {
            _self.getVotes();
        });

        this.options = {
            title: {
                display: false,
            },
            legend: {
                position: 'right'
            }
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
      this.chart.data = this.data;
      this.chart.refresh();
    }

    toData(votes: Rating[]): any {

      var labelSet:any = ["Loved the sessions", "Satisfied", "Felt they needed work"];
      var dataValues:any = [];
      var negatives:number = 0;
      var wasOk: number = 0;
      var positives: number = 0;
      votes.forEach(function (v: Rating) {
        if (v.rating < 4) {
          negatives++;
        } else if (v.rating < 8) {
          wasOk++;
        } else {
          positives++;
        }
      });

      dataValues = [positives, wasOk, negatives];

      var data = {
            labels: labelSet,
            datasets: [
                {
                    data: dataValues,
                    backgroundColor: [
                        "#36A2EB",
                        "#FFCE56",
                        "#FF6384"
                    ],
                    hoverBackgroundColor: [
                        "#36A2EB",
                        "#FFCE56",
                        "#FF6384"
                    ]
                }]
        };

        var self = this;

        return data;
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
