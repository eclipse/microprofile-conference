<h1>SPEAKER</h1>
<div *ngIf="speaker">
    <h1>{{title}}</h1>
    <h2>{{speaker.nameLast}} details!</h2>
    <div>
        <label>ID: </label>{{speaker.id}}
    </div>
    <div>
        <label>Surname: </label>
        <input type="text" [(ngModel)]="speaker.nameLast" placeholder="nameLast">
    </div>
</div>